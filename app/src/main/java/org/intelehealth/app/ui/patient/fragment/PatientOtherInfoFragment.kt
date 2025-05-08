package org.intelehealth.app.ui.patient.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPatientOtherInfoBinding
import org.intelehealth.app.databinding.ViewPatientInfoTabBinding
import org.intelehealth.app.ui.patient.viewmodel.PatientOtherInfoViewModel
import org.intelehealth.config.presenter.fields.patient.utils.PatientConfigKey
import org.intelehealth.config.utility.PatientInfoGroup

/**
 * Created by Vaghela Mithun R. on 27-06-2024 - 13:42.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class PatientOtherInfoFragment : PatientInfoTabFragment(R.layout.fragment_patient_other_info) {
    override val viewModel: PatientOtherInfoViewModel by viewModels()
    private lateinit var binding: FragmentPatientOtherInfoBinding
    private val args by navArgs<PatientOtherInfoFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentPatientOtherInfoBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        binding.isEditMode = args.editMode
        fetchPersonalInfoConfig()
        changeIconStatus(PatientInfoGroup.OTHER)
        observeOtherInfoData()
    }

    private fun observeOtherInfoData() {
        viewModel.fetchPatientOtherInfo(args.patientId).observe(viewLifecycleOwner) { result ->
            result ?: return@observe
            binding.otherInfo = result
        }
    }

    override fun getPatientInfoTabBinding(): ViewPatientInfoTabBinding = binding.patientTab

    //
//    private fun setupSocialCategory() {
//        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.caste)
//        binding.autoCompleteSocialCategory.setAdapter(adapter)
//        if (patient.caste != null && patient.caste.isNotEmpty()) {
//            binding.autoCompleteSocialCategory.setText(patient.caste, false)
//        }
//        binding.autoCompleteSocialCategory.setOnItemClickListener { _, _, i, _ ->
//            binding.textInputLaySocialCategory.hideError()
//            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
//                patient.caste = this.getStringArray(R.array.caste)[i]
//            }
//        }
//    }
//
//
//    override fun onPatientDataLoaded(patient: PatientDTO) {
//        super.onPatientDataLoaded(patient)
//        Timber.d { "onPatientDataLoaded" }
//        Timber.d { Gson().toJson(patient) }
//        binding.patient = patient
//        binding.isEditMode = patientViewModel.isEditMode
//        fetchPersonalInfoConfig()
//    }
//
    private fun fetchPersonalInfoConfig() {
        viewModel.fetchOtherRegFields()
        viewModel.otherSectionFieldsLiveData.observe(viewLifecycleOwner) {
            binding.otherInfoConfig = PatientConfigKey.buildPatientOtherInfoConfig(it)
//            setupSocialCategory()
//            setupEducations()
//            setupHealthFacility()
//            setupEconomicCategory()
//            applyFilter()
//            setInputTextChangListener()
            setClickListener()
        }
    }

    //
//    private fun setupEconomicCategory() {
//        val marathiArray = resources.getStringArray(R.array.economic) // Get Marathi values
//        val englishArray = LanguageUtils.getSpecificLocalResource(requireContext(), "en").getStringArray(R.array.economic)
//
//        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, marathiArray)
//        binding.autoCompleteEconomicCategory.setAdapter(adapter)
//
//        if (!patient.economic.isNullOrEmpty()) {
//            val index = englishArray.indexOf(patient.economic)
//            if (index != -1) {
//                binding.autoCompleteEconomicCategory.setText(marathiArray[index], false)
//            }
//        }
//
//        binding.autoCompleteEconomicCategory.setOnItemClickListener { _, _, i, _ ->
//            binding.textInputLayEducation.hideError()
//            patient.economic = englishArray[i]
//            binding.autoCompleteEconomicCategory.setText(marathiArray[i], false)
//        }
//    }
//
//
    private fun setClickListener() {
        binding.frag2BtnBack.setOnClickListener { findNavController().popBackStack() }
        binding.frag2BtnNext.setOnClickListener {
            val direction = PatientOtherInfoFragmentDirections.actionOtherToDetail(args.patientId)
            findNavController().navigate(direction)
//            validateForm { savePatient() }
        }
    }
//
//    private fun savePatient() {
//        patient.apply {
//            nationalID = binding.textInputNationalId.text?.toString()
//            occupation = binding.textInputOccupation.text?.toString()
//            tmhCaseNumber = binding.textInputTmhCaseNumber.text?.toString()
//            requestId = binding.textInputRequestId.text?.toString()
//            discipline = binding.textInputDiscipline.text?.toString()
//            department = binding.textInputDepartment.text?.toString()
//            relativePhoneNumber = binding.textInputRelativePhoneNumber.text?.toString()
//
//            inn = binding.textInputInn.text?.toString()
//            codeOfHealthFacility = binding.textInputCodeOfHealthyFacility.text?.toString()
//            codeOfDepartment = binding.textInputCodeOfDepartment.text?.toString()
//            department = binding.textInputDepartment.text?.toString()
//
//            patientViewModel.updatedPatient(this)
//            patientViewModel.savePatient().observe(viewLifecycleOwner) {
//                it ?: return@observe
//                patientViewModel.handleResponse(it) { result -> if (result) navigateToDetails() }
//            }
//        }
//    }
//
//    private fun navigateToDetails() {
//        //For roster- if roster module enabled then redirect user to Roster screen otherwise on patient details screen
//        //for now adding roster config hardcoded for testing
//        if (patientViewModel.isEditMode) {
//            PatientOtherInfoFragmentDirections.navigationOtherToDetails(
//                patient.uuid, "searchPatient", "false"
//            ).also {
//                findNavController().navigate(it)
//                requireActivity().finish()
//            }
//        } else {
//            val rosterConfig = patientViewModel.activeStatusRosterSection
//            Log.d("TAG", "navigateToDetails: rosterConfig : " + rosterConfig)
//            //showMoveToRosterDialog()
//            if (rosterConfig) {
//                showMoveToRosterDialog()
//            } else {
//                PatientOtherInfoFragmentDirections.navigationOtherToDetails(
//                    patient.uuid, "searchPatient", "false"
//                ).also {
//                    findNavController().navigate(it)
//                    requireActivity().finish()
//                }
//           }
//        }
//
//    }
//
//    private fun applyFilter() {
////        binding.textInputNationalId.addFilter(FirstLetterUpperCaseInputFilter())
//        binding.textInputOccupation.addFilter(FirstLetterUpperCaseInputFilter())
//        binding.textInputDepartment.addFilter(FirstLetterUpperCaseInputFilter())
//    }
//
//    private fun setInputTextChangListener() {
//        binding.textInputLayNationalId.hideErrorOnTextChang(binding.textInputNationalId)
//        binding.textInputLayOccupation.hideErrorOnTextChang(binding.textInputOccupation)
//
//        binding.textInputLayTmhCaseNumber.hideErrorOnTextChang(binding.textInputTmhCaseNumber)
//        binding.textInputLayRequestId.hideErrorOnTextChang(binding.textInputRequestId)
//        binding.textInputLayDiscipline.hideErrorOnTextChang(binding.textInputDiscipline)
//        binding.textInputLayDepartment.hideErrorOnTextChang(binding.textInputDepartment)
//        binding.textInputLayRelativePhoneNumber.hideDigitErrorOnTextChang(
//            binding.textInputRelativePhoneNumber, 10
//        )
//        binding.textInputLayInn.hideErrorOnTextChang(binding.textInputInn)
//        binding.textInputLayCodeOfHealthyFacility.hideErrorOnTextChang(binding.textInputCodeOfHealthyFacility)
//        binding.textInputLayCodeOfDepartment.hideErrorOnTextChang(binding.textInputCodeOfDepartment)
//    }
//
//    private fun setupEducations() {
//        val marathiArray = resources.getStringArray(R.array.education)
//        val englishArray = LanguageUtils.getSpecificLocalResource(requireContext(), "en").getStringArray(R.array.education)
//
//        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.education)
//        binding.autoCompleteEducation.setAdapter(adapter)
//       /* if (patient.education != null && patient.education.isNotEmpty()) {
//            binding.autoCompleteEducation.setText(patient.education, false)
//        }*/
//        if (!patient.education.isNullOrEmpty()) {
//            val index = englishArray.indexOf(patient.education)
//            if (index != -1) {
//                binding.autoCompleteEducation.setText(marathiArray[index], false)
//            }
//        }
//        binding.autoCompleteEducation.setOnItemClickListener { _, _, i, _ ->
//            binding.textInputLayEducation.hideError()
//            /*LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
//                patient.education = this.getStringArray(R.array.education)[i]
//            }*/
//            patient.education = englishArray[i]
//            binding.autoCompleteEducation.setText(marathiArray[i], false)
//        }
//    }
//
//    private fun setupHealthFacility() {
//        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.health_facility_name)
//        binding.autoCompleteHealthFacilityName.setAdapter(adapter)
//        if (patient.healthFacilityName != null && patient.healthFacilityName.isNotEmpty()) {
//            binding.autoCompleteHealthFacilityName.setText(patient.healthFacilityName, false)
//        }
//        binding.autoCompleteHealthFacilityName.setOnItemClickListener { _, _, i, _ ->
//            binding.textInputLayHealthFacilityName.hideError()
//            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
//                patient.healthFacilityName = this.getStringArray(R.array.health_facility_name)[i]
//            }
//        }
//    }
//
//    private fun validateForm(block: () -> Unit) {
//        Timber.d { "Final patient =>${Gson().toJson(patient)}" }
//        val error = R.string.this_field_is_mandatory
//        binding.otherInfoConfig?.let {
//            val bNationalId = if (it.nationalId?.isEnabled == true && it.nationalId?.isMandatory == true) {
//                binding.textInputLayNationalId.validate(binding.textInputNationalId, error)
//            } else true
//
//            val bOccuptions = if (it.occuptions?.isEnabled == true && it.occuptions?.isMandatory == true) {
//                binding.textInputLayOccupation.validate(binding.textInputOccupation, error)
//            } else true
//
//            val bInn = if (it.inn?.isEnabled == true && it.inn?.isMandatory == true) {
//                binding.textInputLayInn.validate(binding.textInputInn, error)
//            } else true
//
//            val bCodeOfHealthyFacility =
//                if (it.codeOfHealthyFacility?.isEnabled == true && it.codeOfHealthyFacility?.isMandatory == true) {
//                    binding.textInputLayCodeOfHealthyFacility.validate(binding.textInputCodeOfHealthyFacility, error)
//                } else true
//
//            val bCodeOfDepartment = if (it.codeOfDepartment?.isEnabled == true && it.codeOfDepartment?.isMandatory == true) {
//                binding.textInputLayCodeOfDepartment.validate(binding.textInputCodeOfDepartment, error)
//            } else true
//
//            val bDepartment = if (it.department?.isEnabled == true && it.department?.isMandatory == true) {
//                binding.textInputLayDepartment.validate(binding.textInputDepartment, error)
//            } else true
//
//            val bHealthFacilityName = if (it.healthFacilityName?.isEnabled == true && it.healthFacilityName?.isMandatory == true) {
//                binding.textInputLayHealthFacilityName.validateDropDowb(
//                    binding.autoCompleteHealthFacilityName, error
//                )
//            } else true
//
//
//            val bSocialCategory = if (it.socialCategory?.isEnabled == true && it.socialCategory?.isMandatory == true) {
//                binding.textInputLaySocialCategory.validateDropDowb(
//                    binding.autoCompleteSocialCategory, error
//                )
//            } else true
//
//            val bEducation = if (it.education?.isEnabled == true && it.education?.isMandatory == true) {
//                binding.textInputLayEducation.validateDropDowb(
//                    binding.autoCompleteEducation, error
//                )
//            } else true
//
//            val bEconomic = if (it.economicCategory?.isEnabled == true && it.economicCategory?.isMandatory == true) {
//                binding.textInputLayEconomicCategory.validateDropDowb(
//                    binding.autoCompleteEconomicCategory, error
//                )
//            } else true
//
//            val tmhCaseNumber = if (it.tmhCaseNumber?.isEnabled == true && it.tmhCaseNumber?.isMandatory == true) {
//                binding.textInputLayTmhCaseNumber.validate(
//                    binding.textInputTmhCaseNumber, error
//                )
//            } else true
//
//            val requestId = if (it.requestId?.isEnabled == true && it.requestId?.isMandatory == true) {
//                binding.textInputLayRequestId.validate(
//                    binding.textInputRequestId, error
//                )
//            } else true
//
//            val discipline = if (it.discipline?.isEnabled == true && it.discipline?.isMandatory == true) {
//                binding.textInputLayDiscipline.validate(
//                    binding.textInputDiscipline, error
//                )
//            } else true
//
//            val relativePhoneNumber = if (it.relativePhoneNumber?.isEnabled == true && it.relativePhoneNumber?.isMandatory == true) {
//                binding.textInputLayRelativePhoneNumber.validate(
//                    binding.textInputRelativePhoneNumber, error
//                ).and(
//                    binding.textInputLayRelativePhoneNumber.validateDigit(
//                        binding.textInputRelativePhoneNumber, R.string.enter_10_digits, 10
//                    )
//                )
//
//
//            } else true
//
//
//            if (bOccuptions.and(bSocialCategory).and(bEducation).and(bEconomic).and(bNationalId).and(bOccuptions)
//                    .and(tmhCaseNumber).and(requestId).and(discipline).and(relativePhoneNumber).and(bInn)
//                    .and(bCodeOfHealthyFacility).and(bHealthFacilityName).and(bCodeOfDepartment).and(bDepartment)
//            ) block.invoke()
//        }
//    }
//
//    private fun showMoveToRosterDialog() {
//        val dialogUtils = DialogUtils()
//        dialogUtils.showCommonDialog(
//            requireActivity(),
//            R.drawable.ui2_complete_icon,
//            getString(R.string.complete_patient_details),
//            getString(R.string.continue_to_enter_roster),
//            false,
//            getString(R.string.confirm),
//            getString(R.string.cancel)
//        ) { action ->
//            if (action == CustomDialogListener.POSITIVE_CLICK) {
//                startRosterQuestionnaire(
//                    requireActivity(),
//                    patient.uuid,
//                    RosterQuestionnaireStage.GENERAL_ROSTER,
//                    patientViewModel.getPregnancyVisibility(),
//                    false
//                )
//                requireActivity().finish()
//            } else if (action == CustomDialogListener.NEGATIVE_CLICK) {
//
//            }
//        }
//    }
//
}
