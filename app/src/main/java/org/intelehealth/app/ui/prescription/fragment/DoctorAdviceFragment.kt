package org.intelehealth.app.ui.prescription.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.github.ajalt.timberkt.Timber
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentDoctorAdviceBinding
import org.intelehealth.app.ui.prescription.viewmodel.PrescriptionDetailsViewModel

/**
 * Created by Vaghela Mithun R. on 09-07-2025 - 13:44.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class DoctorAdviceFragment : Fragment(R.layout.fragment_doctor_advice) {
    private lateinit var binding: FragmentDoctorAdviceBinding
    private val viewModel: PrescriptionDetailsViewModel by activityViewModels<PrescriptionDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDoctorAdviceBinding.bind(view)
        binding.lblAdvice.isSelected = true
        toggleAdviceInfoCardVisibility()
        observerMedicalAdviceDetails()
    }

    private fun toggleAdviceInfoCardVisibility() {
        binding.lblAdvice.setOnClickListener {
            it.isSelected = !it.isSelected
            binding.groupAdviceInfo.isVisible = it.isSelected
        }
    }

    private fun observerMedicalAdviceDetails() {
        viewModel.getMedicalAdviceDetails().observe(viewLifecycleOwner) {
            it ?: return@observe
            Timber.d { "Medical Advice => $it" }
            binding.value = viewModel.formatToBulletPoints(it)
        }
    }
}
