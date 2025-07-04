package org.intelehealth.app.ui.patient.fragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPatientAddressInfoBinding
import org.intelehealth.app.databinding.ViewPatientInfoTabBinding
import org.intelehealth.app.model.address.Block
import org.intelehealth.app.model.address.DistData
import org.intelehealth.app.model.address.ProvincesAndCities
import org.intelehealth.app.model.address.StateData
import org.intelehealth.app.model.address.Village
import org.intelehealth.app.ui.patient.viewmodel.PatientAddressViewModel
import org.intelehealth.app.utility.MIN_CHAR_LENGTH
import org.intelehealth.app.utility.POSTAL_CODE_LEN
import org.intelehealth.common.extensions.addFilter
import org.intelehealth.common.extensions.getLocalResource
import org.intelehealth.common.extensions.getLocalString
import org.intelehealth.common.extensions.getLocalValueFromArray
import org.intelehealth.common.extensions.getSpinnerArrayAdapter
import org.intelehealth.common.extensions.getSpinnerItemAdapter
import org.intelehealth.common.extensions.hideDigitErrorOnTextChang
import org.intelehealth.common.extensions.hideError
import org.intelehealth.common.extensions.hideErrorOnTextChang
import org.intelehealth.common.extensions.setEditableDropDownStatus
import org.intelehealth.common.extensions.showToast
import org.intelehealth.common.extensions.validate
import org.intelehealth.common.extensions.validateDigit
import org.intelehealth.common.extensions.validateDropDowb
import org.intelehealth.common.inputfilter.AlphaNumericInputFilter
import org.intelehealth.common.inputfilter.AlphabetsInputFilter
import org.intelehealth.common.inputfilter.FirstLetterUpperCaseInputFilter
import org.intelehealth.common.utility.ArrayAdapterUtils
import org.intelehealth.config.presenter.fields.patient.infoconfig.AddressInfoConfig
import org.intelehealth.config.presenter.fields.patient.utils.PatientConfigKey
import org.intelehealth.config.room.entity.PatientRegistrationFields
import org.intelehealth.config.utility.PatientInfoGroup
import org.intelehealth.data.offline.entity.PersonAddress
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 27-06-2024 - 13:42.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class PatientAddressInfoFragment : PatientInfoTabFragment(R.layout.fragment_patient_address_info) {
    override val viewModel: PatientAddressViewModel by viewModels()
    private lateinit var binding: FragmentPatientAddressInfoBinding
    private val args by navArgs<PatientAddressInfoFragmentArgs>()
    private var personAddress = PersonAddress()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentPatientAddressInfoBinding.bind(view)
        binding.textInputLayDistrict.isEnabled = false
        binding.isEditMode = args.editMode
        super.onViewCreated(view, savedInstanceState)
        fetchAddressInfoConfig()
        changeIconStatus(PatientInfoGroup.ADDRESS)
        observeAddressData()
    }

    private fun observeAddressData() {
        viewModel.fetchPatientAddress(args.patientId).observe(viewLifecycleOwner) { address ->
            address ?: return@observe
            Timber.d { "Address => ${Gson().toJson(address)}" }
            binding.address = address
            personAddress = address
        }
    }

    override fun getPatientInfoTabBinding(): ViewPatientInfoTabBinding = binding.patientTab

    private fun setupCountries() {
        val adapter = requireContext().getSpinnerArrayAdapter(R.array.countries)
        val country = personAddress.country
        binding.autoCompleteCountry.setAdapter(adapter)
        if (!country.isNullOrEmpty()) {
            val localCountry = requireContext().getLocalValueFromArray(country, R.array.countries)
            binding.autoCompleteCountry.setText(localCountry, false)
        } else {
            val defaultValue = getString(ResourceR.string.default_country)
            Timber.d { "default $defaultValue index[${adapter.getPosition(defaultValue)}]" }
            binding.autoCompleteCountry.setText(defaultValue, false)
            requireContext().getLocalString(resId = ResourceR.string.default_country).apply {
                Timber.d { "default country => $this" }
                personAddress.country = this
            }
        }
        binding.textInputLayCountry.isEnabled = false
        binding.autoCompleteCountry.setOnItemClickListener { _, _, i, _ ->
            binding.textInputLayCountry.hideError()
            requireContext().getLocalResource().apply {
                personAddress.country = this.getStringArray(R.array.countries)[i]
            }
        }
    }

    private fun fetchAddressInfoConfig() {
        viewModel.fetchAddressRegFields()
        viewModel.addressSectionFieldsLiveData.observe(viewLifecycleOwner) {
            binding.addressInfoConfig = PatientConfigKey.buildPatientAddressInfoConfig(it)
            Timber.d { "Address Config => ${Gson().toJson(binding.addressInfoConfig)}" }
            setClickListener()
            setupUIComponents()
        }
    }

    private fun setupUIComponents() {
        setupCountries()
        binding.addressInfoConfig?.let { configureDropDowns(it) }
//        setupStates()
        applyFilter()
        setInputTextChangListener()
        setClickListener()
//        if (BuildConfig.FLAVOR_client == FlavorKeys.UNFPA) {
//            setupProvinceAndCities()
//        }
    }

    private fun configureDropDowns(config: AddressInfoConfig) {
        if (config.isStateActive()) setupStates()
        else if (config.isProvinceActive()) setupProvinceAndCities()
    }

    //
    private fun setClickListener() {
        binding.frag2BtnBack.setOnClickListener {
//            setOtherBlockData()
            findNavController().popBackStack()
        }
        binding.frag2BtnNext.setOnClickListener {
            validateForm {
                showToast("Valid address")
                savePatient()
            }
//            setOtherBlockData()
//            validateForm { savePatient() }
        }
    }

    //
    private fun savePatient() {
        personAddress.apply {
            uuid = args.patientId
            postalCode = binding.textInputPostalCode.text?.toString()
            address2 = binding.textInputAddress2.text?.toString()
            addressOfHf = binding.textInputRegistrationAddressOfHf.text?.toString()
            address1 = binding.textInputAddress1.text?.toString()
            address6 = binding.textInputHouseholdNumber.text?.toString()
            cityVillage = binding.textInputCityVillage.text.toString()

            if (binding.llBlock.isEnabled) {
                if (binding.autoCompleteBlock.text.toString()
                        .equals(getString(ResourceR.string.lbl_other_block), ignoreCase = true)
                ) {
                    address3 = binding.textInputOtherBlock.text.toString()
                    cityVillage = binding.textInputCityVillage.text?.toString().toString()
                }
            } else {
                cityVillage = binding.textInputCityVillage.text.toString()
            }
        }.also {
            Timber.d { "Final patient => $it" }
            saveAndNavigateToDetails(it)
        }
    }

    private fun saveAndNavigateToDetails(personAddress: PersonAddress) {
        viewModel.addAddress(personAddress).observe(viewLifecycleOwner) { result ->
            result ?: return@observe
            viewModel.handleResponse(result) {
                PatientAddressInfoFragmentDirections.actionAddressToOther(
                    args.patientId, args.editMode
                ).apply {
                    findNavController().navigate(this)
                }
            }
        }
    }

    //
