package org.intelehealth.app.ui.prescription.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isNotEmpty
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPrescriptionListBinding
import org.intelehealth.app.ui.prescription.adapter.PrescriptionAdapter
import org.intelehealth.app.ui.prescription.viewmodel.PrescriptionViewModel
import org.intelehealth.common.extensions.getParcelableExtra
import org.intelehealth.common.extensions.getSerializableExtra
import org.intelehealth.common.extensions.setupLinearView
import org.intelehealth.common.extensions.showToast
import org.intelehealth.common.extensions.toHTML
import org.intelehealth.common.ui.fragment.BaseProgressFragment
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import org.intelehealth.common.utility.CommonConstants
import org.intelehealth.data.offline.entity.VisitDetail
import org.intelehealth.data.offline.entity.PrescriptionStatusCount
import java.util.LinkedList
import org.intelehealth.common.R as CommonR
import org.intelehealth.resource.R as ResourceR


/**
 * Created by Tanvir Hasan on 02-04-25
 * Email : mhasan@intelehealth.org
 *
 * PrescriptionReceiveFragment: Displays recent and older received prescriptions with search and pagination.
 * Fetches and shows received prescriptions in two RecyclerViews (recent and older).
 * Supports searching prescriptions and loading more items on scroll (pagination).
 * Navigates to prescription details on item click.
 */
