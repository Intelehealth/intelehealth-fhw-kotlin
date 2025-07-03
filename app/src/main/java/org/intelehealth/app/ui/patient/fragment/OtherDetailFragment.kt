package org.intelehealth.app.ui.patient.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentOtherDetailBinding
import org.intelehealth.app.ui.patient.viewmodel.PatientDetailViewModel
import org.intelehealth.config.presenter.fields.patient.utils.PatientConfigKey

/**
 * Created by Vaghela Mithun R. on 05-05-2025 - 16:29.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Fragment to display and manage other details of a patient.
 *
 * This fragment observes the other details from the [PatientDetailViewModel]
 * and updates the UI accordingly. It allows toggling the visibility of the
 * other section.
 */
@AndroidEntryPoint
class OtherDetailFragment : Fragment(R.layout.fragment_other_detail) {
    private lateinit var binding: FragmentOtherDetailBinding
    private val detailViewModel: PatientDetailViewModel by activityViewModels()

    /**
     * Called when the fragment's view is created.
     *
     * This method initializes the binding and sets up observers for other details
     * and their visibility. It also sets up a click listener to toggle the visibility
     * of the other section.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOtherDetailBinding.bind(view)
        observeOtherDetailsVisibility()
        observeOtherDetails()
        toggleOtherDetailsVisibility()
        editOtherInfo()
    }

    /**
     * Toggles the visibility of the other section when the button is clicked.
     *
     * This method sets the initial state of the button and toggles the visibility
     * of the other section when clicked.
     */
    private fun toggleOtherDetailsVisibility() {
        binding.btnExpandableOtherSection.isSelected = true
        binding.btnExpandableOtherSection.setOnClickListener {
            val visibility = binding.groupOtherSection.isVisible
            binding.groupOtherSection.isVisible = !visibility
            binding.btnExpandableOtherSection.isSelected = !visibility
        }
    }

    /**
     * Observes the visibility of other details and updates the configuration.
     *
     * This method fetches the other registration fields and updates the binding
     * with the configuration for displaying other details.
     */
    private fun observeOtherDetailsVisibility() {
        detailViewModel.fetchOtherRegFields()
        detailViewModel.otherSectionFieldsLiveData.observe(viewLifecycleOwner) {
            binding.otherConfig = PatientConfigKey.buildPatientOtherInfoConfig(it)
        }
    }

    /**
     * Observes the other details and updates the UI accordingly.
     *
     * This method observes the live data for other details and updates the binding
     * with the fetched data.
     */
    private fun observeOtherDetails() {
        detailViewModel.patientOtherLiveData.observe(viewLifecycleOwner) {
            Timber.d { "Other => $it" }
            binding.otherInfo = it
        }
    }

    private fun editOtherInfo() {
        binding.btnEditOtherInfo.setOnClickListener {
            navigateToOtherInfo()
        }
    }

    private fun navigateToOtherInfo() {
        binding.otherInfo?.let {
            val patientId = it.patientId ?: return@let
            PatientDetailFragmentDirections.actionDetailToOtherInfo(patientId, true).apply {
                findNavController().navigate(this)
            }
        }
    }
}