//    private fun saveAndNavigateToDetails() {
//        patientViewModel.savePatient().observe(viewLifecycleOwner) {
//            it ?: return@observe
//            patientViewModel.handleResponse(it) { result -> if (result) navigateToDetails() }
//        }
//    }
//
//    private fun navigateToDetails() {
//        PatientAddressInfoFragmentDirections.navigationAddressToDetails(
//            patient.uuid, "searchPatient", "false"
//        ).apply {
//            findNavController().navigate(this)
//            requireActivity().finish()
//        }
//    }
//
    private fun applyFilter() {
        binding.textInputCityVillage.addFilter(FirstLetterUpperCaseInputFilter())
        //   binding.textInputAddress1.addFilter(FirstLetterUpperCaseInputFilter())
        binding.textInputAddress1.filters = arrayOf(FirstLetterUpperCaseInputFilter(), AlphaNumericInputFilter())
        binding.textInputAddress2.addFilter(FirstLetterUpperCaseInputFilter())
        // binding.textInputOtherBlock.addFilter(FirstLetterUpperCaseInputFilter())
        binding.textInputOtherBlock.filters = arrayOf(FirstLetterUpperCaseInputFilter(), AlphabetsInputFilter())
    }

    private fun setInputTextChangListener() {
        binding.textInputLayCityVillage.hideErrorOnTextChang(binding.textInputCityVillage)
        binding.textInputLayAddress1.hideErrorOnTextChang(binding.textInputAddress1)
        binding.textInputLayAddress2.hideErrorOnTextChang(binding.textInputAddress2)
        binding.textInputLayRegistrationAddressOfHf.hideErrorOnTextChang(binding.textInputRegistrationAddressOfHf)

        binding.textInputLayPostalCode.hideDigitErrorOnTextChang(binding.textInputPostalCode, 6)
        binding.textInputLayOtherBlock.hideErrorOnTextChang(binding.textInputOtherBlock)
        binding.textInputLayHouseholdNumber.hideErrorOnTextChang(binding.textInputHouseholdNumber)
    }

    private fun setupStates() {
        val defaultState = languageUtils.getState(getString(ResourceR.string.default_state))

        languageUtils.getStateList()?.let {
            binding.textInputLayStateAndProvince.tag = it
            val adapter: ArrayAdapter<StateData> = requireContext().getSpinnerItemAdapter(it)
            binding.autoCompleteStateAndProvince.setAdapter(adapter)

            personAddress.state?.let { s ->
                val state = languageUtils.getState(s)
                if (state != null) {
                    binding.autoCompleteStateAndProvince.setText(state.toString(), false)
                    setupDistricts(state)
                }
            } ?: run {
                // If no state is set, use the default state
                if (defaultState != null) {
                    binding.autoCompleteStateAndProvince.setText(defaultState.toString(), false)
                    personAddress.state = defaultState.state
                    setupDistricts(defaultState)
                } else {
                    binding.autoCompleteStateAndProvince.setText("", false)
                    personAddress.state = ""
                }
            }

            binding.autoCompleteStateAndProvince.setOnItemClickListener { _, _, i, _ ->
                binding.textInputLayStateAndProvince.hideError()
                val list: List<StateData> = binding.textInputLayStateAndProvince.tag as List<StateData>
                val selectedState = list[i]
                binding.autoCompleteDistrict.setText("", false)
                personAddress.district = binding.autoCompleteDistrict.text.toString()
                personAddress.state = selectedState.state
                setupDistricts(selectedState)
            }
        }
    }

    private fun setupProvinceAndCities() {
        languageUtils.getProvincesAndCities().let { provinces ->
            //province
            binding.textInputLayStateAndProvince.tag = provinces
            val adapter: ArrayAdapter<String> = ArrayAdapterUtils.getSpinnerItemAdapter(
                requireContext(), provinces.provinces
            )
            binding.autoCompleteStateAndProvince.setAdapter(adapter)
            personAddress.state?.let {
                val province = languageUtils.getProvince(it)
                if (province != null) {
                    binding.autoCompleteStateAndProvince.setText(province.toString(), false)
                }
            }

            binding.autoCompleteStateAndProvince.setOnItemClickListener { _, _, i, _ ->
                binding.textInputLayStateAndProvince.hideError()
                val provincesAndCities: ProvincesAndCities =
                    binding.textInputLayStateAndProvince.tag as ProvincesAndCities
                personAddress.state = provincesAndCities.provinces[i]
            }

            //cities
            binding.textInputLayCity.tag = provinces
            val cityAdapter: ArrayAdapter<String> = ArrayAdapterUtils.getSpinnerItemAdapter(
                requireContext(), provinces.cities
            )
            binding.autoCompleteCity.setAdapter(cityAdapter)

            personAddress.cityVillage?.let {
                val city = languageUtils.getCity(it)
                if (city != null) {
                    binding.autoCompleteCity.setText(city.toString(), false)
                }
            }

            binding.autoCompleteCity.setOnItemClickListener { _, _, i, _ ->
                binding.textInputLayCity.hideError()
                val provincesAndCities: ProvincesAndCities =
                    binding.textInputLayCity.tag as ProvincesAndCities
                personAddress.cityVillage = provincesAndCities.cities[i]
            }
        }
    }

    private fun setupDistricts(stateData: StateData) {

        val isConfigDistrictEditable = binding.addressInfoConfig?.district?.isEditable ?: true
        val defaultDistrict = languageUtils.getDistrict(stateData, getString(ResourceR.string.default_district))

        val adapter: ArrayAdapter<DistData> = requireContext().getSpinnerItemAdapter(stateData.distDataList)
        binding.autoCompleteDistrict.setAdapter(adapter)

        personAddress.district?.let {
            val selected = languageUtils.getDistrict(stateData, it)
            if (selected != null) {
                binding.autoCompleteDistrict.setText(selected.toString(), false)
                if (binding.llBlock.isEnabled) setupBlocks(selected)
            }
        } ?: run {
            // If no district is set, use the default district
            if (defaultDistrict != null && defaultDistrict.name.isNotEmpty()) {
                binding.autoCompleteDistrict.setText(defaultDistrict.toString(), false)
                personAddress.district = defaultDistrict.name
                if (binding.addressInfoConfig?.block?.isEnabled == true) setupBlocks(defaultDistrict)
            } else {
                binding.autoCompleteDistrict.setText("", false)
                personAddress.district = ""
            }
        }

        binding.textInputLayDistrict.tag = stateData.distDataList
        binding.autoCompleteDistrict.setOnItemClickListener { _, _, i, _ ->
            binding.textInputLayDistrict.hideError()
            val dList: List<DistData> = binding.textInputLayDistrict.tag as List<DistData>
            //patient.district = dList[i].name
            personAddress.district = dList[i].name
            if (binding.addressInfoConfig?.block?.isEnabled == true) setupBlocks(dList[i])
        }

        val editable = (isConfigDistrictEditable && defaultDistrict?.name.isNullOrEmpty())
        binding.textInputLayDistrict.setEditableDropDownStatus(editable, binding.autoCompleteDistrict)
    }

    private fun validPostalCode(config: PatientRegistrationFields?, error: Int): Boolean {
        val postalCode = binding.textInputPostalCode.text.toString()
        return if (postalCode.isNotEmpty()) {
            binding.textInputLayPostalCode.validateDigit(
                binding.textInputPostalCode,
                ResourceR.string.error_invalid_postal_code,
                POSTAL_CODE_LEN
            )
        } else if (config?.isEnabled == true && config.isMandatory) {
            binding.textInputLayPostalCode.validate(binding.textInputPostalCode, ResourceR.string.error_field_required)
        } else true
    }

    private fun validCountry(config: PatientRegistrationFields?, error: Int): Boolean {
        return if (config?.isEnabled == true && config.isMandatory) {
            binding.textInputLayCountry.validateDropDowb(binding.autoCompleteCountry, error)
        } else true
    }

    private fun validState(config: PatientRegistrationFields?, error: Int): Boolean {
        return if (config?.isEnabled == true && config.isMandatory) {
            binding.textInputLayStateAndProvince.validateDropDowb(binding.autoCompleteStateAndProvince, error)
        } else true
    }

    private fun validDistrict(config: PatientRegistrationFields?, error: Int): Boolean {
        return if (config?.isEnabled == true && config.isMandatory) {
            binding.textInputLayDistrict.validateDropDowb(binding.autoCompleteDistrict, error)
        } else true
    }

    private fun validCityVillage(config: PatientRegistrationFields?, error: Int): Boolean {
        return if (config?.isEnabled == true && config.isMandatory) {
            val valid = binding.textInputLayCityVillage.validate(binding.textInputCityVillage, error)
            val validDigit = binding.textInputLayCityVillage.validateDigit(
                binding.textInputCityVillage,
                ResourceR.string.error_invalid_village,
                MIN_CHAR_LENGTH
            )
            valid && validDigit
        } else true
    }

    private fun validCity(config: PatientRegistrationFields?, error: Int): Boolean {
        return if (config?.isEnabled == true && config.isMandatory) {
            binding.textInputLayCity.validateDropDowb(binding.autoCompleteCity, error)
        } else true
    }

    private fun validRelativeAddressOfHf(config: PatientRegistrationFields?, error: Int): Boolean {
        return if (config?.isEnabled == true && config.isMandatory) {
            binding.textInputLayRegistrationAddressOfHf.validate(
                binding.textInputRegistrationAddressOfHf, error
            )
        } else true
    }

    private fun validAddress1(config: PatientRegistrationFields?, error: Int): Boolean {
        return if (config?.isEnabled == true && config.isMandatory) {
            binding.textInputLayAddress1.validate(binding.textInputAddress1, error)
        } else true
    }

    private fun validAddress2(config: PatientRegistrationFields?, error: Int): Boolean {
        return if (config?.isEnabled == true && config.isMandatory) {
            binding.textInputLayAddress2.validate(binding.textInputAddress2, error)
        } else true
    }

    private fun validBlock(config: PatientRegistrationFields?, error: Int): Boolean {
        return if (config?.isEnabled == true && config.isMandatory) {
            binding.textInputLayBlock.validateDropDowb(binding.autoCompleteBlock, error)
        } else true
    }

    private fun validVillageField(config: PatientRegistrationFields?, error: Int): Boolean {
        return if (config?.isEnabled == true && config.isMandatory) {
            val isOtherBlockSelected = binding.autoCompleteBlock.text.toString()
                .equals(getString(ResourceR.string.lbl_other_block), ignoreCase = true)
            if (isOtherBlockSelected) {
                binding.textInputLayOtherBlock.validate(binding.textInputOtherBlock, error)
                binding.textInputLayCityVillage.validate(binding.textInputCityVillage, error)
            } else {
                binding.textInputLayVillageDropdown.validateDropDowb(binding.autoCompleteVillageDropdown, error)
            }
        } else true
    }

    private fun validHouseholdNumber(config: PatientRegistrationFields?, error: Int): Boolean {
        return if (config?.isEnabled == true && config.isMandatory) {
            binding.textInputLayHouseholdNumber.validate(binding.textInputHouseholdNumber, error)
        } else true
    }

    /**
     * Validates the form fields based on the provided configuration and executes the block if all validations pass.
     */
    private fun validateForm(block: () -> Unit) {
        Timber.d { "Final patient =>${Gson().toJson(binding.address)}" }
        val error = ResourceR.string.error_field_required
        binding.addressInfoConfig?.let {
            val bPostalCode = validPostalCode(it.postalCode, error)
            val bCountry = validCountry(it.country, error)
            val bState = validState(it.state, error)
            val bDistrict = validDistrict(it.district, error)
            val bCityVillage = validCityVillage(it.cityVillage, error)
//            val bProvince = validProvince(it.province, error)
            val bCity = validCity(it.city, error)
            val bRelativeAddressOfHf = validRelativeAddressOfHf(it.registrationAddressOfHf, error)
            val bAddress1 = validAddress1(it.address1, error)
            val bAddress2 = validAddress2(it.address2, error)
            val bBlock = validBlock(it.block, error)
            val bVillageField = validVillageField(it.block, error)
            val bHouseholdNumber = validHouseholdNumber(it.householdNumber, error)
            if (bPostalCode.and(bCountry).and(bState).and(bDistrict).and(bCityVillage)
                    .and(bAddress1).and(bAddress2).and(bCity)
                    .and(bRelativeAddressOfHf)
                    .and(bAddress1).and(bAddress2).and(bBlock).and(bVillageField)
                    .and(bHouseholdNumber)
            ) block.invoke()
        }
    }

    //
    private fun setFieldEnabledStatus(view: View, isEnabled: Boolean) {
        view.isEnabled = isEnabled
    }

    //
    private fun setupBlocks(districtData: DistData) {
        if (districtData.blocks != null) {
            val adapter: ArrayAdapter<Block> = requireContext().getSpinnerItemAdapter(districtData.blocks)
            binding.autoCompleteBlock.setAdapter(adapter)
            binding.textInputLayBlock.tag = districtData.blocks

            personAddress.address3?.let {
                val selected = languageUtils.getBlock(districtData, it)
                if (selected == null) {
                    val selected = languageUtils.getBlock(districtData, getString(ResourceR.string.lbl_other_block))
                    binding.autoCompleteBlock.setText(selected.toString(), false)
                    binding.textInputOtherBlock.setText(it)
                    enableOtherBlock()
                } else {
                    binding.autoCompleteBlock.setText(selected.toString(), false)
                    disableOtherBlock()
                    setupVillages(selected)
                }
            }

            binding.autoCompleteBlock.setOnItemClickListener { adapterView, _, i, _ ->
                binding.autoCompleteVillageDropdown.setText("", false)
                //patient.cityvillage = binding.autoCompleteVillageDropdown.text.toString()
                binding.textInputLayBlock.hideError()

                binding.textInputCityVillage.setText("")
                binding.textInputOtherBlock.setText("")
                binding.autoCompleteVillageDropdown.setText("")

                val blocksList: List<Block> = binding.textInputLayBlock.tag as List<Block>
                val selectedBlock = blocksList[i]
                if (i == 2) {
                    binding.textInputLayOtherBlock.visibility = View.VISIBLE
                    enableOtherBlock()
                    binding.textInputCityVillage.setText("")
                    personAddress.address3 = binding.textInputOtherBlock.text.toString()
                } else {
                    disableOtherBlock()
                    personAddress.address3 = blocksList[i].name
                    binding.textInputCityVillage.setText("")
                }
                setupVillages(selectedBlock)
            }
        } else {
            eraseAllBlockFields()
        }
    }

    //
    private fun setupVillages(blocksData: Block) {
        val villages = mutableListOf<Village>()

        if (!binding.address?.getVillageWithoutDistrict().isNullOrEmpty()) {
            val selected = languageUtils.getVillage(
                blocksData.gramPanchayats?.get(0),
                binding.address?.getVillageWithoutDistrict()
            )
            if (selected != null) {
                binding.autoCompleteVillageDropdown.setText(selected.toString(), false)
            }
        }

        blocksData.gramPanchayats?.forEach { gramPanchayat ->
            gramPanchayat.villages?.let { villageList ->
                if (villageList.isNotEmpty()) {
                    villages.addAll(villageList)

                    val adapter: ArrayAdapter<Village> = requireContext().getSpinnerItemAdapter(villages)
                    binding.autoCompleteVillageDropdown.setAdapter(adapter)
                    binding.textInputLayVillageDropdown.tag = blocksData.gramPanchayats

                    binding.autoCompleteVillageDropdown.setOnItemClickListener { adapterView, _, i, _ ->
                        binding.textInputLayVillageDropdown.hideError()
                        val selectedVillage = villages[i]
                        if (binding.autoCompleteBlock.text.toString()
                                .equals(getString(ResourceR.string.lbl_other_block), ignoreCase = true)
                        ) binding.textInputCityVillage.setText("")
                        else personAddress.cityVillage = selectedVillage.name
                    }
                } else {
                }
            } ?: run {
            }
        }
    }

