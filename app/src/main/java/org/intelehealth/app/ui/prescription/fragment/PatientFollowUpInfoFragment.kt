package org.intelehealth.app.ui.prescription.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPatientFollowUpInfoBinding
import org.intelehealth.app.ui.prescription.viewmodel.PrescriptionDetailsViewModel

/**
 * Created by Vaghela Mithun R. on 09-07-2025 - 13:46.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class PatientFollowUpInfoFragment : Fragment(R.layout.fragment_patient_follow_up_info) {
    private lateinit var binding: FragmentPatientFollowUpInfoBinding
    private val viewModel: PrescriptionDetailsViewModel by activityViewModels<PrescriptionDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPatientFollowUpInfoBinding.bind(view)
        binding.lblFollowUp.isSelected = true
        toggleFollowUpInfoCardVisibility()
        observerDoctorDetails()
    }

    private fun toggleFollowUpInfoCardVisibility() {
        binding.lblFollowUp.setOnClickListener {
            it.isSelected = !it.isSelected
            binding.followUpDetail.root.isVisible = it.isSelected
        }
    }

    private fun observerDoctorDetails() {
        viewModel.visitLiveDetail.observe(viewLifecycleOwner) {
            it ?: return@observe
            it.extractDoctorProfile()
            binding.visitDetail = it
        }
    }
}
