package org.intelehealth.data.provider.patient.otherinfo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.intelehealth.data.offline.dao.PatientAttributeDao
import org.intelehealth.data.offline.dao.PatientAttributeTypeMasterDao
import org.intelehealth.data.offline.entity.PatientAttrWithName
import org.intelehealth.data.offline.entity.PatientAttrWithName.Companion.filterNewlyAddedAttrInEditMode
import org.intelehealth.data.offline.entity.PatientAttribute
import org.intelehealth.data.offline.entity.PatientAttributeTypeMaster
import org.intelehealth.data.offline.entity.PatientOtherInfo
import java.util.UUID
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 17-04-2025 - 13:21.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class PatientOtherDataRepository @Inject constructor(
    private val patientAttributeDao: PatientAttributeDao,
    private val patientAttributeTypeMasterDao: PatientAttributeTypeMasterDao,
) {
    fun getPatientOtherAttrs(patientId: String) = patientAttributeDao.getPatientOtherAttrs(patientId)

    fun getPatientPersonalAttrs(patientId: String) = patientAttributeDao.getPatientPersonalAttrsLiveData(patientId)

    suspend fun createPatientOtherData(patientOtherInfo: PatientOtherInfo) {
        mapWithNameAndGeneratePatientAttributes(patientOtherInfo).apply {
            patientAttributeDao.insert(this)
        }
    }

    private suspend fun updatePatientOtherData(
        patientAttributes: List<PatientAttribute>
    ) = patientAttributeDao.bulkUpdate(patientAttributes)

    private suspend fun getPatientAttributesByNames(
        patientId: String, names: List<String>
    ) = patientAttributeDao.getPatientAttributesByNames(patientId, names)

    suspend fun updateOrInsertPersonalAttributes(
        patientId: String,
        otherInfo: PatientOtherInfo
    ) {
        val attrs = withContext(Dispatchers.IO) {
            async { getPatientPersonalAttributes(patientId, otherInfo) }
        }.await()

        withContext(Dispatchers.IO) { updateOrInsertPatientAttributes(otherInfo, attrs) }
    }

    private suspend fun updateOrInsertPatientAttributes(
        otherInfo: PatientOtherInfo,
        attrs: List<PatientAttrWithName>
    ) {
        val infoSize = otherInfo.getNotNullableAttrsSize()

        if (infoSize > attrs.size) {
            val newlyAdded = withContext(Dispatchers.IO) {
                async { filterNewlyAddedAttrInEditMode(attrs, otherInfo) }
            }.await()

            withContext(Dispatchers.IO) {
                launch { createPatientOtherData(newlyAdded) }
                launch { updatePatientOtherData(attrs) }
            }
        } else {
            updatePatientOtherData(attrs)
        }
    }

    suspend fun updateOrInsertOtherAttributes(
        patientId: String,
        otherInfo: PatientOtherInfo
    ) {
        val attrs = withContext(Dispatchers.IO) {
            async { getPatientOtherAttributes(patientId, otherInfo) }
        }.await()

        withContext(Dispatchers.IO) { updateOrInsertPatientAttributes(otherInfo, attrs) }
    }

    suspend fun getPatientMasterAttributeUuids(): List<PatientAttributeTypeMaster> {
        return patientAttributeTypeMasterDao.getMasterAttributesByNames(
            listOf(
                PatientAttributeTypeMaster.NATIONAL_ID,
                PatientAttributeTypeMaster.PROVIDER_ID,
                PatientAttributeTypeMaster.OCCUPATION,
                PatientAttributeTypeMaster.EDUCATION,
                PatientAttributeTypeMaster.TMH_CASE_NUMBER,
                PatientAttributeTypeMaster.REQUEST_ID,
                PatientAttributeTypeMaster.RELATIVE_PHONE_NUMBER,
                PatientAttributeTypeMaster.DISCIPLINE,
                PatientAttributeTypeMaster.DEPARTMENT,
                PatientAttributeTypeMaster.INN,
                PatientAttributeTypeMaster.HEALTH_FACILITY_NAME,
                PatientAttributeTypeMaster.CODE_OF_DEPARTMENT,
                PatientAttributeTypeMaster.EMERGENCY_CONTACT_NAME,
                PatientAttributeTypeMaster.EMERGENCY_CONTACT_NUMBER,
                PatientAttributeTypeMaster.EMERGENCY_CONTACT_TYPE,
                PatientAttributeTypeMaster.TELEPHONE,
                PatientAttributeTypeMaster.ECONOMIC_STATUS,
                PatientAttributeTypeMaster.SWD,
                PatientAttributeTypeMaster.PROFILE_IMG_TIMESTAMP,
                PatientAttributeTypeMaster.CAST,
                PatientAttributeTypeMaster.CREATED_DATE,
                PatientAttributeTypeMaster.HOUSEHOLD_UUID_LINKING,
                PatientAttributeTypeMaster.HOUSE_STRUCTURE,
                PatientAttributeTypeMaster.RESULT_OF_VISIT,
                PatientAttributeTypeMaster.NAME_OF_PRIMARY_RESPONDENT,
                PatientAttributeTypeMaster.REPORT_DATE_OF_PATIENT_CREATED,
                PatientAttributeTypeMaster.HOUSEHOLD_NUMBER_OF_SURVEY,
                PatientAttributeTypeMaster.REPORT_DATE_OF_SURVEY_STARTED,
                PatientAttributeTypeMaster.NUMBER_OF_SMARTPHONES,
                PatientAttributeTypeMaster.NUMBER_OF_FEATURE_PHONES,
                PatientAttributeTypeMaster.HOUSEHOLD_HEAD_NAME,
                PatientAttributeTypeMaster.HOUSEHOLD_HEAD_RELIGION,
                PatientAttributeTypeMaster.HOUSEHOLD_HEAD_CAST,
                PatientAttributeTypeMaster.NUMBER_OF_EARNING_MEMBERS,
                PatientAttributeTypeMaster.PRIMARY_SOURCE_OF_INCOME,
                PatientAttributeTypeMaster.HOUSEHOLD_ELECTRICITY_STATUS,
                PatientAttributeTypeMaster.NO_OF_LOAD_SHEDDING_HRS_PER_DAY,
                PatientAttributeTypeMaster.NO_OF_LOAD_SHEDDING_HRS_PER_WEEK,
                PatientAttributeTypeMaster.RUNNING_WATER_STATUS,
                PatientAttributeTypeMaster.PRIMARY_SOURCE_OF_RUNNING_WATER,
                PatientAttributeTypeMaster.WATER_SOURCE_DISTANCE,
                PatientAttributeTypeMaster.WATER_SUPPLY_AVAILABILITY_HRS_PER_DAY,
                PatientAttributeTypeMaster.WATER_AVAILABILITY_DAYS_PER_WEEK,
                PatientAttributeTypeMaster.HOUSEHOLD_BANK_ACCOUNT_STATUS,
                PatientAttributeTypeMaster.HOUSEHOLD_CULTIVABLE_LAND,
                PatientAttributeTypeMaster.AVERAGE_ANNUAL_HOUSEHOLD_INCOME,
                PatientAttributeTypeMaster.MONTHLY_FOOD_EXPENDITURE,
                PatientAttributeTypeMaster.ANNUAL_HEALTH_EXPENDITURE,
                PatientAttributeTypeMaster.ANNUAL_EDUCATION_EXPENDITURE,
                PatientAttributeTypeMaster.ANNUAL_CLOTHING_EXPENDITURE,
                PatientAttributeTypeMaster.MONTHLY_INTOXICANTS_EXPENDITURE,
                PatientAttributeTypeMaster.HOUSEHOLD_BPL_CARD_STATUS,
                PatientAttributeTypeMaster.HOUSEHOLD_ANTODAYA_CARD_STATUS,
                PatientAttributeTypeMaster.HOUSEHOLD_RSBY_CARD_STATUS,
                PatientAttributeTypeMaster.HOUSEHOLD_MGNREGA_CARD_STATUS,
                PatientAttributeTypeMaster.HOUSEHOLD_OPEN_DEFECATION_STATUS,
                PatientAttributeTypeMaster.FOOD_ITEMS_PREPARED_IN_TWENTY_FOUR_HRS,
                PatientAttributeTypeMaster.SUB_CENTRE_DISTANCE,
                PatientAttributeTypeMaster.NEAREST_PRIMARY_HEALTH_CENTER_DISTANCE,
                PatientAttributeTypeMaster.NEAREST_COMMUNITY_HEALTH_CENTER_DISTANCE,
                PatientAttributeTypeMaster.NEAREST_DISTRICT_HOSPITAL_DISTANCE,
                PatientAttributeTypeMaster.NEAREST_PATHOLOGICAL_LAB_DISTANCE,
                PatientAttributeTypeMaster.NEAREST_PRIVATE_CLINIC_MBBS_DOCTOR,
                PatientAttributeTypeMaster.NEAREST_PRIVATE_CLINIC_ALTERNATE_MEDICINE,
                PatientAttributeTypeMaster.NEAREST_TERTIARY_CARE_FACILITY,
                PatientAttributeTypeMaster.COOKING_FUEL_TYPE,
                PatientAttributeTypeMaster.MAIN_LIGHTING_SOURCE,
                PatientAttributeTypeMaster.MAIN_DRINKING_WATER_SOURCE,
                PatientAttributeTypeMaster.SAFER_WATER_PROCESS,
                PatientAttributeTypeMaster.HOUSEHOLD_TOILET_FACILITY
            )
        )
    }

    private fun mapWithNameAndGeneratePatientAttributes(
        otherInfo: PatientOtherInfo
    ): List<PatientAttribute> = mutableListOf<PatientAttribute>().apply {
        if (otherInfo.patientId == null) return@apply
        val masterAttributes = otherInfo.patientMasterAttrs

        otherInfo.nationalId?.let { nationalId ->
            if(nationalId.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.NATIONAL_ID }?.uuid,
                    value = nationalId
                )
            )
        }

        otherInfo.providerId?.let { providerId ->
            if(providerId.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.PROVIDER_ID }?.uuid,
                    value = providerId
                )
            )
        }

        otherInfo.telephone?.let { telephone ->
            if(telephone.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.TELEPHONE }?.uuid,
                    value = telephone
                )
            )
        }

        otherInfo.swd?.let { swd ->
            if(swd.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.SWD }?.uuid,
                    value = swd
                )
            )
        }

        otherInfo.economicStatus?.let { economicStatus ->
            if(economicStatus.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.ECONOMIC_STATUS }?.uuid,
                    value = economicStatus
                )
            )
        }

        otherInfo.cast?.let { cast ->
            if(cast.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.CAST }?.uuid,
                    value = cast
                )
            )
        }

        otherInfo.profileImgTimestamp?.let { profileImgTimestamp ->
            if(profileImgTimestamp.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.PROFILE_IMG_TIMESTAMP }?.uuid,
                    value = profileImgTimestamp
                )
            )
        }

        otherInfo.householdUuidLinking?.let { householdUuidLinking ->
            if(householdUuidLinking.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.HOUSEHOLD_UUID_LINKING }?.uuid,
                    value = householdUuidLinking
                )
            )
        }

        otherInfo.block?.let { block ->
            if(block.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.BLOCK }?.uuid,
                    value = block
                )
            )
        }

        otherInfo.occupation?.let { occupation ->
            if(occupation.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.OCCUPATION }?.uuid,
                    value = occupation
                )
            )
        }

        otherInfo.education?.let { education ->
            if(education.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.EDUCATION }?.uuid,
                    value = education
                )
            )
        }

        otherInfo.tmhCaseNumber?.let { tmhCaseNumber ->
            if(tmhCaseNumber.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.TMH_CASE_NUMBER }?.uuid,
                    value = tmhCaseNumber
                )
            )
        }

        otherInfo.requestId?.let { requestId ->
            if(requestId.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.REQUEST_ID }?.uuid,
                    value = requestId
                )
            )
        }

        otherInfo.relativePhoneNumber?.let { relativePhoneNumber ->
            if(relativePhoneNumber.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.RELATIVE_PHONE_NUMBER }?.uuid,
                    value = relativePhoneNumber
                )
            )
        }

        otherInfo.discipline?.let { discipline ->
            if(discipline.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.DISCIPLINE }?.uuid,
                    value = discipline
                )
            )
        }

        otherInfo.department?.let { department ->
            if(department.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.DEPARTMENT }?.uuid,
                    value = department
                )
            )
        }

        otherInfo.inn?.let { inn ->
            if(inn.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.INN }?.uuid,
                    value = inn
                )
            )
        }

        otherInfo.healthFacilityName?.let { healthFacilityName ->
            if(healthFacilityName.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.HEALTH_FACILITY_NAME }?.uuid,
                    value = healthFacilityName
                )
            )
        }

        otherInfo.codeOfDepartment?.let { codeOfDepartment ->
            if(codeOfDepartment.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.CODE_OF_DEPARTMENT }?.uuid,
                    value = codeOfDepartment
                )
            )
        }

        otherInfo.emergencyContactName?.let { emergencyContactName ->
            if(emergencyContactName.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.EMERGENCY_CONTACT_NAME }?.uuid,
                    value = emergencyContactName
                )
            )
        }

        otherInfo.emergencyContactNumber?.let { emergencyContactNumber ->
            if(emergencyContactNumber.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.EMERGENCY_CONTACT_NUMBER }?.uuid,
                    value = emergencyContactNumber
                )
            )
        }

        otherInfo.emergencyContactType?.let { emergencyContactType ->
            if(emergencyContactType.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.EMERGENCY_CONTACT_TYPE }?.uuid,
                    value = emergencyContactType
                )
            )
        }

        otherInfo.createdDate?.let { createdDate ->
            if(createdDate.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.CREATED_DATE }?.uuid,
                    value = createdDate
                )
            )
        }

        otherInfo.provinces?.let { provinces ->
            if(provinces.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.PROVINCES }?.uuid,
                    value = provinces
                )
            )
        }

        otherInfo.cities?.let { cities ->
            if(cities.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.CITIES }?.uuid,
                    value = cities
                )
            )
        }

        otherInfo.registrationAddressOfHf?.let { registrationAddressOfHf ->
            if(registrationAddressOfHf.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.REGISTRATION_ADDRESS_OF_HF }?.uuid,
                    value = registrationAddressOfHf
                )
            )
        }

        otherInfo.codeOfHealthFacility?.let { codeOfHealthFacility ->
            if(codeOfHealthFacility.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.CODE_OF_HEALTH_FACILITY }?.uuid,
                    value = codeOfHealthFacility
                )
            )
        }

        otherInfo.reportDateOfPatientCreated?.let { reportDate ->
            if(reportDate.trim().isEmpty()) return@let
            add(
                createPatientAttribute(
                    patientUuid = otherInfo.patientId,
                    personAttributeTypeUuid = masterAttributes.find { it.name == PatientAttributeTypeMaster.REPORT_DATE_OF_PATIENT_CREATED }?.uuid,
                    value = reportDate
                )
            )
        }
    }

    private fun createPatientAttribute(
        patientUuid: String?,
        personAttributeTypeUuid: String?,
        value: String
    ): PatientAttribute = PatientAttribute(
        patientUuid = patientUuid,
        personAttributeTypeUuid = personAttributeTypeUuid,
        value = value
    ).apply { uuid = UUID.randomUUID().toString() }

    private suspend fun getPatientPersonalAttributes(
        patientId: String,
        otherInfo: PatientOtherInfo
    ): List<PatientAttrWithName> {
        return withContext(Dispatchers.IO) {
            return@withContext async {
                getPatientAttributesByNames(
                    patientId, PatientAttributeTypeMaster.listOfPersonalScreenAttributes()
                )
            }.await().apply {
                PatientAttrWithName.mapToPersonalPatientAttrs(this, otherInfo)
            }
        }
    }

    private suspend fun getPatientOtherAttributes(
        patientId: String,
        otherInfo: PatientOtherInfo
    ): List<PatientAttrWithName> {
        return withContext(Dispatchers.IO) {
            return@withContext async {
                getPatientAttributesByNames(
                    patientId, PatientAttributeTypeMaster.listOfOtherScreenAttributes()
                )
            }.await().apply {
                PatientAttrWithName.mapToOtherPatientAttrs(this, otherInfo)
            }
        }
    }
}
