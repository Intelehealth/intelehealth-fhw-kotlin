package org.intelehealth.app.ui.visit.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.resource.R as ResourceR
import org.intelehealth.app.databinding.FragmentOpenVisitBinding
import org.intelehealth.app.ui.visit.adapter.OpenVisitAdapter
import org.intelehealth.app.ui.visit.viewmodel.OpenVisitViewModel
import org.intelehealth.common.extensions.setupLinearView
import org.intelehealth.common.extensions.showCommonDialog
import org.intelehealth.common.model.DialogParams
import org.intelehealth.common.ui.fragment.BaseProgressFragment
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import org.intelehealth.common.utility.CommonConstants
import org.intelehealth.config.presenter.feature.viewmodel.ActiveFeatureStatusViewModel
import org.intelehealth.config.room.entity.ActiveFeatureStatus
import org.intelehealth.data.offline.entity.VisitDetail
import java.util.LinkedList

/**
 * Created by Vaghela Mithun R. on 11-07-2025 - 12:05.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class OpenVisitFragment : BaseProgressFragment(R.layout.fragment_open_visit), BaseViewHolder.ViewHolderClickListener {
    override val viewModel: OpenVisitViewModel by viewModels()
    private val afsViewModel: ActiveFeatureStatusViewModel by viewModels()
    private lateinit var binding: FragmentOpenVisitBinding
    private lateinit var adapter: OpenVisitAdapter
    private lateinit var activeStatus: ActiveFeatureStatus

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOpenVisitBinding.bind(view)
        handleClickEvents()
        initOpenVisitListView()
        observeOpenVisitData()
        observePagingData()
        viewModel.getCurrentMonthOpenVisits()
        observeActiveFeatureStatus()
    }

    private fun observeActiveFeatureStatus() {
        afsViewModel.fetchActiveFeatureStatus().observe(viewLifecycleOwner) {
            activeStatus = it ?: return@observe
        }
    }

    /**
     * Handles click events for the "Add New Patient" button.
     *
     * Navigates to the AddPatientFragment when the button is clicked.
     */
    private fun handleClickEvents() {
        binding.noPatientFoundBlock.btnAddNewPatient.setOnClickListener {
//            findNavController().navigate(
//                PrescriptionFragmentDirections.actionNavPrescriptionToAddPatient(null)
//            )
        }
    }

    /**
     * Initializes the RecyclerView for displaying prescriptions.
     *
     * Sets up the PrescriptionAdapter with an empty list and configures it to handle item clicks.
     * The RecyclerView is set up with a linear layout manager.
     */
    private fun initOpenVisitListView() {
        adapter = OpenVisitAdapter(requireContext(), LinkedList())
        adapter.viewHolderClickListener = this
        binding.rvPrescriptionList.setupLinearView(adapter, false)
    }

    /**
     * Observes the prescription data for the current month.
     *
     * Updates the RecyclerView adapter with the prescription data when available.
     * Shows the data state group and hides the "no patient found" block when data is present.
     */
    private fun observeOpenVisitData() {
        viewModel.openVisitCurrentMonthLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe
            viewModel.handleResponse(it) { data ->
                adapter.updateItems(data.toMutableList())
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
                adapter.isLoading = false
                adapter.addItems(pagingData) // Add new items to the end of the list
                // Remove the footer if data size is less than the limit
                if (pagingData.size < CommonConstants.LIMIT) adapter.remove(adapter.itemCount - 1)
            }
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
        if (view.id == org.intelehealth.common.R.id.btnViewMore) {
            // Handle view more button click if needed
            adapter.isLoading = true
            viewModel.fetchOtherOpenVisitWithPagination()
            return
        } else if (view.id == R.id.cardItem) {
            Timber.d { "clicked position => $position" }
            val item = view.tag as? VisitDetail ?: return
            val visitId = item.visitId ?: return
            Timber.d { "VisitId nav => $visitId" }
            findNavController().navigate(OpenVisitFragmentDirections.actionNavOpenVisitToVisitDetails(visitId))
        } else if (view.id == R.id.btnCloseVisit) {
            Timber.d { "clicked position => $position" }
            val item = view.tag as? VisitDetail ?: return
            val visitId = item.visitId ?: return
            Timber.d { "VisitId nav => $visitId" }
            closeVisit(item)
//            findNavController().navigate(OpenVisitFragmentDirections.actionNavOpenVisitToVisitDetails(visitId))
        }
    }

    private fun closeVisit(visit: VisitDetail) {
        if (visit.hasPrescription == true) navigateToUserFeedback(visit.visitId)
        else if (activeStatus.restrictEndVisit) showWaitForPrescriptionDialog(visit)
        else showAlertNoPrescriptionDialog(visit)
    }

    private fun showAlertNoPrescriptionDialog(visit: VisitDetail) {
        showCommonDialog(
            DialogParams(
                icon = ResourceR.drawable.ic_circle_prescription,
                title = ResourceR.string.action_end_visit,
                message = ResourceR.string.content_close_visit_without_prescription,
                positiveLbl = ResourceR.string.action_end_visit,
                negativeLbl = ResourceR.string.action_cancel,
                onPositiveClick = { navigateToUserFeedback(visit.visitId) }
            )
        )
    }

    private fun showWaitForPrescriptionDialog(visit: VisitDetail) {
        showCommonDialog(
            DialogParams(
                icon = ResourceR.drawable.ic_circle_prescription,
                title = ResourceR.string.action_end_visit,
                message = ResourceR.string.content_prescription_pending
            )
        )
    }

    private fun checkFollowUpBeforeClosingVisit(visit: VisitDetail): Boolean {
        return false
    }

    private fun checkIfAnyAppointmentExists(visit: VisitDetail): Boolean {
        // Logic to check if any appointment exists for the visit
        return false // Placeholder return value
    }

    private fun navigateToUserFeedback(visitId: String?) {
//        findNavController().navigate(OpenVisitFragmentDirections.actionNavOpenVisitToUserFeedback(visitId))
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
        binding.noPatientFoundBlock.root.isVisible = true
    }
}
