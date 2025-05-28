package org.intelehealth.app.ui.prescription.fragment

import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPrescriptionReceiveBinding
import org.intelehealth.app.ui.prescription.adapter.PrescriptionRecyclerViewAdapter
import org.intelehealth.app.ui.prescription.viewmodel.PrescriptionViewModel
import org.intelehealth.common.enums.LoadingType
import org.intelehealth.common.extensions.setupLinearView
import org.intelehealth.common.state.Result
import org.intelehealth.common.ui.fragment.BaseProgressFragment
import org.intelehealth.common.ui.viewholder.BaseViewHolder


@AndroidEntryPoint
class PrescriptionReceiveFragment : BaseProgressFragment(R.layout.fragment_prescription_receive),
    BaseViewHolder.ViewHolderClickListener {
    lateinit var binding: FragmentPrescriptionReceiveBinding
    override val viewModel: PrescriptionViewModel by viewModels<PrescriptionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPrescriptionReceiveBinding.bind(view)
        binding.viewModel = viewModel
        bindProgressView(binding.progressBar, binding.progressBarPage)
        bindPrescriptionAdapter()
        bindScrollListener()
    }

    private fun bindScrollListener() {
        binding.nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, _, scrollY, _, oldScrollY ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if ((scrollY >= (v.getChildAt(0).measuredHeight - v.measuredHeight))
                    && scrollY > oldScrollY
                ) {
                    viewModel.fetchReceivedPrescription(LoadingType.PAGINATION)
                }
            }
        }
    }

    private fun bindPrescriptionAdapter() {
        var recentAdapter: PrescriptionRecyclerViewAdapter? = null
        var pendingAdapter: PrescriptionRecyclerViewAdapter? = null

        viewModel.receivedRecentPrescription.observe(viewLifecycleOwner) {
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

        viewModel.receivedOlderPrescription.observe(viewLifecycleOwner) {
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

    override fun onViewHolderViewClicked(view: View?, position: Int) {
        findNavController().navigate(PrescriptionListFragmentDirections.actionNavPrescriptionListToVisitDetailsFragment())
    }
}