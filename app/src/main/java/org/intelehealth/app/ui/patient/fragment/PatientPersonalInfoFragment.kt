package org.intelehealth.app.ui.patient.fragment

import android.net.Uri
import android.os.Bundle
import android.text.InputFilter.LengthFilter
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.ajalt.timberkt.Timber
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.BuildConfig
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPatientPersonalInfoBinding
import org.intelehealth.app.databinding.ViewPatientInfoTabBinding
import org.intelehealth.app.ui.patient.viewmodel.PatientPersonalViewModel
import org.intelehealth.app.utility.FlavorKeys
import org.intelehealth.app.utility.IND_MOBILE_LEN
import org.intelehealth.app.utility.MAX_NAME_LENGTH
import org.intelehealth.common.dialog.AgePickerDialog
import org.intelehealth.common.dialog.CalendarDialog
import org.intelehealth.common.extensions.getSpinnerArrayAdapter
import org.intelehealth.common.extensions.hideDigitErrorOnTextChang
import org.intelehealth.common.extensions.hideError
import org.intelehealth.common.extensions.hideErrorOnTextChang
import org.intelehealth.common.extensions.toPeriod
import org.intelehealth.common.extensions.validate
import org.intelehealth.common.extensions.validateDigit
import org.intelehealth.common.extensions.validateDropDowb
import org.intelehealth.common.inputfilter.AlphabetsInputFilter
import org.intelehealth.common.inputfilter.FirstLetterUpperCaseInputFilter
import org.intelehealth.common.model.AgePeriod
import org.intelehealth.common.utility.AgeUtils
import org.intelehealth.common.utility.ArrayAdapterUtils
import org.intelehealth.common.utility.DateTimeUtils
import org.intelehealth.config.presenter.feature.viewmodel.ActiveFeatureStatusViewModel
import org.intelehealth.config.presenter.fields.patient.infoconfig.PersonalInfoConfig
import org.intelehealth.config.presenter.fields.patient.utils.PatientConfigKey
import org.intelehealth.config.room.entity.ActiveFeatureStatus
import org.intelehealth.config.utility.PatientInfoGroup
import org.intelehealth.data.offline.entity.Patient
import org.intelehealth.data.offline.entity.PatientOtherInfo
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import java.util.UUID
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 27-06-2024 - 13:42.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

