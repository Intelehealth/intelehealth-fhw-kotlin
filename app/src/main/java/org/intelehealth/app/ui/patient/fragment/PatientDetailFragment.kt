package org.intelehealth.app.ui.patient.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPatientDetailBinding
import org.intelehealth.common.extensions.applyLabelAsScreenTitle
import org.intelehealth.config.presenter.feature.viewmodel.ActiveFeatureStatusViewModel
import org.intelehealth.config.room.entity.ActiveFeatureStatus

/**
 * Created by Vaghela Mithun R. on 05-05-2025 - 16:29.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class PatientDetailFragment : Fragment(R.layout.fragment_patient_detail) {
    private lateinit var binding: FragmentPatientDetailBinding
    private val activeFeatureViewModel: ActiveFeatureStatusViewModel by viewModels()
    private val args by navArgs<PatientDetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyLabelAsScreenTitle()
        binding = FragmentPatientDetailBinding.bind(view)
        observeActiveFeatureStatus()
    }

    private fun observeActiveFeatureStatus() {
        activeFeatureViewModel.fetchActiveFeatureStatus().observe(viewLifecycleOwner) { featureStatus ->
            featureStatus?.let { updatePatientDetailVisibility(it) }
        }
    }

    private fun updatePatientDetailVisibility(it: ActiveFeatureStatus) {
        binding.addressActiveStatus = it.activeStatusPatientAddress
        binding.otherActiveStatus = it.activeStatusPatientOther
    }
}
