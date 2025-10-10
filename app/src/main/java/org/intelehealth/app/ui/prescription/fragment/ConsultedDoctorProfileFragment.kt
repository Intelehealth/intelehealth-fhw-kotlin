package org.intelehealth.app.ui.prescription.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentConsultedDoctorProfileBinding
import org.intelehealth.app.ui.prescription.viewmodel.PrescriptionDetailsViewModel

/**
 * Created by Vaghela Mithun R. on 09-07-2025 - 13:37.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class ConsultedDoctorProfileFragment : Fragment(R.layout.fragment_consulted_doctor_profile) {
    private lateinit var binding: FragmentConsultedDoctorProfileBinding
    private val viewModel: PrescriptionDetailsViewModel by activityViewModels<PrescriptionDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentConsultedDoctorProfileBinding.bind(view)
        observerDoctorDetails()
        toggleDoctorCardVisibility()
        binding.lblConsultedDoctor.isSelected = true
    }

    private fun toggleDoctorCardVisibility() {
        binding.lblConsultedDoctor.setOnClickListener {
            it.isSelected = !it.isSelected
            binding.drInfoInfoExpandableLayoutGroup.isVisible = it.isSelected
        }
    }

    private fun observerDoctorDetails() {
        viewModel.visitLiveDetail.observe(viewLifecycleOwner) {
            it ?: return@observe
            it.extractDoctorProfile()
            binding.doctorDetail = it.doctorProfile
            getDoctorAge(it.doctorProfile?.uuid ?: return@observe)
        }
    }

    private fun getDoctorAge(doctorId: String) {
        viewModel.getDoctorAge(doctorId).observe(viewLifecycleOwner) { age ->
            age ?: return@observe
            binding.doctorDetail?.age = age
        }
    }
}