//    private fun observeBlockAndVillageChange() {
//        val isBlockEnabled = (binding.addressInfoConfig?.block?.isEnabled);
//        isBlockEnabled?.let {
//            patientViewModel.setCityVillageEnabled(
//                if (isBlockEnabled == true) false
//                else binding.addressInfoConfig?.cityVillage?.isEnabled ?: true
//            )
//        }
//        patientViewModel.addressInfoConfigCityVillageEnabled.observe(
//            viewLifecycleOwner
//        ) { isEnabled ->
//            isCityVillageEnabled = isEnabled
//
//            manageBlockVisibility(isBlockEnabled ?: false)
//        }

//    }

    private fun resetAdaptersAndFieldData() {
        // Reset adapter and clear text for each AutoCompleteTextView
        binding.autoCompleteStateAndProvince.setAdapter(null)
        binding.autoCompleteStateAndProvince.setText("", false)
        binding.address?.state = ""

        binding.autoCompleteDistrict.setAdapter(null)
        binding.autoCompleteDistrict.setText("", false)
        binding.address?.district = ""
    }

    private fun enableOtherBlock() {
        binding.textInputLayOtherBlock.visibility = View.VISIBLE
        binding.llCityVillage.visibility = View.VISIBLE
        binding.llVillageDropdown.visibility = View.GONE
        binding.llOtherBlock.visibility = View.VISIBLE
//        if (isOtherBlockSelected) {
//            binding.lblCityVillage.text =
//                getString(ResourceR.string.hint_city) + "*"
//        }
    }

    private fun disableOtherBlock() {
        binding.textInputLayOtherBlock.visibility = View.GONE
        binding.llCityVillage.visibility = View.GONE
        binding.llVillageDropdown.visibility = View.VISIBLE
        binding.llOtherBlock.visibility = View.GONE
        //binding.lblCityVillage.text = getString(R.string.identification_screen_prompt_city)
    }

    private fun manageBlockVisibility(isBlockEnabled: Boolean) {
        val address3 = binding.address?.address3
        if (isBlockEnabled) {
            binding.llBlock.visibility = View.VISIBLE
            binding.llBlock.isEnabled = true
            if ((!address3.isNullOrEmpty() && isOtherBlockSelected()) || (isOtherBlockSelected())) {
                enableOtherBlock()
            } else {
                disableOtherBlock()
            }
        } else {
            binding.llBlock.visibility = View.GONE
            binding.llBlock.isEnabled = false

//            binding.llCityVillage.visibility = if (isCityVillageEnabled) View.VISIBLE else View.GONE
        }
    }

    private fun setOtherBlockData() {
        if (isOtherBlockSelected()) {
            binding.address?.address3 = binding.textInputOtherBlock.text.toString()
            binding.address?.cityVillage = binding.textInputCityVillage.text.toString()
        }
    }

    private fun isOtherBlockSelected() =
        binding.autoCompleteBlock.text.toString().equals(getString(ResourceR.string.lbl_other_block), ignoreCase = true)

    private fun eraseAllBlockFields() {
        binding.autoCompleteBlock.setText("", false)
        binding.autoCompleteVillageDropdown.setText("", false)
        binding.textInputOtherBlock.setText("")
        binding.autoCompleteBlock.setAdapter(null)
        binding.autoCompleteVillageDropdown.setAdapter(null)
//        if (isOtherBlockSelected)
//            binding.textInputCityVillage.setText("")
//        if (binding.llBlock.isEnabled) {
//            isOtherBlockSelected = false;
//            disableOtherBlock()
//        }
    }
}
