package org.intelehealth.app.ui.prescription.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPrescriptionPendingBinding
import org.intelehealth.app.ui.prescription.adapter.PrescriptionRecyclerViewAdapter
import org.intelehealth.app.ui.prescription.viewmodel.PrescriptionViewModel
import org.intelehealth.common.enums.LoadingType
import org.intelehealth.common.extensions.setupLinearView
import org.intelehealth.common.state.Result
import org.intelehealth.common.ui.fragment.BaseProgressFragment
import org.intelehealth.common.ui.viewholder.BaseViewHolder

/**
 * Created by Tanvir Hasan on 2-04-25
 * Email : mhasan@intelehealth.org
 *
 * PrescriptionPendingFragment: Displays a list of pending prescriptions.
 * Uses a RecyclerView to show pending prescriptions and navigates to details on item click.
 **/

@AndroidEntryPoint
class PrescriptionPendingFragment : BaseProgressFragment(R.layout.fragment_prescription_pending),
    BaseViewHolder.ViewHolderClickListener {
    lateinit var binding: FragmentPrescriptionPendingBinding
    override val viewModel: PrescriptionViewModel by viewModels<PrescriptionViewModel>()

    var recentAdapter: PrescriptionRecyclerViewAdapter? = null
    var pendingAdapter: PrescriptionRecyclerViewAdapter? = null

    /**
     * Called immediately after the Fragment's view has been created.
     *
     * Initializes ViewBinding, sets the ViewModel for data binding,
     * binds progress indicators, sets up RecyclerView adapters,
     * and initializes scroll and search listeners.
     *
     * @param view The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *     from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPrescriptionPendingBinding.bind(view) // Initialize ViewBinding
        viewModel.fetchPendingPrescription(LoadingType.INITIAL)
        binding.viewModel = viewModel // Set ViewModel for DataBinding

        // Set up UI components and listeners
        bindProgressView(binding.progressBar, binding.progressBarPage)
        bindPrescriptionAdapter()
        bindScrollListener()
        bindSearchView()
        bindPrescriptionCount()
    }


    /**
     * Sets up the SearchView listener to handle user search queries for pending prescriptions.
     *
     * - When a query is submitted: Fetches initial pending prescriptions matching the query.
     * - When the query text changes to empty: Resets adapters, clears the search query in
     *   the ViewModel, and fetches all initial pending prescriptions.
     */
    private fun bindSearchView() {
        binding.searchView.prescriptionSearchView.setOnQueryTextListener(object :
            OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        recentAdapter = null
                        pendingAdapter = null
                        viewModel.searchQueryPending = it
                        // Fetch initial list for the new search query
                        viewModel.fetchPendingPrescription(
                            LoadingType.INITIAL
                        ) // Pass 'it' (the query)
                        return true // Indicates the action was handled
                    }
                }
                return false // Query is null or empty, action not handled by us
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.isEmpty()) {
                        // Reset adapters
                        recentAdapter = null
                        pendingAdapter = null
                        viewModel.searchQueryPending = ""
                        // Fetch all initial pending prescriptions when search is cleared
                        viewModel.fetchPendingPrescription(LoadingType.INITIAL)
                    }
                }
                return false // Default: let the SearchView handle other text changes if needed
            }
        })
    }


    /**
     * Sets up a scroll listener on the NestedScrollView for pagination of pending prescriptions.
     *
     * When the user scrolls to the bottom of the pending prescriptions list,
     * it triggers fetching the next page of pending prescriptions from the ViewModel.
     */
    private fun bindScrollListener() {
        binding.nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, _, scrollY, _, oldScrollY ->
            // Ensure NestedScrollView has children to prevent NullPointerException
            if (v.getChildAt(v.childCount - 1) != null) {
                // Check if scrolled to the bottom (or very near it) AND scrolling downwards
                if ((scrollY >= (v.getChildAt(0).measuredHeight - v.measuredHeight)) // Scrolled to bottom
                    && scrollY > oldScrollY // Scrolling down
                ) {
                    // Fetch next page of PENDING prescriptions
                    viewModel.fetchPendingPrescription(LoadingType.PAGINATION)
                }
            }
        }
    }


    /**
     * Binds the prescription data to the RecyclerView adapter.
     * Observes the ViewModel for prescription updates and sets up the RecyclerView adapter.
     */
    private fun bindPrescriptionAdapter() {
        viewModel.pendingRecentPrescription.observe(viewLifecycleOwner) {
            it ?: return@observe
            if (recentAdapter == null) {
                recentAdapter = PrescriptionRecyclerViewAdapter(
                    requireActivity(),
                    it.toMutableList()
                )
                recentAdapter?.viewHolderClickListener = this
                binding.recentView.recentRv.setupLinearView(recentAdapter!!, false)
            } else {
                recentAdapter?.updateList(it.toMutableList())
            }
        }

        viewModel.pendingOlderPrescription.observe(viewLifecycleOwner) {
            it ?: return@observe
            if (pendingAdapter == null) {
                pendingAdapter = PrescriptionRecyclerViewAdapter(
                    requireActivity(),
                    it.toMutableList()
                )
                pendingAdapter?.viewHolderClickListener = this
                binding.olderView.olderRv.setupLinearView(pendingAdapter!!, false)
            } else {
                pendingAdapter?.updateList(it.toMutableList())
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
                org.intelehealth.resource.R.string.content_pending_visit_msg,
                prescriptionCounts.pending.toString()
            )
        }
    }


    /**
     * Handles the click event on a prescription item in the RecyclerView.
     * Navigates to the VisitDetailsFragment when a prescription item is clicked.
     * Params:
     * view - The clicked view.
     * position - The position of the clicked item in the RecyclerView.
     */
    override fun onViewHolderViewClicked(view: View?, position: Int) {
        findNavController().navigate(PrescriptionListFragmentDirections.actionNavPrescriptionListToVisitDetailsFragment())
    }
}