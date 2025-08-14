package org.intelehealth.app.ui.prescription.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.github.ajalt.timberkt.Timber
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPrescribedMedicationBinding
import org.intelehealth.app.ui.prescription.adapter.MedicationAdapter
import org.intelehealth.app.ui.prescription.viewmodel.PrescriptionDetailsViewModel
import org.intelehealth.common.extensions.setupLinearView
import org.intelehealth.data.offline.model.Medication

/**
 * Created by Vaghela Mithun R. on 09-07-2025 - 13:45.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class PrescribedMedicationFragment : Fragment(R.layout.fragment_prescribed_medication) {
    private lateinit var binding: FragmentPrescribedMedicationBinding
    private val viewModel: PrescriptionDetailsViewModel by activityViewModels<PrescriptionDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPrescribedMedicationBinding.bind(view)
        togglePrescribedMedicationInfoCardVisibility()
        binding.lblPrescribedMedication.isSelected = true
        observerMedicationDetails()
    }

    private fun togglePrescribedMedicationInfoCardVisibility() {
        binding.lblPrescribedMedication.setOnClickListener {
            it.isSelected = !it.isSelected
            val visibility = binding.medicationsTv.isVisible
            binding.medicationsTv.isVisible = if (visibility) it.isSelected else false
            binding.medicationInfoInfoExpandableLayoutGroup.isVisible = it.isSelected
        }
    }

    private fun observerMedicationDetails() {
        viewModel.getPrescribedMedicationDetails()
        viewModel.medicationLiveData.observe(viewLifecycleOwner) {
            Timber.d { "Prescribed Medication Details => $it" }
            it ?: return@observe
            binding.medicationsTv.isVisible = false
            setupMedicationAdapter(it)
        }
    }

    private fun setupMedicationAdapter(medications: List<Medication>) {
        val adapter = MedicationAdapter(requireContext(), medications)
        binding.rvMedication.setupLinearView(adapter, hasItemDecoration = true)
    }
}
