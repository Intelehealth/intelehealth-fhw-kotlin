package org.intelehealth.app.ui.patient.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPatientDetailBinding
import org.intelehealth.app.ui.patient.viewmodel.PatientDetailViewModel
import org.intelehealth.common.extensions.applyLabelAsScreenTitle
import org.intelehealth.config.presenter.feature.viewmodel.ActiveFeatureStatusViewModel
import org.intelehealth.config.room.entity.ActiveFeatureStatus

/**
 * Created by Vaghela Mithun R. on 05-05-2025 - 16:29.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Fragment to display and manage patient details.
 *
 * This fragment observes the patient details from the [PatientDetailViewModel]
 * and updates the UI accordingly. It allows toggling the visibility of the
 * address and other sections based on active feature status.
 */
@AndroidEntryPoint
class PatientDetailFragment : Fragment(R.layout.fragment_patient_detail) {
    private lateinit var binding: FragmentPatientDetailBinding
    private val activeFeatureViewModel: ActiveFeatureStatusViewModel by viewModels()
    private val detailViewModel: PatientDetailViewModel by activityViewModels()
    private val args by navArgs<PatientDetailFragmentArgs>()

    /**
     * Called when the fragment's view is created.
     *
     * This method initializes the binding and sets up observers for patient details
     * and active feature status. It also applies the label as the screen title.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyLabelAsScreenTitle()
        binding = FragmentPatientDetailBinding.bind(view)
        observeActiveFeatureStatus()
        observePatientDetails()
    }

    /**
     * Observes the visibility of the address section based on active feature status.
     *
     * This method sets the initial state of the address section and updates its
     * visibility based on the active feature status.
     */
    private fun observePatientDetails() {
        detailViewModel.fetchPatientPersonalDetail(args.patientId)
        detailViewModel.fetchPatientAddress(args.patientId)
        detailViewModel.fetchPatientOtherDetails(args.patientId)
        detailViewModel.patientPersonalLiveData.observe(viewLifecycleOwner) {
            binding.patient = it
        }
    }

    /**
     * Observes the active feature status and updates the visibility of address and other sections.
     *
     * This method fetches the active feature status and updates the binding with
     * the visibility of address and other sections based on the active status.
     */
    private fun observeActiveFeatureStatus() {
        activeFeatureViewModel.fetchActiveFeatureStatus().observe(viewLifecycleOwner) { featureStatus ->
            featureStatus?.let { binding.activeFeatureStatus = it }
        }
    }
}
