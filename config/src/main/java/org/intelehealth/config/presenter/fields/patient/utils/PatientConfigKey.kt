package org.intelehealth.config.presenter.fields.patient.utils

import org.intelehealth.config.presenter.fields.patient.infoconfig.OtherInfoConfig
import org.intelehealth.config.presenter.fields.patient.infoconfig.PersonalInfoConfig
import org.intelehealth.config.presenter.fields.patient.infoconfig.AddressInfoConfig
import org.intelehealth.config.room.entity.PatientRegistrationFields

/**
 * Created by Vaghela Mithun R. on 27-03-2025 - 13:56.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
object PatientConfigKey {
    //PERSONAL
    const val PROFILE_PHOTO = "p_profile_photo"
    const val FIRST_NAME = "p_first_name"
    const val MIDDLE_NAME = "p_middle_name"
    const val LAST_NAME = "p_last_name"
    const val GENDER = "p_gender"
    const val DOB = "p_date_of_birth"
    const val AGE = "p_age"
    const val PHONE_NUM = "p_phone_number"
    const val GUARDIAN_NAME = "p_guardian_name"
    const val GUARDIAN_TYPE = "p_guardian_type"
    const val EM_CONTACT_NAME = "p_emergency_contact_name"
    const val EM_CONTACT_NUMBER = "p_emergency_contact_number"
    const val EM_CONTACT_TYPE = "p_contact_type"

    //ADDRESS
    const val POSTAL_CODE = "a_postal_address"
    const val COUNTRY = "a_country"
    const val STATE = "a_state"
    const val DISTRICT = "a_district"
    const val VILLAGE_TOWN_CITY = "a_village_town_city"
    const val ADDRESS_1 = "a_corresponding_address_1"
    const val ADDRESS_2 = "a_corresponding_address_2"
    const val PROVINCES = "a_provinces"
    const val CITIES = "a_cities"
    const val REGISTRATION_ADDRESS_OF_HF = "a_registration_address_of_health_facility"
    const val BLOCK = "a_block"
    const val HOUSEHOLD_NUMBER = "a_household_number"

    //OTHERS
    const val NATIONAL_ID = "o_national_id"
    const val OCCUPATION = "o_occupation"
    const val SOCIAL_CATEGORY = "o_social_category"
    const val EDUCATION = "o_education"
    const val ECONOMIC_CATEGORY = "o_economic_category"
    const val TMH_CASE_SUMMARY = "o_tmh_case_number"
    const val REQUEST_ID = "o_request_id"
    const val RELATIVE_PHONE_NUM = "o_relative_phone_number"
    const val DISCIPLINE = "o_discipline"
    const val DEPARTMENT = "o_department"
    const val INN = "o_inn"
    const val CODE_OF_HEALTHY_FACILITY = "o_code_of_the_health_facility"
    const val HEALTH_FACILITY_NAME = "o_health_facility_name"
    const val CODE_OF_DEPARTMENT = "o_code_of_the_department"

    @JvmStatic
    fun buildPatientPersonalInfoConfig(patientRegistrationFields: List<PatientRegistrationFields>): PersonalInfoConfig {
        return PersonalInfoConfig().apply {
            patientRegistrationFields.forEach {
                when (it.idKey) {
                    FIRST_NAME -> firstName = it
                    MIDDLE_NAME -> middleName = it
                    LAST_NAME -> lastName = it
                    DOB -> dob = it
                    AGE -> age = it
                    PHONE_NUM -> phone = it
                    PROFILE_PHOTO -> profilePic = it
                    GENDER -> gender = it
                    GUARDIAN_NAME -> guardianName = it
                    GUARDIAN_TYPE -> guardianType = it
                    EM_CONTACT_NAME -> emergencyContactName = it
                    EM_CONTACT_NUMBER -> emergencyContactNumber = it
                    EM_CONTACT_TYPE -> emergencyContactType = it
                    REQUEST_ID -> requestId = it
                }
            }
        }
    }

    @JvmStatic
    fun buildPatientAddressInfoConfig(patientRegistrationFields: List<PatientRegistrationFields>): AddressInfoConfig {
        return AddressInfoConfig().apply {
            patientRegistrationFields.forEach {
                when (it.idKey) {
                    POSTAL_CODE -> postalCode = it
                    COUNTRY -> country = it
                    STATE -> state = it
                    DISTRICT -> district = it
                    VILLAGE_TOWN_CITY -> cityVillage = it
                    ADDRESS_1 -> address1 = it
                    ADDRESS_2 -> address2 = it
                    PROVINCES -> province = it
                    CITIES -> city = it
                    REGISTRATION_ADDRESS_OF_HF -> registrationAddressOfHf = it
                    BLOCK -> block = it
                    HOUSEHOLD_NUMBER -> householdNumber = it
                }
            }
        }
    }

    @JvmStatic
    fun buildPatientOtherInfoConfig(patientRegistrationFields: List<PatientRegistrationFields>): OtherInfoConfig {
        return OtherInfoConfig().apply {
            patientRegistrationFields.forEach {
                when (it.idKey) {
                    NATIONAL_ID -> nationalId = it
                    OCCUPATION -> occuptions = it
                    SOCIAL_CATEGORY -> socialCategory = it
                    EDUCATION -> education = it
                    ECONOMIC_CATEGORY -> economicCategory = it
                    TMH_CASE_SUMMARY -> tmhCaseNumber = it
                    REQUEST_ID -> requestId = it
                    RELATIVE_PHONE_NUM -> relativePhoneNumber = it
                    DISCIPLINE -> discipline = it
                    DEPARTMENT -> department = it
                    INN -> inn = it
                    CODE_OF_HEALTHY_FACILITY -> codeOfHealthyFacility = it
                    HEALTH_FACILITY_NAME -> healthFacilityName = it
                    CODE_OF_DEPARTMENT -> codeOfDepartment = it
                }
            }
        }
    }
}
