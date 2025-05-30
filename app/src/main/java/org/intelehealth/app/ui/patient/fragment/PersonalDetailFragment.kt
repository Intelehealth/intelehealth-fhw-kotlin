package org.intelehealth.app.ui.patient.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPersonalDetailBinding
import org.intelehealth.app.ui.patient.viewmodel.PatientDetailViewModel
import org.intelehealth.config.presenter.fields.patient.utils.PatientConfigKey

/**
 * Created by Vaghela Mithun R. on 05-05-2025 - 16:29.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Fragment to display and manage personal details of a patient.
 *
 * This fragment observes the personal details from the [PatientDetailViewModel]
 * and updates the UI accordingly. It allows toggling the visibility of the
 * personal section.
 */
@AndroidEntryPoint
class PersonalDetailFragment : Fragment(R.layout.fragment_personal_detail) {
    private lateinit var binding: FragmentPersonalDetailBinding
    private val detailViewModel: PatientDetailViewModel by activityViewModels()

    /**
     * Called when the fragment's view is created.
     *
     * This method initializes the binding and sets up observers for personal details
     * and their visibility. It also sets up a click listener to toggle the visibility
     * of the personal section.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPersonalDetailBinding.bind(view)
        observerPersonalDetailsVisibility()
        observePersonalDetails()
        togglePersonalDetailsVisibility()
    }

    /**
     * Toggles the visibility of the personal section when the button is clicked.
     *
     * This method sets the initial state of the button and toggles the visibility
     * of the personal section when clicked.
     */
    private fun togglePersonalDetailsVisibility() {
        binding.btnExpandablePersonalSection.isSelected = true
        binding.btnExpandablePersonalSection.setOnClickListener {
            val visibility = binding.groupPersonalSection.isVisible
            binding.groupPersonalSection.isVisible = !visibility
            binding.btnExpandablePersonalSection.isSelected = !visibility
        }
    }

    /**
     * Observes the visibility of personal details and updates the UI accordingly.
     *
     * This method fetches the personal registration fields and updates the
     * binding with the configuration for personal information.
     */
    private fun observerPersonalDetailsVisibility() {
        detailViewModel.fetchPersonalRegFields()
        detailViewModel.personalSectionFieldsLiveData.observe(viewLifecycleOwner) {
            binding.personalConfig = PatientConfigKey.buildPatientPersonalInfoConfig(it)
        }
    }

    /**
     * Observes the personal details and updates the UI with the latest data.
     *
     * This method observes the live data for personal and other patient information
     * and updates the binding accordingly.
     */
    private fun observePersonalDetails() {
        detailViewModel.patientPersonalLiveData.observe(viewLifecycleOwner) {
            binding.personalInfo = it
        }

        detailViewModel.patientOtherLiveData.observe(viewLifecycleOwner) {
            binding.otherInfo = it
        }
    }
}
