package org.intelehealth.app.ui.prescription.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPrescriptionPendingBinding
import org.intelehealth.app.ui.prescription.adapter.PrescriptionRecyclerViewAdapter
import org.intelehealth.app.ui.prescription.viewmodel.PrescriptionViewModel
import org.intelehealth.common.extensions.setupLinearView
import org.intelehealth.common.state.Result
import org.intelehealth.common.ui.fragment.BaseProgressFragment
import org.intelehealth.common.ui.viewholder.BaseViewHolder

class PrescriptionPendingFragment : BaseProgressFragment(R.layout.fragment_prescription_pending),
    BaseViewHolder.ViewHolderClickListener {
    lateinit var binding: FragmentPrescriptionPendingBinding
    override val viewModel: PrescriptionViewModel by viewModels<PrescriptionViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPrescriptionPendingBinding.bind(view)
        binding.viewModel = viewModel
        bindProgressView(binding.progressBar)
        bindPrescriptionAdapter()
    }

    private fun bindPrescriptionAdapter() {
        viewModel.receivedPrescription.observe(viewLifecycleOwner) {
            it ?: return@observe
            //for now added dummy result
            viewModel.handleResponse(Result.Success(it,"")) { result ->
                Timber.d { Gson().toJson(result) }
                val adapter = PrescriptionRecyclerViewAdapter(
                    requireActivity(),
                    it.toMutableList()
                )
                adapter.viewHolderClickListener = this
                binding.recentView.recyclerRecent.setupLinearView(adapter, true)
            }

        }
    }

    override fun onViewHolderViewClicked(view: View?, position: Int) {
        findNavController().navigate(PrescriptionListFragmentDirections.actionNavPrescriptionListToVisitDetailsFragment())
    }
}