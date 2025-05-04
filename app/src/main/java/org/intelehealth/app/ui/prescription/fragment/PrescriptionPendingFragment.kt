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
import org.intelehealth.app.ui.prescription.adapter.PrescriptionRecyclerViewAdapter
import org.intelehealth.app.ui.prescription.viewmodel.PrescriptionViewModel
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
        bindProgressView(binding.progressBar)
        bindPrescriptionAdapter()
    }

    /**
     * Binds the prescription data to the RecyclerView adapter.
     * Observes the ViewModel for prescription updates and sets up the RecyclerView adapter.
     */
    private fun bindPrescriptionAdapter() {
        viewModel.pendingPrescription.observe(viewLifecycleOwner) {
            it ?: return@observe
            val adapter = PrescriptionRecyclerViewAdapter(
                requireActivity(),
                it.toMutableList()
            )
            adapter.viewHolderClickListener = this
            binding.recentView.recyclerRecent.setupLinearView(adapter, true)

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