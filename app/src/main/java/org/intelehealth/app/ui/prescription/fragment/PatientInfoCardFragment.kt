package org.intelehealth.app.ui.prescription.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPatientCardBinding
import org.intelehealth.app.ui.prescription.viewmodel.PrescriptionDetailsViewModel

/**
 * Created by Vaghela Mithun R. on 09-07-2025 - 15:36.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class PatientInfoCardFragment : Fragment(R.layout.fragment_patient_card) {
    private lateinit var binding: FragmentPatientCardBinding
    private val viewModel: PrescriptionDetailsViewModel by activityViewModels<PrescriptionDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPatientCardBinding.bind(view)
        observerPatientDetails()
        togglePatientInfoCardVisibility()
        binding.ivBtnExpandPatientDetails.isSelected = true
    }

    private fun togglePatientInfoCardVisibility() {
        binding.ivBtnExpandPatientDetails.setOnClickListener {
            it.isSelected = !it.isSelected
            binding.chwInfoExpandableLayoutGroup.isVisible = it.isSelected
        }
    }

    private fun observerPatientDetails() {
        viewModel.visitLiveDetail.observe(viewLifecycleOwner) {
            it ?: return@observe
            binding.visitDetail = it
        }
    }
}