@AndroidEntryPoint
class PatientPersonalInfoFragment : PatientInfoTabFragment(R.layout.fragment_patient_personal_info) {
    override val viewModel: PatientPersonalViewModel by viewModels()
    private lateinit var binding: FragmentPatientPersonalInfoBinding
    private val args by navArgs<PatientPersonalInfoFragmentArgs>()
    private var selectedDate = Calendar.getInstance().timeInMillis
    private val reportDate by lazy {
        DateTimeUtils.getCurrentDateInUTC(DateTimeUtils.LAST_SYNC_DB_FORMAT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentPatientPersonalInfoBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        changeIconStatus(PatientInfoGroup.PERSONAL)
        observePatientPersonalInfo()
    }

    override fun getPatientInfoTabBinding(): ViewPatientInfoTabBinding = binding.patientTab

    override fun onProfilePictureSelected(uri: Uri) {
        binding.ivPatientDb.tag = uri
        Glide.with(requireContext()).load(uri.toString()).diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(ResourceR.drawable.img_avatar).placeholder(ResourceR.drawable.img_avatar).into(binding.ivPatientDb)
    }

    private fun observePatientPersonalInfo() {
        val patientId = args.patientId ?: binding.patient?.uuid
        patientId?.let { fetchPersonalInfo(it) } ?: run {
            binding.patient = Patient().apply { uuid = UUID.randomUUID().toString() }
            fetchPersonalInfoConfig()
        }
    }

    private fun fetchPersonalInfo(patientId: String) {
        viewModel.fetchPersonalInfo(patientId).observe(viewLifecycleOwner) {
            it ?: return@observe
            binding.patient = it
            fetchPersonalInfoConfig()
            fetchPersonalOtherData(it.uuid)
        }
    }

    private fun fetchPersonalOtherData(patientId: String) {
        viewModel.fetchPatientPersonalOtherInfo(patientId).observe(viewLifecycleOwner) {
            it ?: return@observe
            binding.otherInfo = it
        }
    }

    private fun openAgePickerDialog() {
        val agePeriod = selectedDate.toPeriod()
        AgePickerDialog.Builder().selectedYear(agePeriod.years).selectedMonth(agePeriod.months)
            .selectedDay(agePeriod.days).agePickListener(agePickerLister).build()
            .show(childFragmentManager, AgePickerDialog.TAG)
    }

    private val agePickerLister = object : AgePickerDialog.OnAgePickListener {
        override fun onAgePick(year: Int, month: Int, day: Int) {
            bindAgeAndDobValue(year, month, day)
        }
    }

    private fun setupAge() {
        binding.textInputETAge.setOnClickListener { openAgePickerDialog() }
    }

    private fun bindAgeAndDobValue(year: Int, month: Int, days: Int) {
        val agePeriod = AgePeriod(year, month, days)
        bindAgeValue(agePeriod)

        Calendar.getInstance().apply {
            add(Calendar.YEAR, -year)
            add(Calendar.MONTH, -month)
            add(Calendar.DAY_OF_MONTH, -days)
        }.also { bindDobValue(it) }
    }

    //
    private fun updateGuardianVisibility(year: Int, month: Int, days: Int) {
        val visibility = AgeUtils.isGuardianRequired(year, month, days)
        Timber.d { "Year[$year]/Month[$month]/Day[$days] => visibility[$visibility]" }
        binding.personalConfig?.let {
            Timber.d { "personalConfig => visibility[$visibility]" }
            it.guardianName?.isEnabled = it.guardianName?.isEnabled?.let { enabled ->
                if (visibility) enabled else false
            } ?: visibility

            it.guardianType?.isEnabled = it.guardianType?.isEnabled?.let { enabled ->
                if (visibility) enabled else false
            } ?: visibility

            binding.llGuardianName.isVisible = it.guardianName?.isEnabled ?: visibility
            binding.llGuardianType.isVisible = it.guardianType?.isEnabled ?: visibility
            binding.personalConfig = it
        }
    }

    //
    private fun bindDobValue(calendar: Calendar) {
        selectedDate = calendar.timeInMillis
        val sdf = DateTimeUtils.getSimpleDateFormat(
            DateTimeUtils.MMM_DD_YYYY_FORMAT, TimeZone.getDefault()
        )
        val formattedDate = sdf.format(calendar.time)
        binding.textInputETDob.setText(formattedDate)
        binding.textInputLayDob.hideError()
        updateDob()
    }

    private fun fetchPersonalInfoConfig() {
        viewModel.fetchPersonalRegFields()
        viewModel.personalSectionFieldsLiveData.observe(viewLifecycleOwner) {
            binding.personalConfig = PatientConfigKey.buildPatientPersonalInfoConfig(it)
            setupGuardianType()
            setupEmContactType()
            setupDOB()
            setupAge()
            applyFilter()
            setGender()
            setClickListener()
            setInputTextChangListener()
        }
    }

    private fun setClickListener() {
        binding.ivPatientDb.setOnClickListener { requestPermission() }
        binding.btnPatientPersonalNext.setOnClickListener {
            validateForm { savePatient() }
        }
    }

    private fun navigateToAddress(patientId: String) {
        PatientPersonalInfoFragmentDirections.actionPersonalToAddress(
            patientId, args.patientId.isNullOrEmpty().not()
        ).apply {
            findNavController().navigate(this)
        }
    }

    private fun navigateToOther(patientId: String) {
        PatientPersonalInfoFragmentDirections.actionPersonalToOther(
            patientId, args.patientId.isNullOrEmpty().not()
        ).apply {
            findNavController().navigate(this)
        }
    }

    private fun navigateToDetail(patientId: String) {
        PatientPersonalInfoFragmentDirections.actionPersonalToDetail(patientId).apply {
            findNavController().navigate(this)
        }
    }

    private fun savePatient() {
        binding.patient?.let { patient ->
            bindGenderValue()
            patient.firstName = binding.textInputETFName.text?.toString()
            patient.middleName = binding.textInputETMName.text?.toString()
            patient.lastName = binding.textInputETLName.text?.toString()
            patient.guardianName = binding.textInputETGuardianName.text?.toString()
            validateOtherInfo(patient)
        }
    }

    private fun validateOtherInfo(patient: Patient) {
        PatientOtherInfo().apply {
            patientId = patient.uuid
            telephone = binding.countrycodeSpinner.fullNumberWithPlus
            emergencyContactNumber = binding.ccpEmContactPhone.fullNumberWithPlus
            emergencyContactType = binding.autoCompleteEmContactType.text.toString()
            emergencyContactName = binding.textInputETECName.text?.toString()
            requestId = binding.textInputRequestId.text?.toString()
            createOrUpdatePatient(patient, this)
        }
    }

    private fun createOrUpdatePatient(patient: Patient, otherInfo: PatientOtherInfo) {
        if (args.patientId.isNullOrEmpty().not()) updatePatientPersonalInfo(patient, otherInfo)
        else createPatient(patient, otherInfo)
    }

    private fun createPatient(patient: Patient, otherInfo: PatientOtherInfo) {
        otherInfo.reportDateOfPatientCreated = reportDate
        viewModel.createPatient(patient, otherInfo).observe(viewLifecycleOwner) {
            it ?: return@observe
            viewModel.allowNullDataResponse(it) { navigateToNextScreen(patient.uuid) }
        }
    }

    private fun navigateToNextScreen(patientId: String) {
        if (activeFeaturesStatus.activeStatusPatientAddress) {
            navigateToAddress(patientId)
        } else if (activeFeaturesStatus.activeStatusPatientOther) {
            navigateToOther(patientId)
        } else navigateToDetail(patientId)
    }

    private fun updatePatientPersonalInfo(patient: Patient, otherInfo: PatientOtherInfo) {
        viewModel.updatePatient(patient, otherInfo).observe(viewLifecycleOwner) {
            it ?: return@observe
            viewModel.allowNullDataResponse(it) { navigateToDetail(patient.uuid) }
        }
    }

    private fun setGender() {
        if (BuildConfig.FLAVOR_client == FlavorKeys.UNFPA) {
            binding.btnMale.isCheckable = false
            binding.btnFemale.isCheckable = false
            binding.btnOther.isCheckable = false
        }

        binding.toggleGender.addOnButtonCheckedListener { _, _, _ ->
            binding.tvGenderError.isVisible = false
            bindGenderValue()
        }
    }

    private fun bindGenderValue() {
        binding.patient?.gender = when (binding.toggleGender.checkedButtonId) {
            R.id.btnMale -> "M"
            R.id.btnFemale -> "F"
            R.id.btnOther -> "O"
            else -> "O"
        }
    }

    //
    private fun setupDOB() {
        binding.patient?.dateOfBirth?.let {
            parseDob(it, DateTimeUtils.YYYY_MM_DD_HYPHEN)

            DateTimeUtils.formatToLocalDate(
                Date(selectedDate), DateTimeUtils.MMM_DD_YYYY_FORMAT
            ).apply { binding.textInputETDob.setText(this) }
        }

        binding.textInputETDob.setOnClickListener {
            showDatePickerDialog(selectedDate)
        }
    }

    //
    private fun parseDob(date: String, format: String) {
        selectedDate = DateTimeUtils.parseDate(date, format, TimeZone.getDefault()).time
        val agePeriod = selectedDate.toPeriod()
        bindAgeValue(agePeriod)
    }

    private fun bindAgeValue(agePeriod: AgePeriod) {
        binding.textInputETAge.setText(
            agePeriod.format(requireContext(), AgePeriod.DisplayFormat.YEARS_MONTHS_DAYS)
        )
        updateGuardianVisibility(agePeriod.years, agePeriod.months, agePeriod.days)
        binding.textInputLayAge.hideError()
    }

    private fun showDatePickerDialog(selectedDate: Long) {
        CalendarDialog.Builder().maxDate(Calendar.getInstance().timeInMillis).selectedDate(selectedDate)
            .format(DateTimeUtils.MMM_DD_YYYY_FORMAT).listener(dateListener).build()
            .show(childFragmentManager, CalendarDialog.TAG)
    }

    private val dateListener = object : CalendarDialog.OnDatePickListener {
        override fun onDatePick(day: Int, month: Int, year: Int, value: String?) {
            value?.let { parseDob(it, DateTimeUtils.MMM_DD_YYYY_FORMAT) }
            updateDob()
            binding.textInputETDob.setText(value)
            binding.textInputLayDob.hideError()
        }
    }

    //
    private fun updateDob() {
        Calendar.getInstance().apply {
            timeInMillis = selectedDate
        }.also {
            DateTimeUtils.formatToLocalDate(it.time, DateTimeUtils.YYYY_MM_DD_HYPHEN).apply {
                binding.patient?.dateOfBirth = this
            }
        }
    }

    //
    private fun applyFilter() {
        binding.textInputETFName.filters = filters
        binding.textInputETMName.filters = filters
        binding.textInputETLName.filters = filters
        binding.textInputETGuardianName.filters = filters
        binding.textInputETECName.filters = filters
    }

    private val filters = arrayOf(
        FirstLetterUpperCaseInputFilter(), AlphabetsInputFilter(), LengthFilter(MAX_NAME_LENGTH)
    )

    private fun setInputTextChangListener() {
        binding.countrycodeSpinner.registerCarrierNumberEditText(binding.textInputETPhoneNumber)
        binding.countrycodeSpinner.setNumberAutoFormattingEnabled(false)
        binding.countrycodeSpinner.fullNumber = binding.otherInfo?.telephone
        binding.ccpEmContactPhone.registerCarrierNumberEditText(binding.textInputETEMPhoneNumber)
        binding.ccpEmContactPhone.setNumberAutoFormattingEnabled(false)
        binding.ccpEmContactPhone.fullNumber = binding.otherInfo?.emergencyContactNumber
        binding.textInputLayFName.hideErrorOnTextChang(binding.textInputETFName)
        binding.textInputLayMName.hideErrorOnTextChang(binding.textInputETMName)
        binding.textInputLayLName.hideErrorOnTextChang(binding.textInputETLName)
        binding.textInputLayGuardianName.hideErrorOnTextChang(binding.textInputETGuardianName)
        binding.textInputLayECName.hideErrorOnTextChang(binding.textInputETECName)
        binding.textInputLayPhoneNumber.hideDigitErrorOnTextChang(
            binding.textInputETPhoneNumber, IND_MOBILE_LEN
        )
        binding.textInputLayEMPhoneNumber.hideDigitErrorOnTextChang(
            binding.textInputETEMPhoneNumber, IND_MOBILE_LEN
        )

        binding.textInputLayRequestId.hideErrorOnTextChang(binding.textInputRequestId)
    }

    //
    private fun setupGuardianType() {
        val patient = binding.patient
        val adapter = requireContext().getSpinnerArrayAdapter(R.array.guardian_type)
        binding.autoCompleteGuardianType.setAdapter(adapter)
        if (patient?.guardianType != null && patient.guardianType.isNullOrEmpty().not()) {
            binding.autoCompleteGuardianType.setText(patient.guardianType, false)
        }
        binding.autoCompleteGuardianType.setOnItemClickListener { _, _, i, _ ->
            binding.textInputLayGuardianType.hideError()
            languageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                patient?.guardianType = this.getStringArray(R.array.guardian_type)[i]
            }
        }
    }