@AndroidEntryPoint
class PrescriptionListFragment : BaseProgressFragment(R.layout.fragment_prescription_list),
                                 BaseViewHolder.ViewHolderClickListener, SearchView.OnQueryTextListener,
                                 PopupMenu.OnMenuItemClickListener {
    // Fragment for displaying received prescriptions
    private lateinit var binding: FragmentPrescriptionListBinding

    // ViewModel for managing prescription data and UI state
    override val viewModel: PrescriptionViewModel by viewModels<PrescriptionViewModel>()

    // Adapter for displaying prescriptions in a RecyclerView
    private lateinit var adapter: PrescriptionAdapter

    // Search query for filtering prescriptions
    private var searchQuery: String = ""

    // Type of prescription tab being displayed (received or pending)
    private var tabType: VisitDetail.TabType = VisitDetail.TabType.RECEIVED

    private lateinit var presCount: PrescriptionStatusCount

    /**
     * Called to do initial creation of the fragment.
     *
     * Initializes the ViewModel and sets up the toolbar title.
     *
     */
    private lateinit var filterPopupMenu: PopupMenu

    /**
     * Called when the fragment's view has been created.
     *
     * Initializes view binding, sets up the ViewModel, progress indicators,
     * RecyclerView adapters, scroll listener for pagination, and search view.
     *
     * @param view The View returned by onCreateView().
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPrescriptionListBinding.bind(view)
        extractExtra()
        binding.progressBar.progressLayout.background = Color.TRANSPARENT.toDrawable()
        bindProgressView(binding.progressBar)
        binding.tabName = getScreenTitle().lowercase()
        initPrescriptionListView()
        searchResult(searchQuery)
        observePrescriptionData()
        observePagingData()
        handleClickEvents()
        binding.searchView.prescriptionSearchView.setOnQueryTextListener(this)
        setupFilterMenuPopup()
        bindPrescriptionCount()
    }

    private fun extractExtra() {
        tabType = getSerializableExtra(EXT_TAB_TYPE, VisitDetail.TabType.RECEIVED)
        getParcelableExtra<PrescriptionStatusCount>(EXT_PRES_COUNT)?.let { presCount = it }
    }

    /**
     * Returns the title for the screen based on the tab type.
     *
     * @return A string representing the title of the screen.
     */
    private fun getScreenTitle(): String {
        return when (tabType) {
            VisitDetail.TabType.RECEIVED -> getString(ResourceR.string.title_received)
            VisitDetail.TabType.PENDING -> getString(ResourceR.string.title_pending)
        }
    }

    /**
     * Handles click events for the "Add New Patient" button.
     *
     * Navigates to the AddPatientFragment when the button is clicked.
     */
    private fun handleClickEvents() {
        binding.noPatientFoundBlock.btnAddNewPatient.setOnClickListener {
            findNavController().navigate(
                PrescriptionFragmentDirections.actionNavPrescriptionToAddPatient(null)
            )
        }

        binding.searchView.filterIcon.setOnClickListener {
            if (filterPopupMenu.menu.isNotEmpty()) filterPopupMenu.show()
            else showToast("No filters available")
        }
    }

    /**
     * Initializes the RecyclerView for displaying prescriptions.
     *
     * Sets up the PrescriptionAdapter with an empty list and configures it to handle item clicks.
     * The RecyclerView is set up with a linear layout manager.
     */
    private fun initPrescriptionListView() {
        adapter = PrescriptionAdapter(requireContext(), LinkedList())
        adapter.viewHolderClickListener = this
        binding.rvPrescriptionList.setupLinearView(adapter, false)
    }

    /**
     * Observes the prescription data for the current month.
     *
     * Updates the RecyclerView adapter with the prescription data when available.
     * Shows the data state group and hides the "no patient found" block when data is present.
     */
    private fun observePrescriptionData() {
        viewModel.prescriptionCurrentMonthLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe
            viewModel.handleResponse(it) { data ->
                adapter.updateItems(data.toMutableList())
                binding.groupDataState.isVisible = true
                binding.noPatientFoundBlock.root.isVisible = false
            }
        }
    }

    /**
     * Observes the paging data for prescriptions.
     *
     * Updates the RecyclerView adapter with new paging data when available.
     * Handles loading states and updates the visibility of the footer based on data availability.
     */
    private fun observePagingData() {
        viewModel.pagingLiveData.observe(viewLifecycleOwner) { pagingData ->
            pagingData ?: return@observe
            hideLoading()

            if (pagingData.isEmpty() && adapter.itemCount > 0) {
                adapter.remove(adapter.itemCount - 1) // Remove the footer if no more data
            } else if (adapter.itemCount > 2) {
                adapter.isLoading = false
                adapter.addItemsAt(adapter.itemCount - 1, pagingData) // Add new items before the footer
            } else if (pagingData.isNotEmpty()) {
                binding.groupDataState.isVisible = true
                adapter.isLoading = false
                adapter.addItems(pagingData) // Add new items to the end of the list
                // Remove the footer if data size is less than the limit
                if (pagingData.size < CommonConstants.LIMIT) adapter.remove(adapter.itemCount - 1)
            }
        }
    }

    /**
     * Observes the count of pending prescriptions from the ViewModel.
     *
     * Updates the 'awaitingPatientCount' data binding variable with the number of pending prescriptions.
     * This is likely used to display the count of patients awaiting prescriptions in the UI.
     */
    private fun bindPrescriptionCount() {
        // Update the data binding variable with the count of pending prescriptions
        if (::presCount.isInitialized) {
            val content = if (tabType == VisitDetail.TabType.RECEIVED) getString(
                ResourceR.string.content_patients_waiting_for_closure, presCount.pending.toString()
            )
            else getString(ResourceR.string.content_pending_visit_msg, presCount.pending.toString())
            binding.patientCountView.receivedEndVisitNo.text = content.toHTML()
        }
    }


    /**
     * Handles click events on items in the RecyclerView.
     *
     * Navigates to the VisitDetailsFragment when any prescription item is clicked.
     * The specific item clicked (based on 'position' or 'view') is not explicitly used for navigation logic here.
     *
     * @param view The clicked view within the RecyclerView item. Can be null.
     * @param position The adapter position of the clicked item.
     */
    override fun onViewHolderViewClicked(view: View?, position: Int) {
        view ?: return
        if (view.id == CommonR.id.btnViewMore) {
            // Handle view more button click if needed
            adapter.isLoading = true
            viewModel.fetchPrescriptionWithPagination(tabType, searchQuery)
            return
        } else if (view.id == R.id.cardPatientItem) {
            Timber.d { "clicked position => $position" }
            val item = view.tag as? VisitDetail ?: return
            val visitId = item.visitId ?: return
            Timber.d { "VisitId nav => $visitId" }
            findNavController().navigate(PrescriptionFragmentDirections.actionNavPrescriptionToVisitDetails(visitId))
        } else if (view.id == R.id.btnSharePrescription) {
            Timber.d { "clicked position => $position" }
            val item = view.tag as? VisitDetail ?: return
            val visitId = item.visitId ?: return
            Timber.d { "VisitId nav => $visitId" }
//            findNavController().navigate(PrescriptionFragmentDirections.actionNavPrescriptionToVisitDetails(visitId))
        }
    }

    /**
     * Called when the data fetch fails.
     *
     * Hides the data state group and shows the "no patient found" block when the data fetch fails.
     *
     * @param reason The reason for the failure, which can be logged or displayed to the user.
     */
    override fun onFailed(reason: String) {
        super.onFailed(reason)
        binding.groupDataState.isVisible = false
        binding.noPatientFoundBlock.root.isVisible = true
    }

    /**
     * Called when the data fetch encounters an error.
     *
     * Hides the data state group and shows the "no patient found" block when an error occurs.
     *
     */
    override fun onQueryTextSubmit(query: String?): Boolean {
        binding.searchView.prescriptionSearchView.clearFocus() // Clear focus after submitting the query
        query?.let { searchResult(it) }
        return true
    }

    /**
     * Called when the text in the search view changes.
     *
     * If the new text is empty, it triggers a search with an empty query.
     *
     * @param newText The new text entered in the search view.
     * @return True to indicate that the text change has been handled.
     */
    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let { if (it.isEmpty()) searchResult(it) }
        return true
    }

    /**
     * Searches for prescriptions based on the provided query.
     *
     * Clears the current adapter items, hides the "no patient found" block,
     * and fetches prescriptions for the current month based on the search query.
     *
     * @param query The search query to filter prescriptions.
     */
    private fun searchResult(query: String) {
        adapter.clear()
        binding.noPatientFoundBlock.root.isVisible = false
        viewModel.getCurrentMonthPrescriptions(tabType, query)
        searchQuery = query
    }

    private fun setupFilterMenuPopup() {
        filterPopupMenu = PopupMenu(requireContext(), binding.searchView.filterIcon)
        filterPopupMenu.menuInflater.inflate(R.menu.menu_filter_prescription, filterPopupMenu.menu)
        filterPopupMenu.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_view_all_visits -> searchResult(searchQuery)
            R.id.action_filter_priority_visits -> fetchPriorityPrescriptions()
        }

        return true
    }

    private fun fetchPriorityPrescriptions() {
        showToast("To be implemented")
    }

    /**
     * Called when the fragment is no longer visible to the user.
     *
     * Cleans up resources by clearing the adapter items and resetting the search query.
     */
    companion object {
        // Creates a new instance of PrescriptionListFragment with the specified tab type.
        private const val EXT_TAB_TYPE = "tab_type"
        private const val EXT_PRES_COUNT = "prescription_count"

        // Factory method to create a new instance of PrescriptionListFragment with the specified tab type.
        @JvmStatic
        fun newInstance(
            tab: VisitDetail.TabType = VisitDetail.TabType.RECEIVED, prescriptionStatusCount: PrescriptionStatusCount
        ): PrescriptionListFragment {
            return PrescriptionListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(EXT_TAB_TYPE, tab)
                    putParcelable(EXT_PRES_COUNT, prescriptionStatusCount)
                }
            }
        }
    }
}
