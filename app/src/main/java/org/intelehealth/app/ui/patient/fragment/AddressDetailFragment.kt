package org.intelehealth.app.ui.patient.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentAddressDetailBinding
import org.intelehealth.app.ui.patient.viewmodel.PatientDetailViewModel
import org.intelehealth.config.presenter.fields.patient.utils.PatientConfigKey

/**
 * Created by Vaghela Mithun R. on 05-05-2025 - 16:29.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Fragment to display and manage address details of a patient.
 *
 * This fragment observes the address details from the [PatientDetailViewModel]
 * and updates the UI accordingly. It allows toggling the visibility of the
 * address section.
 */
@AndroidEntryPoint
class AddressDetailFragment : Fragment(R.layout.fragment_address_detail) {
    private lateinit var binding: FragmentAddressDetailBinding
    private val detailViewModel: PatientDetailViewModel by activityViewModels()

    /**
     * Called when the fragment's view is created.
     *
     * This method initializes the binding and sets up observers for address details
     * and their visibility. It also sets up a click listener to toggle the visibility
     * of the address section.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddressDetailBinding.bind(view)
        observeAddressDetailsVisibility()
        observeAddressDetails()
        toggleAddressDetailsVisibility()
        editAddressInfo()
    }

    /**
     * Toggles the visibility of the address section when the button is clicked.
     *
     * This method sets the initial state of the button and toggles the visibility
     * of the address section when clicked.
     */
    private fun toggleAddressDetailsVisibility() {
        binding.btnExpandableAddressSection.isSelected = true
        binding.btnExpandableAddressSection.setOnClickListener {
            val visibility = binding.groupAddressSection.isVisible
            binding.groupAddressSection.isVisible = !visibility
            binding.btnExpandableAddressSection.isSelected = !visibility
        }
    }

    /**
     * Observes the visibility of address details and updates the UI accordingly.
     *
     * This method fetches the address registration fields and updates the
     * configuration for displaying address information in the UI.
     */
    private fun observeAddressDetailsVisibility() {
        detailViewModel.fetchAddressRegFields()
        detailViewModel.addressSectionFieldsLiveData.observe(viewLifecycleOwner) {
            binding.addressConfig = PatientConfigKey.buildPatientAddressInfoConfig(it)
        }
    }

    /**
     * Observes the address details and updates the UI with the latest data.
     *
     * This method observes the address details from the [PatientDetailViewModel]
     * and updates the binding with the latest address information.
     */
    private fun observeAddressDetails() {
        detailViewModel.fetchPatientAddress().observe(viewLifecycleOwner) {
            binding.addressInfo = it
        }
    }

    private fun editAddressInfo() {
        binding.btnEditAddressInfo.setOnClickListener {
            navigateToAddressInfo()
        }
    }

    private fun navigateToAddressInfo() {
        binding.addressInfo?.let {
            PatientDetailFragmentDirections.actionDetailToAddressInfo(it.uuid, true).apply {
                findNavController().navigate(this)
            }
        }
    }
}
