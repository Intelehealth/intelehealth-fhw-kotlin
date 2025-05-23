package org.intelehealth.app.ui.patient.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPersonalDetailBinding
import org.intelehealth.app.ui.patient.viewmodel.PatientPersonalViewModel
import org.intelehealth.config.presenter.fields.patient.utils.PatientConfigKey

/**
 * Created by Vaghela Mithun R. on 05-05-2025 - 16:29.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class PersonalDetailFragment : Fragment(R.layout.fragment_personal_detail) {
    private lateinit var binding: FragmentPersonalDetailBinding
    private val personalViewModel by viewModels<PatientPersonalViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPersonalDetailBinding.bind(view)
        observerPersonalDetailsVisibility()
    }

    private fun observerPersonalDetailsVisibility() {
        personalViewModel.fetchPersonalRegFields()
        personalViewModel.personalSectionFieldsLiveData.observe(viewLifecycleOwner) {
            binding.personalConfig = PatientConfigKey.buildPatientPersonalInfoConfig(it)
        }
    }

    fun observePersonalDetails() {
//        personalViewModel.fetchPersonalInfo().observe(viewLifecycleOwner) { personalDetails ->
//            // Update UI with personal details
//        }
    }
}
