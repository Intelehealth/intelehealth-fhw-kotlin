package org.intelehealth.app.ui.prescription.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.ajalt.timberkt.Timber
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentSymptomsDiagnosisBinding
import org.intelehealth.app.ui.prescription.viewmodel.PrescriptionDetailsViewModel

/**
 * Created by Vaghela Mithun R. on 09-07-2025 - 13:43.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class SymptomDiagnosisFragment : Fragment(R.layout.fragment_symptoms_diagnosis) {
    private lateinit var binding: FragmentSymptomsDiagnosisBinding
    private val viewModel: PrescriptionDetailsViewModel by activityViewModels<PrescriptionDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSymptomsDiagnosisBinding.bind(view)
        toggleDiagnosisInfoCardVisibility()
        binding.lblDiagnosis.isSelected = true
        observerDiagnosisDetails()
    }

    private fun toggleDiagnosisInfoCardVisibility() {
        binding.lblDiagnosis.setOnClickListener {
            it.isSelected = !it.isSelected
            binding.groupDiagnosisInfo.isVisible = it.isSelected
        }
    }

    private fun observerDiagnosisDetails() {
        viewModel.getDiagnosisDetails().observe(viewLifecycleOwner) {
            it ?: return@observe
            Timber.d { "Diagnosis => $it" }
            binding.value = viewModel.formatToBulletPoints(it)
        }
    }
}
