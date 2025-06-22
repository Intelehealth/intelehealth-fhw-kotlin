package org.intelehealth.app.ui.patient.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPatientOtherInfoBinding
import org.intelehealth.app.databinding.ViewPatientInfoTabBinding
import org.intelehealth.app.ui.patient.viewmodel.PatientOtherInfoViewModel
import org.intelehealth.app.utility.IND_MOBILE_LEN
import org.intelehealth.app.utility.LanguageUtils.Companion.ENGLISH
import org.intelehealth.common.extensions.addFilter
import org.intelehealth.common.extensions.getSpinnerArrayAdapter
import org.intelehealth.common.extensions.hideDigitErrorOnTextChang
import org.intelehealth.common.extensions.hideError
import org.intelehealth.common.extensions.hideErrorOnTextChang
import org.intelehealth.common.extensions.validate
import org.intelehealth.common.extensions.validateDigit
import org.intelehealth.common.extensions.validateDropDowb
import org.intelehealth.common.inputfilter.FirstLetterUpperCaseInputFilter
import org.intelehealth.config.presenter.fields.patient.infoconfig.OtherInfoConfig
import org.intelehealth.config.presenter.fields.patient.utils.PatientConfigKey
import org.intelehealth.config.utility.PatientInfoGroup
import org.intelehealth.data.offline.entity.PatientOtherInfo
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 27-06-2024 - 13:42.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class PatientOtherInfoFragment : PatientInfoTabFragment(R.layout.fragment_patient_other_info) {
    override val viewModel: PatientOtherInfoViewModel by viewModels()
    private lateinit var binding: FragmentPatientOtherInfoBinding
    private val args by navArgs<PatientOtherInfoFragmentArgs>()
    private var otherInfo = PatientOtherInfo()

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
            otherInfo = result
        }
    }

    override fun getPatientInfoTabBinding(): ViewPatientInfoTabBinding = binding.patientTab

    private fun setupSocialCategory() {
        val adapter = requireContext().getSpinnerArrayAdapter(R.array.caste)
        binding.autoCompleteSocialCategory.setAdapter(adapter)

        otherInfo.cast?.let {
            languageUtils.getLocalValueFromArray(requireContext(), it, R.array.caste).apply {
                binding.autoCompleteSocialCategory.setText(this, false)
            }
        }

        binding.autoCompleteSocialCategory.setOnItemClickListener { _, _, i, _ ->
            binding.textInputLaySocialCategory.hideError()
            languageUtils.getSpecificLocalResource(requireContext(), ENGLISH).apply {
                otherInfo.cast = this.getStringArray(R.array.caste)[i]
            }
        }
    }

    private fun fetchPersonalInfoConfig() {
        viewModel.fetchOtherRegFields()
        viewModel.otherSectionFieldsLiveData.observe(viewLifecycleOwner) {
            binding.otherInfoConfig = PatientConfigKey.buildPatientOtherInfoConfig(it)
            setupSocialCategory()
            setupEducations()
            setupHealthFacility()
            setupEconomicCategory()
            applyFilter()
            setInputTextChangListener()
            setClickListener()
        }
    }

    private fun setupEconomicCategory() {
        val adapter = requireContext().getSpinnerArrayAdapter(R.array.economic)
        binding.autoCompleteEconomicCategory.setAdapter(adapter)

        otherInfo.economicStatus?.let {
            languageUtils.getLocalValueFromArray(requireContext(), it, R.array.economic).apply {
                binding.autoCompleteEconomicCategory.setText(this, false)
            }
        }

        binding.autoCompleteEconomicCategory.setOnItemClickListener { _, _, i, _ ->
            binding.textInputLayEducation.hideError()
            languageUtils.getSpecificLocalResource(requireContext(), ENGLISH).apply {
                otherInfo.economicStatus = this.getStringArray(R.array.economic)[i]
            }
        }
    }

    private fun setClickListener() {
        binding.btnPatientOtherBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnPatientOtherNext.setOnClickListener {
            validateForm { savePatientOtherInfo() }
        }
    }

    private fun savePatientOtherInfo() {
        otherInfo.apply {
            nationalId = binding.textInputNationalId.text?.toString()
            occupation = binding.textInputOccupation.text?.toString()
            tmhCaseNumber = binding.textInputTmhCaseNumber.text?.toString()
            discipline = binding.textInputDiscipline.text?.toString()
            department = binding.textInputDepartment.text?.toString()
            relativePhoneNumber = binding.textInputRelativePhoneNumber.text?.toString()

            inn = binding.textInputInn.text?.toString()
            codeOfHealthFacility = binding.textInputCodeOfHealthyFacility.text?.toString()
            codeOfDepartment = binding.textInputCodeOfDepartment.text?.toString()
            department = binding.textInputDepartment.text?.toString()

            createOrUpdateOtherInfo(this)
        }
    }

    private fun createOrUpdateOtherInfo(otherInfo: PatientOtherInfo) {
        if (args.editMode) updateOtherInfo(otherInfo)
        else createOtherInfo(otherInfo)
    }

    private fun createOtherInfo(otherInfo: PatientOtherInfo) {
        viewModel.createPatientOtherInfo(otherInfo).observe(viewLifecycleOwner) {
            it ?: return@observe
            viewModel.allowNullDataResponse(it) { moveToNextScreen() }
        }
    }

    private fun updateOtherInfo(otherInfo: PatientOtherInfo) {
        viewModel.updatePatientOtherInfo(args.patientId, otherInfo).observe(viewLifecycleOwner) {
            it ?: return@observe
            viewModel.allowNullDataResponse(it) { moveToNextScreen() }
        }
    }

    private fun moveToNextScreen() {
        if (args.editMode) {
            findNavController().popBackStack()
        } else {
            val direction = PatientOtherInfoFragmentDirections.actionOtherToDetail(args.patientId)
            findNavController().navigate(direction)
        }
    }

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
    private fun applyFilter() {
//        binding.textInputNationalId.addFilter(FirstLetterUpperCaseInputFilter())
        binding.textInputOccupation.addFilter(FirstLetterUpperCaseInputFilter())
        binding.textInputDepartment.addFilter(FirstLetterUpperCaseInputFilter())
    }

    private fun setInputTextChangListener() {
        binding.textInputLayNationalId.hideErrorOnTextChang(binding.textInputNationalId)
        binding.textInputLayOccupation.hideErrorOnTextChang(binding.textInputOccupation)

        binding.textInputLayTmhCaseNumber.hideErrorOnTextChang(binding.textInputTmhCaseNumber)
        binding.textInputLayDiscipline.hideErrorOnTextChang(binding.textInputDiscipline)
        binding.textInputLayDepartment.hideErrorOnTextChang(binding.textInputDepartment)
        binding.textInputLayRelativePhoneNumber.hideDigitErrorOnTextChang(
            binding.textInputRelativePhoneNumber, 10
        )
        binding.textInputLayInn.hideErrorOnTextChang(binding.textInputInn)
        binding.textInputLayCodeOfHealthyFacility.hideErrorOnTextChang(binding.textInputCodeOfHealthyFacility)
        binding.textInputLayCodeOfDepartment.hideErrorOnTextChang(binding.textInputCodeOfDepartment)
    }

    private fun setupEducations() {
        val adapter = requireContext().getSpinnerArrayAdapter(R.array.education)
        binding.autoCompleteEducation.setAdapter(adapter)

        otherInfo.education?.let {
            languageUtils.getLocalValueFromArray(requireContext(), it, R.array.education).apply {
                binding.autoCompleteEducation.setText(this, false)
            }
        }

        binding.autoCompleteEducation.setOnItemClickListener { _, _, i, _ ->
            binding.textInputLayEducation.hideError()
            languageUtils.getSpecificLocalResource(requireContext(), ENGLISH).apply {
                otherInfo.education = this.getStringArray(R.array.education)[i]
            }
        }
    }

    private fun setupHealthFacility() {
        val adapter = requireContext().getSpinnerArrayAdapter(R.array.health_facility_name)
        binding.autoCompleteHealthFacilityName.setAdapter(adapter)

        otherInfo.healthFacilityName?.let {
            languageUtils.getLocalValueFromArray(requireContext(), it, R.array.health_facility_name).apply {
                binding.autoCompleteHealthFacilityName.setText(this, false)
            }
        }

        binding.autoCompleteHealthFacilityName.setOnItemClickListener { _, _, i, _ ->
            binding.textInputLayHealthFacilityName.hideError()
            languageUtils.getSpecificLocalResource(requireContext(), ENGLISH).apply {
                otherInfo.healthFacilityName = this.getStringArray(R.array.health_facility_name)[i]
            }
        }
    }

    private fun validNationalId(it: OtherInfoConfig, error: Int): Boolean {
        return if (it.nationalId?.isEnabled == true && it.nationalId?.isMandatory == true) {
            binding.textInputLayNationalId.validate(binding.textInputNationalId, error)
        } else true
    }

    private fun validOccupation(it: OtherInfoConfig, error: Int): Boolean {
        return if (it.occuption?.isEnabled == true && it.occuption?.isMandatory == true) {
            binding.textInputLayOccupation.validate(binding.textInputOccupation, error)
        } else true
    }

    private fun validINN(it: OtherInfoConfig, error: Int): Boolean {
        return if (it.inn?.isEnabled == true && it.inn?.isMandatory == true) {
            binding.textInputLayInn.validate(binding.textInputInn, error)
        } else true
    }

    private fun validCodeOfHealthyFacility(it: OtherInfoConfig, error: Int): Boolean {
        return if (it.codeOfHealthyFacility?.isEnabled == true && it.codeOfHealthyFacility?.isMandatory == true) {
            binding.textInputLayCodeOfHealthyFacility.validate(binding.textInputCodeOfHealthyFacility, error)
        } else true
    }

    private fun validCodeOfDepartment(it: OtherInfoConfig, error: Int): Boolean {
        return if (it.codeOfDepartment?.isEnabled == true && it.codeOfDepartment?.isMandatory == true) {
            binding.textInputLayCodeOfDepartment.validate(binding.textInputCodeOfDepartment, error)
        } else true
    }

    private fun validDepartment(it: OtherInfoConfig, error: Int): Boolean {
        return if (it.department?.isEnabled == true && it.department?.isMandatory == true) {
            binding.textInputLayDepartment.validate(binding.textInputDepartment, error)
        } else true
    }

    private fun validHealthFacilityName(it: OtherInfoConfig, error: Int): Boolean {
        return if (it.healthFacilityName?.isEnabled == true && it.healthFacilityName?.isMandatory == true) {
            binding.textInputLayHealthFacilityName.validateDropDowb(
                binding.autoCompleteHealthFacilityName, error
            )
        } else true
    }

    private fun validRelativePhoneNumber(it: OtherInfoConfig, error: Int): Boolean {
        return if (it.relativePhoneNumber?.isEnabled == true && it.relativePhoneNumber?.isMandatory == true) {
            binding.textInputLayRelativePhoneNumber.validate(
                binding.textInputRelativePhoneNumber, error
            ).and(
                binding.textInputLayRelativePhoneNumber.validateDigit(
                    binding.textInputRelativePhoneNumber, ResourceR.string.error_invalid_mobile_number, IND_MOBILE_LEN
                )
            )
        } else true
    }

    private fun validTmhCaseNumber(it: OtherInfoConfig, error: Int): Boolean {
        return if (it.tmhCaseNumber?.isEnabled == true && it.tmhCaseNumber?.isMandatory == true) {
            binding.textInputLayTmhCaseNumber.validate(binding.textInputTmhCaseNumber, error)
        } else true
    }

    private fun validDiscipline(it: OtherInfoConfig, error: Int): Boolean {
        return if (it.discipline?.isEnabled == true && it.discipline?.isMandatory == true) {
            binding.textInputLayDiscipline.validate(binding.textInputDiscipline, error)
        } else true
    }

    private fun validEducation(it: OtherInfoConfig, error: Int): Boolean {
        return if (it.education?.isEnabled == true && it.education?.isMandatory == true) {
            binding.textInputLayEducation.validateDropDowb(
                binding.autoCompleteEducation, error
            )
        } else true
    }

    private fun validEconomicCategory(it: OtherInfoConfig, error: Int): Boolean {
        return if (it.economicCategory?.isEnabled == true && it.economicCategory?.isMandatory == true) {
            binding.textInputLayEconomicCategory.validateDropDowb(
                binding.autoCompleteEconomicCategory, error
            )
        } else true
    }

    private fun validSocialCategory(it: OtherInfoConfig, error: Int): Boolean {
        return if (it.socialCategory?.isEnabled == true && it.socialCategory?.isMandatory == true) {
            binding.textInputLaySocialCategory.validateDropDowb(
                binding.autoCompleteSocialCategory, error
            )
        } else true
    }

    private fun validateForm(block: () -> Unit) {
        Timber.d { "Final patient =>${Gson().toJson(binding.otherInfo)}" }
        val error = ResourceR.string.error_field_required
        binding.otherInfoConfig?.let {
            val bNationalId = validNationalId(it, error)
            val bOccupation = validOccupation(it, error)
            val bInn = validINN(it, error)
            val bCodeOfHealthyFacility = validCodeOfHealthyFacility(it, error)
            val bCodeOfDepartment = validCodeOfDepartment(it, error)
            val bDepartment = validDepartment(it, error)
            val bHealthFacilityName = validHealthFacilityName(it, error)
            val bSocialCategory = validSocialCategory(it, error)
            val bEducation = validEducation(it, error)
            val bEconomic = validEconomicCategory(it, error)
            val tmhCaseNumber = validTmhCaseNumber(it, error)
            val discipline = validDiscipline(it, error)
            val relativePhoneNumber = validRelativePhoneNumber(it, error)


            if (bOccupation.and(bSocialCategory).and(bEducation).and(bEconomic).and(bNationalId).and(bOccupation)
                    .and(tmhCaseNumber).and(discipline).and(relativePhoneNumber).and(bInn)
                    .and(bCodeOfHealthyFacility).and(bHealthFacilityName).and(bCodeOfDepartment).and(bDepartment)
            ) block.invoke()
        }
    }

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

}
