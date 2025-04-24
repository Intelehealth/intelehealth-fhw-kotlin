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
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPrescriptionPendingBinding
import org.intelehealth.app.databinding.FragmentPrescriptionReceiveBinding
import org.intelehealth.app.model.help.FAQItem
import org.intelehealth.app.ui.help.adapter.FAQAdapter
import org.intelehealth.app.ui.prescription.adapter.PrescriptionRecyclerViewAdapter
import org.intelehealth.app.ui.prescription.viewmodel.PrescriptionViewModel
import org.intelehealth.app.ui.user.viewmodel.UserViewModel
import org.intelehealth.app.utility.KEY_RESULTS
import org.intelehealth.common.extensions.setupLinearView
import org.intelehealth.common.state.Result
import org.intelehealth.common.ui.fragment.BaseProgressFragment
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.config.presenter.language.viewmodel.LanguageViewModel
import org.intelehealth.data.offline.entity.Patient


@AndroidEntryPoint
class PrescriptionReceiveFragment: BaseProgressFragment(R.layout.fragment_prescription_receive),
    BaseViewHolder.ViewHolderClickListener {
    lateinit var binding: FragmentPrescriptionReceiveBinding
    override val viewModel: PrescriptionViewModel by viewModels<PrescriptionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPrescriptionReceiveBinding.bind(view)
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