    //
    private fun setupEmContactType() {
        val other = binding.otherInfo
        val adapter = requireContext().getSpinnerArrayAdapter(R.array.contact_type)
        binding.autoCompleteEmContactType.setAdapter(adapter)
        if (other?.emergencyContactType != null && other.emergencyContactType.isNullOrEmpty().not()) {
            binding.autoCompleteEmContactType.setText(other.emergencyContactType, false)
        }
        binding.autoCompleteEmContactType.setOnItemClickListener { _, _, i, _ ->
            binding.textInputLayEmContactType.hideError()
            languageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                other?.emergencyContactType = this.getStringArray(R.array.contact_type)[i]
            }
        }
    }

    //
    private fun validateForm(block: () -> Unit) {
        val error = ResourceR.string.error_field_required
        val patient = binding.patient
        binding.personalConfig?.let {
//            val bProfile = validProfilePic(it)
            val bFName = validFirstName(it, error)
            val bMName = validMiddleName(it, error)
            val bLName = validLastName(it, error)
            val bGender = patient?.let { p -> validGender(it, p) } ?: true
            val bDob = validBirthDate(it, error)
            val bAge = validAge(it, error)
            val bPhone = validPhoneNumber(it, error)
            val bGuardianType = validGuardianType(it, error)
            val bGName = validGuardianName(it, error)
            val bEmName = validEmergencyContactName(it, error)
            val bEmPhone = validEmergencyPhoneNumber(it, error)
            val bEmContactType = validEmergencyContactType(it, error)
            val bRequestId = validRequestId(it, error)


            Timber.d { "fname => $bFName" }
            Timber.d { "mname => $bMName" }
            Timber.d { "lname => $bLName" }
            Timber.d { "Gender => $bGender" }
            Timber.d { "dob => $bDob" }
            Timber.d { "age => $bAge" }
            Timber.d { "phone => $bPhone" }
            Timber.d { "guardianType => $bGuardianType" }
            Timber.d { "guardianName => $bGName" }
            Timber.d { "emergencyName => $bEmName" }
            Timber.d { "emergencyPhone => $bEmPhone" }
            Timber.d { "emergencyContactType => $bEmContactType" }

            if (bFName.and(bMName).and(bLName).and(bGender).and(bDob).and(bAge).and(bPhone).and(bGName)
                    .and(bGuardianType).and(bEmName).and(bEmPhone).and(bEmContactType).and(bRequestId)
            ) block.invoke()
        }
    }

    private fun validRequestId(it: PersonalInfoConfig, error: Int): Boolean {
        return if (it.requestId?.isEnabled == true && it.requestId?.isMandatory == true) {
            binding.textInputLayRequestId.validate(binding.textInputRequestId, error)
        } else true
    }

    private fun validProfilePic(config: PersonalInfoConfig): Boolean = config.let {
        !(it.profilePic?.isEnabled == true && it.profilePic?.isMandatory == true)
    }.apply { binding.profileImageError.isVisible = this.not() }

    private fun validFirstName(config: PersonalInfoConfig, error: Int): Boolean = config.let {
        if (it.firstName?.isEnabled == true && it.firstName?.isMandatory == true) {
            binding.textInputLayFName.validate(binding.textInputETFName, error)
        } else true
    }

    private fun validMiddleName(config: PersonalInfoConfig, error: Int): Boolean = config.let {
        if (it.middleName?.isEnabled == true && it.middleName?.isMandatory == true) {
            binding.textInputLayMName.validate(binding.textInputETMName, error)
        } else true
    }

    private fun validLastName(config: PersonalInfoConfig, error: Int): Boolean = config.let {
        if (it.lastName?.isEnabled == true && it.lastName?.isMandatory == true) {
            binding.textInputLayLName.validate(binding.textInputETLName, error)
        } else true
    }

    private fun validGender(config: PersonalInfoConfig, patient: Patient): Boolean = config.let {
        if (it.gender?.isEnabled == true && it.gender?.isMandatory == true) {
            !patient.gender.isNullOrEmpty()
        } else true
    }.apply { binding.tvGenderError.isVisible = this.not() }

    private fun validBirthDate(config: PersonalInfoConfig, error: Int): Boolean = config.let {
        if (it.dob?.isEnabled == true && it.dob?.isMandatory == true) {
            binding.textInputLayDob.validate(binding.textInputETDob, error)
        } else true
    }

    private fun validAge(config: PersonalInfoConfig, error: Int): Boolean = config.let {
        if (it.age?.isEnabled == true && it.age?.isMandatory == true) {
            binding.textInputLayAge.validate(binding.textInputETAge, error)
        } else true
    }

    private fun validPhoneNumber(config: PersonalInfoConfig, error: Int): Boolean = config.let {
        Timber.d { "validPhoneNumber [enable/mandatory]=> ${it.phone?.isEnabled}/${it.phone?.isMandatory}" }
        return@let if (it.phone?.isEnabled == true && it.phone?.isMandatory == true) {
            binding.textInputLayPhoneNumber.validate(binding.textInputETPhoneNumber, error).and(
                isValidIndianPhoneNumber(binding.textInputLayPhoneNumber, binding.textInputETPhoneNumber)
            )

        } else true
    }

    private fun validGuardianType(config: PersonalInfoConfig, error: Int): Boolean = config.let {
        if (it.guardianType?.isEnabled == true && it.guardianType?.isMandatory == true) {
            binding.textInputLayGuardianType.validateDropDowb(
                binding.autoCompleteGuardianType, error
            )
        } else true
    }

    private fun validGuardianName(config: PersonalInfoConfig, error: Int): Boolean = config.let {
        if (it.guardianName?.isEnabled == true && it.guardianName?.isMandatory == true) {
            binding.textInputLayGuardianName.validate(
                binding.textInputETGuardianName, error
            )
        } else true
    }

    private fun isValidIndianPhoneNumber(inputLayout: TextInputLayout, inputText: TextInputEditText): Boolean {
        return inputLayout.validateDigit(
            inputText, getString(ResourceR.string.error_invalid_mobile_number, IND_MOBILE_LEN), IND_MOBILE_LEN
        )
    }

    private fun compareEmergencyContactNumberWithPhoneNumber(phone: String, emPhone: String): Boolean {
        return if (phone == emPhone) {
            binding.textInputLayEMPhoneNumber.error = getString(
                ResourceR.string.error_phone_number_and_emergency_number_can_not_be_the_same
            )
            false
        } else true
    }

    private fun validateAndCompareEmergencyPhoneNumber() = isValidIndianPhoneNumber(
        binding.textInputLayEMPhoneNumber, binding.textInputETEMPhoneNumber
    ).and(binding.textInputETPhoneNumber.text?.let { phone ->
        val emergencyNumber = binding.textInputETEMPhoneNumber.text.toString()
        return@let compareEmergencyContactNumberWithPhoneNumber(phone.toString(), emergencyNumber)
    } ?: true)

    private fun validEmergencyPhoneNumber(config: PersonalInfoConfig, error: Int): Boolean = config.let {
        Timber.d { "validEmergencyPhoneNumber [enable/mandatory]=> ${it.emergencyContactNumber?.isEnabled}/${it.emergencyContactNumber?.isMandatory}" }
        val isNotEmpty = binding.textInputETEMPhoneNumber.text?.isNotEmpty() ?: false
        return@let if (it.emergencyContactNumber?.isEnabled == true && it.emergencyContactNumber?.isMandatory == true) {
            binding.textInputLayEMPhoneNumber.validate(binding.textInputETEMPhoneNumber, error)
                .and(validateAndCompareEmergencyPhoneNumber())
        } else if (it.emergencyContactNumber?.isEnabled == true && isNotEmpty) {
            validateAndCompareEmergencyPhoneNumber()
        } else true
    }

    private fun validEmergencyContactName(config: PersonalInfoConfig, error: Int): Boolean = config.let {
        if (it.emergencyContactName?.isEnabled == true && it.emergencyContactName?.isMandatory == true) {
            binding.textInputLayECName.validate(binding.textInputETECName, error)
        } else true
    }

    private fun validEmergencyContactType(config: PersonalInfoConfig, error: Int): Boolean = config.let {
        if (it.emergencyContactType?.isEnabled == true && it.emergencyContactType?.isMandatory == true) {
            binding.textInputLayEmContactType.validateDropDowb(
                binding.autoCompleteEmContactType, error
            )
        } else true
    }
}
