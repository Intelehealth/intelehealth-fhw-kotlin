package org.intelehealth.app.ui.prescription.fragment

import android.os.Bundle
import android.view.View
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPrescriptionPendingBinding.bind(view)
        binding.viewModel = viewModel
        bindProgressView(binding.progressBar, binding.progressBarPage)
        bindPrescriptionAdapter()
        bindScrollListener()
        bindSearchView()
    }

    private fun bindSearchView() {
        binding.searchView.prescriptionSearchView.setOnQueryTextListener(object :
            OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query?.isNotEmpty() == true) {
                    viewModel.fetchPendingPrescription(LoadingType.INITIAL, query)
                    return true
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        }
        )
    }

    private fun bindScrollListener() {
        binding.nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, _, scrollY, _, oldScrollY ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if ((scrollY >= (v.getChildAt(0).measuredHeight - v.measuredHeight))
                    && scrollY > oldScrollY
                ) {
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
        var recentAdapter: PrescriptionRecyclerViewAdapter? = null
        var pendingAdapter: PrescriptionRecyclerViewAdapter? = null

        viewModel.pendingRecentPrescription.observe(viewLifecycleOwner) {
            it ?: return@observe
            viewModel.handleResponse(Result.Success(it, "")) { result ->
                Timber.d { Gson().toJson(result) }
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

        }

        viewModel.pendingOlderPrescription.observe(viewLifecycleOwner) {
            it ?: return@observe
            viewModel.handleResponse(Result.Success(it, "")) { result ->
                Timber.d { Gson().toJson(result) }
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