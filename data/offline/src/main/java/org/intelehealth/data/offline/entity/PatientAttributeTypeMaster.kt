package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_patient_attribute_master")
data class PatientAttributeTypeMaster(
    @SerializedName("name") var name: String? = null,
    @ColumnInfo("updated_at") @SerializedName("modified_date") override var updatedAt: String? = null,
) : BaseEntity(), Parcelable {

    companion object Name {
        const val TELEPHONE = "Telephone Number"
        const val ECONOMIC_STATUS = "Economic Status"
        const val EDUCATION = "Education Level"
        const val PROVIDER_ID = "providerUUID"
        const val OCCUPATION = "occupation"
        const val SWD = "Son/wife/daughter"
        const val NATIONAL_ID = "NationalID"
        const val PROFILE_IMG_TIMESTAMP = "ProfileImageTimestamp"
        const val CAST = "Caste"
        const val CREATED_DATE = "createdDate"
        const val TMH_CASE_NUMBER = "TMH Case Number"
        const val REQUEST_ID = "Request ID"
        const val RELATIVE_PHONE_NUMBER = "Relative Phone Number"
        const val DISCIPLINE = "Discipline"
        const val DEPARTMENT = "Department"
        const val PROVINCES = "Provinces"
        const val CITIES = "Cities"
        const val REGISTRATION_ADDRESS_OF_HF = "Registration address of health facility"
        const val INN = "INN"
        const val CODE_OF_HEALTH_FACILITY = "Code of the Health Facility"
        const val HEALTH_FACILITY_NAME = "Health facility name"
        const val CODE_OF_DEPARTMENT = "Code of the Department"
        const val HOUSEHOLD_UUID_LINKING = "HouseHold"
        const val BLOCK = "blockSurvey"

        //household survey attributes
        const val HOUSE_STRUCTURE = "HouseStructure"
        const val RESULT_OF_VISIT = "ResultOfVisit"
        const val NAME_OF_PRIMARY_RESPONDENT = "NamePrimaryRespondent"
        const val REPORT_DATE_OF_PATIENT_CREATED = "ReportDate of patient created"
        const val HOUSEHOLD_NUMBER_OF_SURVEY = "HouseholdNumber"
        const val REPORT_DATE_OF_SURVEY_STARTED = "ReportDate of survey started"

        //second screen
        const val NUMBER_OF_SMARTPHONES = "noOfSmartphones"
        const val NUMBER_OF_FEATURE_PHONES = "noOfFeaturePhones"
        const val HOUSEHOLD_HEAD_NAME = "householdHeadName"
        const val HOUSEHOLD_HEAD_RELIGION = "householdHeadReligion"
        const val HOUSEHOLD_HEAD_CAST = "householdHeadCaste"
        const val NUMBER_OF_EARNING_MEMBERS = "noOfEarningMembers"
        const val PRIMARY_SOURCE_OF_INCOME = "primarySourceOfIncome"

        //third screen
        const val HOUSEHOLD_ELECTRICITY_STATUS = "householdElectricityStatus"
        const val NO_OF_LOAD_SHEDDING_HRS_PER_DAY = "noOfLoadSheddingHrsPerDay"
        const val NO_OF_LOAD_SHEDDING_HRS_PER_WEEK = "noOfLoadSheddingHrsPerWeek"
        const val RUNNING_WATER_STATUS = "runningWaterStatus"
        const val PRIMARY_SOURCE_OF_RUNNING_WATER = "primarySourceOfRunningWater"
        const val WATER_SOURCE_DISTANCE = "waterSourceDistance"
        const val WATER_SUPPLY_AVAILABILITY_HRS_PER_DAY = "waterSupplyAvailabilityHrsPerDay"
        const val WATER_AVAILABILITY_DAYS_PER_WEEK = "waterSupplyAvailabilityDaysperWeek"
        const val HOUSEHOLD_BANK_ACCOUNT_STATUS = "householdBankAccountStatus"

        //fourth screen
        const val HOUSEHOLD_CULTIVABLE_LAND = "householdCultivableLand"
        const val AVERAGE_ANNUAL_HOUSEHOLD_INCOME = "averageAnnualHouseholdIncome"
        const val MONTHLY_FOOD_EXPENDITURE = "monthlyFoodExpenditure"
        const val ANNUAL_HEALTH_EXPENDITURE = "annualHealthExpenditure"
        const val ANNUAL_EDUCATION_EXPENDITURE = "annualEducationExpenditure"
        const val ANNUAL_CLOTHING_EXPENDITURE = "annualClothingExpenditure"
        const val MONTHLY_INTOXICANTS_EXPENDITURE = "monthlyIntoxicantsExpenditure"
        const val HOUSEHOLD_BPL_CARD_STATUS = "householdBPLCardStatus"
        const val HOUSEHOLD_ANTODAYA_CARD_STATUS = "householdAntodayaCardStatus"
        const val HOUSEHOLD_RSBY_CARD_STATUS = "householdRSBYCardStatus"
        const val HOUSEHOLD_MGNREGA_CARD_STATUS = "householdMGNREGACardStatus"

        //fifth screen
        const val COOKING_FUEL_TYPE = "cookingFuelType"
        const val MAIN_LIGHTING_SOURCE = "mainLightingSource"
        const val MAIN_DRINKING_WATER_SOURCE = "mainDrinkingWaterSource"
        const val SAFER_WATER_PROCESS = "saferWaterProcess"
        const val HOUSEHOLD_TOILET_FACILITY = "householdToiletFacility"

        //sixth fragment
        const val HOUSEHOLD_OPEN_DEFECATION_STATUS = "householdOpenDefecationStatus"
        const val FOOD_ITEMS_PREPARED_IN_TWENTY_FOUR_HRS = "foodItemsPreparedInTwentyFourHrs"

        //seventh fragment
        const val SUB_CENTRE_DISTANCE = "subCentreDistance"
        const val NEAREST_PRIMARY_HEALTH_CENTER_DISTANCE = "nearestPrimaryHealthCenterDistance"

        const val NEAREST_COMMUNITY_HEALTH_CENTER_DISTANCE = "nearestCommunityHealthCenterDistance"
        const val NEAREST_DISTRICT_HOSPITAL_DISTANCE = "nearestDistrictHospitalDistance"
        const val NEAREST_PATHOLOGICAL_LAB_DISTANCE = "nearestPathologicalLabDistance"
        const val NEAREST_PRIVATE_CLINIC_MBBS_DOCTOR = "nearestPrivateClinicMBBSDoctor"
        const val NEAREST_PRIVATE_CLINIC_ALTERNATE_MEDICINE = "nearestPrivateClinicAlternateMedicine"
        const val NEAREST_TERTIARY_CARE_FACILITY = "nearestTertiaryCareFacility"
        const val EMERGENCY_CONTACT_NAME = "Emergency Contact Name"
        const val EMERGENCY_CONTACT_NUMBER = "Emergency Contact Number"
        const val EMERGENCY_CONTACT_TYPE = "Emergency Contact Type"

        fun listOfPersonalScreenAttributes() = listOf(
            EMERGENCY_CONTACT_NAME,
            EMERGENCY_CONTACT_NUMBER,
            EMERGENCY_CONTACT_TYPE,
            TELEPHONE,
        )
    }
}
