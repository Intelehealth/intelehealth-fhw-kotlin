package org.intelehealth.app.ui.prescription.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.resource.R as resR
import org.intelehealth.common.R as CommonR
import org.intelehealth.app.databinding.FragmentPrescriptionReceiveBinding
import org.intelehealth.app.ui.prescription.adapter.PrescriptionAdapter
import org.intelehealth.app.ui.prescription.adapter.PrescriptionRecyclerViewAdapter
import org.intelehealth.app.ui.prescription.viewmodel.PrescriptionViewModel
import org.intelehealth.common.enums.LoadingType
import org.intelehealth.common.extensions.setupLinearView
import org.intelehealth.common.ui.fragment.BaseProgressFragment
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import java.util.LinkedList
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible

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
class PrescriptionReceiveFragment : BaseProgressFragment(R.layout.fragment_prescription_receive),
                                    BaseViewHolder.ViewHolderClickListener {
    private lateinit var binding: FragmentPrescriptionReceiveBinding
    override val viewModel: PrescriptionViewModel by viewModels<PrescriptionViewModel>()
    private lateinit var adapter: PrescriptionAdapter

    private var recentReceivedAdapter: PrescriptionRecyclerViewAdapter? = null
    private var olderReceivedAdapter: PrescriptionRecyclerViewAdapter? = null

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
        binding = FragmentPrescriptionReceiveBinding.bind(view)
        binding.progressBar.progressLayout.background = Color.TRANSPARENT.toDrawable()
        initPrescriptionListView()
        viewModel.getCurrentMonthReceivedPrescriptions()
        binding.viewModel = viewModel
        bindProgressView(binding.progressBar)
        observePrescriptionData()
        observePagingData()
//        bindSearchView()
//        bindScrollListener()
//        bindPrescriptionAdapter()
//        bindPrescriptionCount()
    }

    private fun initPrescriptionListView() {
        adapter = PrescriptionAdapter(requireActivity(), LinkedList())
        adapter.viewHolderClickListener = this
        binding.rvPrescriptionList.setupLinearView(adapter, false)
    }

    private fun observePrescriptionData() {
        viewModel.receivedPrescriptionLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe
            viewModel.handleResponse(it) { data ->
                adapter.updateItems(data.toMutableList())
//                binding.noPatientFoundBlock.root.isVisible = false
//                binding.groupDataState.isVisible = true
            }
        }
    }

    private fun observePagingData() {
        viewModel.receivedPrescriptionPagingLiveData.observe(viewLifecycleOwner) { pagingData ->
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
            }
        }
    }

    /**
     * Sets up the SearchView to filter prescriptions.
     *
     * Clears existing adapters and re-fetches prescriptions when the search query is submitted (if empty)
     * or when the search query text is cleared.
     */
    private fun bindSearchView() {
        binding.searchView.prescriptionSearchView.setOnQueryTextListener(object :
                                                                             SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    // Corrected logic: Fetch if query is submitted, not just if it's empty
                    // You'll likely want to handle non-empty queries to perform an actual search
                    if (it.isNotEmpty()) { // Or some other condition for submitting
                        recentReceivedAdapter = null
                        olderReceivedAdapter = null
                        viewModel.searchQueryReceived = it // Use 'it' (the actual query)
                        viewModel.fetchReceivedPrescription(LoadingType.INITIAL)
                    }
                }
                return false // Typically return true if you handled the action
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.isEmpty()) {
                        recentReceivedAdapter = null
                        olderReceivedAdapter = null
                        viewModel.searchQueryReceived = ""
                        viewModel.fetchReceivedPrescription(LoadingType.INITIAL)
                    }
                    // Consider adding logic for live search as text changes (with debounce)
                }
                return false // Typically return true if you handled the action
            }
        }
        )
    }

    /**
     * Sets up a scroll listener on the NestedScrollView to implement pagination.
     *
     * Fetches the next page of prescriptions when the user scrolls to the bottom of the list.
     */
    private fun bindScrollListener() {
        binding.nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, _, scrollY, _, oldScrollY ->
            if (v.getChildAt(v.childCount - 1) != null) { // Check if NestedScrollView has children
                // Check if scrolled to the bottom and scrolling downwards
                if ((scrollY >= (v.getChildAt(0).measuredHeight - v.measuredHeight))
                    && scrollY > oldScrollY
                ) {
                    viewModel.fetchReceivedPrescriptionWithPagination()
                }
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
        viewModel.prescriptionCount().observe(viewLifecycleOwner) { prescriptionCounts ->
            prescriptionCounts ?: return@observe // Exit if prescriptionCounts is null

            // Update the data binding variable with the count of pending prescriptions
            binding.patientCountView.text = requireActivity().getString(
                resR.string.content_patients_waiting_for_closure,
                prescriptionCounts.pending.toString()
            )
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
            viewModel.fetchReceivedPrescriptionWithPagination()
            return
        } else {
            findNavController().navigate(PrescriptionFragmentDirections.actionNavPrescriptionToVisitDetails())
        }
    }

    override fun onFailed(reason: String) {
        super.onFailed(reason)
        binding.groupDataState.isVisible = false
        binding.noPatientFoundBlock.root.isVisible = true
    }
}
