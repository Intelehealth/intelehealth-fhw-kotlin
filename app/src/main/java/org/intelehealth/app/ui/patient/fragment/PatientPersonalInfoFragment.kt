package org.intelehealth.app.ui.patient.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPatientPersonalInfoBinding
import org.intelehealth.app.databinding.ViewPatientInfoTabBinding
import org.intelehealth.app.ui.patient.viewmodel.PatientViewModel
import org.intelehealth.config.presenter.fields.patient.utils.PatientConfigKey
import org.intelehealth.config.utility.PatientInfoGroup
import java.util.Calendar

/**
 * Created by Vaghela Mithun R. on 27-06-2024 - 13:42.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

@AndroidEntryPoint
class PatientPersonalInfoFragment : PatientInfoTabFragment(R.layout.fragment_patient_personal_info) {
    override val viewModel: PatientViewModel by viewModels()
    private lateinit var binding: FragmentPatientPersonalInfoBinding
    private var selectedDate = Calendar.getInstance().timeInMillis

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentPatientPersonalInfoBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        changeIconStatus(PatientInfoGroup.PERSONAL)
        fetchPersonalInfoConfig()
    }

    override fun getPatientInfoTabBinding(): ViewPatientInfoTabBinding = binding.patientTab

    private fun setupAge() {
//        binding.textInputETAge.setOnClickListener {
//            val dialogBinding = Dialog2NumbersPickerBinding.inflate(layoutInflater)
//            MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogStyle).apply {
//                setTitle(R.string.identification_screen_prompt_age)
//                setView(dialogBinding.root)
//                dialogBinding.dialog3NumbersUnit.visibility = View1.VISIBLE
//                dialogBinding.dialog2NumbersQuantity.minValue = 0
//                dialogBinding.dialog2NumbersQuantity.maxValue = 100
//                dialogBinding.dialog2NumbersUnit.minValue = 0
//                dialogBinding.dialog2NumbersUnit.maxValue = 12
//                dialogBinding.dialog3NumbersUnit.minValue = 0
//                dialogBinding.dialog3NumbersUnit.maxValue = 31
//                val period = getAgePeriod()
//                dialogBinding.dialog2NumbersQuantity.value = period.years
//                dialogBinding.dialog2NumbersUnit.value = period.months
//                dialogBinding.dialog3NumbersUnit.value = period.days
//            }.create().also { dialog ->
//
//                dialog.window?.let {
//                    it.setBackgroundDrawableResource(R.drawable.ui2_rounded_corners_dialog_bg)
//                    val width = resources.getDimensionPixelSize(R.dimen.internet_dialog_width)
//                    it.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
//                }
//
//                dialogBinding.btnCancelPicker.setOnClickListener { dialog.dismiss() }
//                dialogBinding.buttonOkPicker.setOnClickListener {
//                    val month = dialogBinding.dialog2NumbersUnit.value
//                    val year = dialogBinding.dialog2NumbersQuantity.value
//                    val days = dialogBinding.dialog3NumbersUnit.value
//
//                    bindAgeAndDobValue(year, month, days)
//                    dialog.dismiss()
//                }
//
//            }.show()
//        }
    }

    //    private fun bindAgeAndDobValue(year: Int, month: Int, days: Int) {
//        bindAgeValue(year, month, days)
//
//        Calendar.getInstance().apply {
//            add(Calendar.YEAR, -year)
//            add(Calendar.MONTH, -month)
//            add(Calendar.DAY_OF_MONTH, -days)
//        }.also { bindDobValue(it) }
//    }
//
//    private fun bindAgeValue(year: Int, month: Int, days: Int) {
//        DateAndTimeUtils.formatAgeInYearsMonthsDate(
//            context,
//            year,
//            month,
//            days
//        ).apply { binding.textInputETAge.setText(this) }
//
//        updateGuardianVisibility(year, month, days)
//        binding.textInputLayAge.hideError()
//    }
//
//    private fun updateGuardianVisibility(year: Int, month: Int, days: Int) {
//        val visibility = AgeUtils.isGuardianRequired(year, month, days)
//        Timber.d { "Year[$year]/Month[$month]/Day[$days] => visibility[$visibility]" }
//        binding.personalConfig?.let {
//            Timber.d { "personalConfig => visibility[$visibility]" }
//            it.guardianName?.isEnabled = it.guardianName?.isEnabled?.let { enabled ->
//                if (visibility) enabled else false
//            } ?: visibility
//
//            it.guardianType?.isEnabled = it.guardianType?.isEnabled?.let { enabled ->
//                if (visibility) enabled else false
//            } ?: visibility
//
//            binding.llGuardianName.isVisible = it.guardianName?.isEnabled ?: visibility
//            binding.llGuardianType.isVisible = it.guardianType?.isEnabled ?: visibility
//            binding.personalConfig = it
//        }
//    }
//
//    private fun bindDobValue(calendar: Calendar) {
//        selectedDate = calendar.timeInMillis
//        val sdf = DateTimeUtils.getSimpleDateFormat(
//            DateTimeUtils.MMM_DD_YYYY_FORMAT,
//            TimeZone.getDefault()
//        )
//        val formattedDate = sdf.format(calendar.time)
//        binding.textInputETDob.setText(formattedDate)
//        binding.textInputLayDob.hideError()
//        updateDob()
//    }
//
//    override fun onPatientDataLoaded(patient: PatientDTO) {
//        super.onPatientDataLoaded(patient)
//        Timber.d { "onPatientDataLoaded" }
//        Timber.d { Gson().toJson(patient) }
//        fetchPersonalInfoConfig()
//        if (BuildConfig.FLAVOR_client == FlavorKeys.UNFPA) {
//            patient.apply {
//                gender = gender ?: "F"
//            }
//        }
//        binding.patient = patient
//        binding.isEditMode = patientViewModel.isEditMode
//    }
//
    private fun fetchPersonalInfoConfig() {
        viewModel.fetchPersonalRegFields()
        viewModel.personalSectionFieldsLiveData.observe(viewLifecycleOwner) {
            binding.personalConfig = PatientConfigKey.buildPatientPersonalInfoConfig(it)
//            setupGuardianType()
//            setupEmContactType()
//            setupDOB()
//            setupAge()
//            applyFilter()
//            setGender()
            setClickListener()
//            setInputTextChangListener()
//            binding.addOnRebindCallback(onRebindCallback)
        }
    }

    //
////    private val onRebindCallback =
////        object : OnRebindCallback<FragmentPatientPersonalInfoOldDesignBinding>() {
////            on
////            override fun onBound(binding: FragmentPatientPersonalInfoOldDesignBinding?) {
////                super.onBound(binding)
////                Timber.d { "OnRebindCallback.onBound" }
////
////            }
////        }
//
    private fun setClickListener() {
        binding.patientImgview.setOnClickListener { requestPermission() }
        binding.btnPatientPersonalNext.setOnClickListener {
            val directions = PatientPersonalInfoFragmentDirections.actionPersonalToAddress("0")
            findNavController().navigate(directions)
//            validateForm { savePatient() }
        }
    }
//
//    private fun savePatient() {
//        patient.apply {
//            bindGenderValue()
//            firstname = binding.textInputETFName.text?.toString()
//            middlename = binding.textInputETMName.text?.toString()
//            lastname = binding.textInputETLName.text?.toString()
//            phonenumber = binding.countrycodeSpinner.fullNumberWithPlus
//            guardianName = binding.textInputETGuardianName.text?.toString()
//            emContactName = binding.textInputETECName.text?.toString()
//            emContactNumber = binding.ccpEmContactPhone.fullNumberWithPlus
//
//            patientViewModel.updatedPatient(this)
//            if (patientViewModel.isEditMode) {
//                saveAndNavigateToDetails()
//            } else {
//                if (patientViewModel.activeStatusAddressSection) {
//                    PatientPersonalInfoFragmentDirections.navigationPersonalToAddress().apply {
//                        findNavController().navigate(this)
//                    }
//                } else if (patientViewModel.activeStatusOtherSection) {
//                    PatientPersonalInfoFragmentDirections.navigationPersonalToOther().apply {
//                        findNavController().navigate(this)
//                    }
//                } else saveAndNavigateToDetails()
//            }
//        }
//    }
//
//    private fun saveAndNavigateToDetails() {
//        patientViewModel.savePatient().observe(viewLifecycleOwner) {
//            it ?: return@observe
//            patientViewModel.handleResponse(it) { result -> if (result) navigateToDetails() }
//        }
//    }
//
//    private fun navigateToDetails() {
//        PatientPersonalInfoFragmentDirections.navigationPersonalToDetails(
//            patient.uuid, "searchPatient", "false"
//        ).apply {
//            findNavController().navigate(this)
//            requireActivity().finish()
//        }
//    }
//
//    private fun setGender() {
//        if (BuildConfig.FLAVOR_client == FlavorKeys.UNFPA) {
//            binding.btnMale.isCheckable = false
//            binding.btnFemale.isCheckable = false
//            binding.btnOther.isCheckable = false
//        }
//        binding.toggleGender.addOnButtonCheckedListener { _, checkedId, _ ->
//            binding.tvGenderError.isVisible = false
//            bindGenderValue()
//        }
//    }
//
//    private fun bindGenderValue() {
//        patient.gender = when (binding.toggleGender.checkedButtonId) {
//            R.id.btnMale -> "M"
//            R.id.btnFemale -> "F"
//            R.id.btnOther -> "O"
//            else -> "O"
//        }
//    }
//
//    private fun setupDOB() {
//        patient.dateofbirth?.let {
//            parseDob(it, DateTimeUtils.YYYY_MM_DD_HYPHEN)
//
//            DateTimeUtils.formatToLocalDate(
//                Date(selectedDate),
//                DateTimeUtils.MMM_DD_YYYY_FORMAT
//            ).apply { binding.textInputETDob.setText(this) }
//        }
//
//        binding.textInputETDob.setOnClickListener {
//            showDatePickerDialog(selectedDate)
//        }
//    }
//
//    private fun parseDob(date: String, format: String) {
//        selectedDate = DateTimeUtils.parseDate(date, format, TimeZone.getDefault()).time
//        val period = getAgePeriod()
//        bindAgeValue(period.years, period.months, period.days)
//    }
//
//    private fun getAgePeriod(): Period {
//        val pastCalendar = Calendar.getInstance().apply { timeInMillis = selectedDate }
//        val birthdate = LocalDate(
//            pastCalendar[Calendar.YEAR],
//            pastCalendar[Calendar.MONTH] + 1,
//            pastCalendar[Calendar.DAY_OF_MONTH]
//        ) //Birth date
//        val now = LocalDate() //Today's date
//        return Period(birthdate, now, PeriodType.yearMonthDay())
//    }
//
//    fun setLocale(context: Context): Context {
//        val appLanguage = languageUtils.getLocalLang()
//        val res = context.resources
//        val conf = res.configuration
//        val locale = Locale(appLanguage)
//        Locale.setDefault(locale)
//        conf.setLocale(locale)
//        context.createConfigurationContext(conf)
//        val dm = res.displayMetrics
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            conf.setLocales(LocaleList(locale))
//        } else {
//            conf.locale = locale
//        }
//        res.updateConfiguration(conf, dm)
//        return context
//    }
//
//
//    private fun showDatePickerDialog(selectedDate: Long) {
//        setLocale(requireActivity())
//        CalendarDialog.Builder()
//            .maxDate(Calendar.getInstance().timeInMillis)
//            .selectedDate(selectedDate)
//            .format(DateTimeUtils.MMM_DD_YYYY_FORMAT)
//            .listener(dateListener)
//            .build().show(childFragmentManager, CalendarDialog.TAG)
//    }
//
//    private val dateListener = object : CalendarDialog.OnDatePickListener {
//        override fun onDatePick(day: Int, month: Int, year: Int, value: String?) {
//            value?.let { parseDob(it, DateTimeUtils.MMM_DD_YYYY_FORMAT) }
//            updateDob()
//            binding.textInputETDob.setText(value)
//            binding.textInputLayDob.hideError()
//        }
//    }
//
//    private fun updateDob() {
//        Calendar.getInstance().apply {
//            timeInMillis = selectedDate
//        }.also {
//            DateTimeUtils.formatToLocalDate(it.time, DateTimeUtils.YYYY_MM_DD_HYPHEN).apply {
//                patient.dateofbirth = this
//            }
//        }
//    }
//
//    private fun applyFilter() {
//        //  binding.textInputETFName.addFilter(FirstLetterUpperCaseInputFilter())
//        binding.textInputETFName.filters =
//            arrayOf(FirstLetterUpperCaseInputFilter(), inputFilter_Others, LengthFilter(25))
//        binding.textInputETMName.filters =
//            arrayOf(FirstLetterUpperCaseInputFilter(), inputFilter_Others, LengthFilter(25))
//        binding.textInputETLName.filters =
//            arrayOf(FirstLetterUpperCaseInputFilter(), inputFilter_Others, LengthFilter(25))
//        binding.textInputETGuardianName.filters =
//            arrayOf(FirstLetterUpperCaseInputFilter(), inputFilter_Others, LengthFilter(25))
//        binding.textInputETECName.filters =
//            arrayOf(FirstLetterUpperCaseInputFilter(), inputFilter_Others, LengthFilter(25))
//    }
//
//    private fun setInputTextChangListener() {
//        binding.countrycodeSpinner.registerCarrierNumberEditText(binding.textInputETPhoneNumber)
//        binding.countrycodeSpinner.setNumberAutoFormattingEnabled(false)
//        binding.countrycodeSpinner.fullNumber = patient.phonenumber
//        binding.ccpEmContactPhone.registerCarrierNumberEditText(binding.textInputETEMPhoneNumber)
//        binding.ccpEmContactPhone.setNumberAutoFormattingEnabled(false)
//        binding.ccpEmContactPhone.fullNumber = patient.emContactNumber
//        binding.textInputLayFName.hideErrorOnTextChang(binding.textInputETFName)
//        binding.textInputLayMName.hideErrorOnTextChang(binding.textInputETMName)
//        binding.textInputLayLName.hideErrorOnTextChang(binding.textInputETLName)
//        binding.textInputLayGuardianName.hideErrorOnTextChang(binding.textInputETGuardianName)
//        binding.textInputLayECName.hideErrorOnTextChang(binding.textInputETECName)
//        binding.textInputLayPhoneNumber.hideDigitErrorOnTextChang(
//            binding.textInputETPhoneNumber,
//            10
//        )
//        binding.textInputLayEMPhoneNumber.hideDigitErrorOnTextChang(
//            binding.textInputETEMPhoneNumber,
//            10
//        )
//    }
//
//    private fun setupGuardianType() {
//        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.guardian_type)
//        binding.autoCompleteGuardianType.setAdapter(adapter)
//        if (patient.guardianType != null && patient.guardianType.isNotEmpty()) {
//            binding.autoCompleteGuardianType.setText(patient.guardianType, false)
//        }
//        binding.autoCompleteGuardianType.setOnItemClickListener { _, _, i, _ ->
//            binding.textInputLayGuardianType.hideError()
//            languageUtils.getSpecificLocalResource(requireContext(), "en").apply {
//                patient.guardianType = this.getStringArray(R.array.guardian_type)[i]
//            }
//        }
//    }
//
//    private fun setupEmContactType() {
//        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.contact_type)
//        binding.autoCompleteEmContactType.setAdapter(adapter)
//        if (patient.contactType != null && patient.contactType.isNotEmpty()) {
//            binding.autoCompleteEmContactType.setText(patient.contactType, false)
//        }
//        binding.autoCompleteEmContactType.setOnItemClickListener { _, _, i, _ ->
//            binding.textInputLayEmContactType.hideError()
//            languageUtils.getSpecificLocalResource(requireContext(), "en").apply {
//                patient.contactType = this.getStringArray(R.array.contact_type)[i]
//            }
//        }
//    }
//
//    private fun validateForm(block: () -> Unit) {
//        val error = R.string.this_field_is_mandatory
//        binding.personalConfig?.let {
//            val bProfile =
//                if (it.profilePic?.isEnabled == true && it.profilePic?.isMandatory == true) {
//                    !patient.patientPhoto.isNullOrEmpty()
//                } else true
//
//            binding.profileImageError.isVisible = bProfile.not()
//
//            val bFName = if (it.firstName?.isEnabled == true && it.firstName?.isMandatory == true) {
//                binding.textInputLayFName.validate(binding.textInputETFName, error)
//            } else true
//
//            val bMName =
//                if (it.middleName?.isEnabled == true && it.middleName?.isMandatory == true) {
//                    binding.textInputLayMName.validate(binding.textInputETMName, error)
//                } else true
//
//            val bLName = if (it.lastName?.isEnabled == true && it.lastName?.isMandatory == true) {
//                binding.textInputLayLName.validate(binding.textInputETLName, error)
//            } else true
//
//            val bGender = if (it.gender?.isEnabled == true && it.gender?.isMandatory == true) {
//                !patient.gender.isNullOrEmpty()
//            } else true
//
//            binding.tvGenderError.isVisible = bGender.not()
//
//            val bDob = if (it.dob?.isEnabled == true && it.dob?.isMandatory == true) {
//                binding.textInputLayDob.validate(binding.textInputETDob, error)
//            } else true
//
//            val bAge = if (it.age?.isEnabled == true && it.age?.isMandatory == true) {
//                binding.textInputLayAge.validate(binding.textInputETAge, error)
//            } else true
//
//            val bPhone = if (it.phone?.isEnabled == true && it.phone?.isMandatory == true) {
//                binding.textInputLayPhoneNumber.validate(binding.textInputETPhoneNumber, error).and(
//                    binding.textInputLayPhoneNumber.validateDigit(
//                        binding.textInputETPhoneNumber,
//                        R.string.enter_10_digits,
//                        10
//                    )
//                )
//
//            } else true
//            val bEPhone =
//                if (it.emergencyContactNumber!!.isEnabled && binding.textInputETEMPhoneNumber.length() > 0 && binding.textInputETEMPhoneNumber.length() < 10) {
//                    binding.textInputLayEMPhoneNumber.validate(binding.textInputETEMPhoneNumber, error)
//                        .and(
//                            binding.textInputLayEMPhoneNumber.validateDigit(
//                                binding.textInputETEMPhoneNumber,
//                                R.string.enter_10_digits,
//                                10
//                            )
//                        )
//
//                } else true
//
//
//            val bGuardianType =
//                if (it.guardianType?.isEnabled == true && it.guardianType?.isMandatory == true) {
//                    binding.textInputLayGuardianType.validateDropDowb(
//                        binding.autoCompleteGuardianType,
//                        error
//                    )
//                } else true
//
//            val bGName =
//                if (it.guardianName?.isEnabled == true && it.guardianName?.isMandatory == true) {
//                    binding.textInputLayGuardianName.validate(
//                        binding.textInputETGuardianName,
//                        error
//                    )
//                } else true
//
//            val bEmName =
//                if (it.emergencyContactName?.isEnabled == true && it.emergencyContactName?.isMandatory == true) {
//                    binding.textInputLayECName.validate(binding.textInputETECName, error)
//                } else true
//
//            val bEmPhone =
//                if (it.emergencyContactNumber?.isEnabled == true && it.emergencyContactNumber?.isMandatory == true) {
//                    Timber.d { "Emergency validation" }
//                    binding.textInputLayEMPhoneNumber.validate(
//                        binding.textInputETEMPhoneNumber,
//                        error
//                    ).and(
//                        binding.textInputLayEMPhoneNumber.validateDigit(
//                            binding.textInputETEMPhoneNumber,
//                            R.string.enter_10_digits,
//                            10
//                        )
//                    ).and(binding.textInputETPhoneNumber.text?.let { phone ->
//                        val valid =
//                            phone.toString() != binding.textInputETEMPhoneNumber.text.toString()
//                        if (!valid) {
//                            binding.textInputLayEMPhoneNumber.error = getString(
//                                R.string.phone_number_and_emergency_number_can_not_be_the_same
//                            )
//                        }
//                        valid
//                    } ?: false)
//
//                }
//                // comparing em-contact number with phone number only
//                // when field is not mandatory
//                else {
//                    binding.textInputETEMPhoneNumber.let { etEm ->
//                        // checking emergency contact number entered or not
//                        // if entered, then checking the 10 digits validation and
//                        // comparing with phone number
//                        if (etEm.text?.isNotEmpty() == true) {
//                            binding.textInputLayEMPhoneNumber.validateDigit(
//                                binding.textInputETEMPhoneNumber,
//                                R.string.enter_10_digits,
//                                10
//                            ).and(binding.textInputETPhoneNumber.text?.let { phone ->
//                                val valid =
//                                    phone.toString() != binding.textInputETEMPhoneNumber.text.toString()
//                                if (!valid) {
//                                    binding.textInputLayEMPhoneNumber.error = getString(
//                                        R.string.phone_number_and_emergency_number_can_not_be_the_same
//                                    )
//                                }
//                                valid
//                            } ?: false)
//                        } else true
//                    }
//                }
//
//            val bEmContactType =
//                if (it.emergencyContactType?.isEnabled == true && it.emergencyContactType?.isMandatory == true) {
//                    binding.textInputLayEmContactType.validateDropDowb(
//                        binding.autoCompleteEmContactType,
//                        error
//                    )
//                } else true
//
//            if (bProfile.and(bFName).and(bMName).and(bLName).and(bGender)
//                    .and(bDob).and(bAge).and(bPhone).and(bGName).and(bGuardianType)
//                    .and(bEmName).and(bEmPhone).and(bEmContactType).and(bEPhone)
//            ) block.invoke()
//        }
//    }
}
