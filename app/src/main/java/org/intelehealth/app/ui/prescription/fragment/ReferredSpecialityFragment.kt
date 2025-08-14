package org.intelehealth.app.ui.prescription.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.ajalt.timberkt.Timber
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentReferredSpecialityBinding
import org.intelehealth.app.ui.prescription.viewmodel.PrescriptionDetailsViewModel

/**
 * Created by Vaghela Mithun R. on 09-07-2025 - 13:45.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class ReferredSpecialityFragment : Fragment(R.layout.fragment_referred_speciality) {
    private lateinit var binding: FragmentReferredSpecialityBinding
    private val viewModel: PrescriptionDetailsViewModel by activityViewModels<PrescriptionDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReferredSpecialityBinding.bind(view)
        binding.lblReferredSpecialist.isSelected = true
        toggleReferredSpecialityInfoCardVisibility()
        observerReferredSpecialityDetails()
    }

    private fun toggleReferredSpecialityInfoCardVisibility() {
        binding.lblReferredSpecialist.setOnClickListener {
            it.isSelected = !it.isSelected
            binding.referredSpExpandableLayoutGroup.isVisible = it.isSelected
        }
    }

    private fun observerReferredSpecialityDetails() {
        viewModel.getReferredSpecialityDetails().observe(viewLifecycleOwner) {
            it ?: return@observe
            Timber.d { "Referred Speciality => $it" }
            binding.value = viewModel.formatToBulletPoints(it)
        }
    }
}
