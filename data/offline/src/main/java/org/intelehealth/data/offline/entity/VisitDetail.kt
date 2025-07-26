package org.intelehealth.data.offline.entity

import android.os.Parcelable
import android.text.format.DateUtils
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import org.intelehealth.common.model.ListItemHeaderSection
import org.intelehealth.common.utility.CommonConstants.OTHER
import org.intelehealth.common.utility.CommonConstants.THIS_MONTH
import org.intelehealth.common.utility.CommonConstants.THIS_WEEK
import org.intelehealth.common.utility.CommonConstants.TODAY
import org.intelehealth.common.utility.CommonConstants.YESTERDAY
import org.intelehealth.common.utility.DateTimeUtils
import java.util.TimeZone


/**
 * Created by Tanvir Hasan on 04-05-25
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

@Parcelize
@Entity
data class VisitDetail(
    @ColumnInfo("encounterId") var encounterUuid: String? = null,
    @ColumnInfo("visitId") var visitId: String? = null,
    @ColumnInfo("patientId") var patientId: String? = null,
    @ColumnInfo("full_name") var patientFullName: String? = null,
    @ColumnInfo("openmrs_id") var openmrsId: String? = null,
    @ColumnInfo("first_name") var firstName: String? = null,
    @ColumnInfo("middle_name") var middleName: String? = null,
    @ColumnInfo("last_name") var lastName: String? = null,
    @ColumnInfo("phone_number") var phoneNumber: String? = null,
    var gender: String? = null,
    @ColumnInfo("date_of_birth") var dob: String? = null,
    @ColumnInfo("age") var age: Int? = 0,
    @ColumnInfo("patient_created_at") var patientCreatedAt: String? = null,
    @ColumnInfo("startdate") var visitStartDate: String? = null,
    @ColumnInfo("dr_speciality") var drSpeciality: String? = null,
    @ColumnInfo("follow_up") var followupDate: String? = null,
    var sync: String? = null,
    @ColumnInfo("priority") var isPriority: Boolean? = false,
    var patientPhoto: String? = null,
    @ColumnInfo("chief_complain") var chiefComplaint: String? = null,
    var section: String? = null,
    @ColumnInfo("prescribed_date") var prescribedDate: String? = null,
    @ColumnInfo("prescription") var hasPrescription: Boolean? = false,
    @ColumnInfo("has_visit") var hasVisit: Boolean? = false,
    @ColumnInfo("completed") var completed: Boolean? = false,
    @ColumnInfo("doctor_profile") var doctorProfileJson: String? = null,
    @Ignore var visitDate: String? = null,
    @Ignore var visitTime: String? = null,
    @Ignore var doctorProfile: DoctorProfile? = null,
    @Ignore
    var screenView: CardScreenType = CardScreenType.VISIT
) : ListItemHeaderSection, Parcelable {

    override fun toString(): String = Gson().toJson(this)

    override fun isHeader(): Boolean = false

    fun hiddenVisitId(): String {
        visitId ?: return ""
        return "X".repeat(6) + "X${visitId?.takeLast(4)}"
    }

    fun separateVisitDateAndTime() {
        visitStartDate ?: return
        // Example: 2024-07-22T19:13:42.000+0530
        DateTimeUtils.formatDbToDisplay(
            visitStartDate,
            DateTimeUtils.USER_DOB_DB_FORMAT,
            DateTimeUtils.DD_MMM_YYYY_T_HH_MM_A_FORMAT,
        ).apply {
            if (this.isNotEmpty()) {
                val results = this.split("T")
                visitDate = results[0].trim() // "22 Jul 2024"
                visitTime = results[1].trim() // "07:13"
            }
        }
    }

    fun followupDateFormatted(): String {
        return noFollowupContent {
            DateTimeUtils.formatDbToDisplay(
                followupDate,
                DateTimeUtils.YYYY_MM_DD_HYPHEN,
                DateTimeUtils.DD_MMM_FORMAT,
            )
        }
    }

    fun followupFullDateFormatted(): String {
        // Example 2024-07-29, Time: 12:00 PM, Remark: Revisit
        return noFollowupContent {
            DateTimeUtils.formatDbToDisplay(
                followupDate,
                DateTimeUtils.YYYY_MM_DD_HYPHEN,
                DateTimeUtils.DD_MMM_YYYY_FORMAT,
            )
        }
    }

    fun isFollowupPastDue(): Boolean {
        followupDate ?: return false
        if (followupDate == "No") return false
        val followupTime = DateTimeUtils.parseDate(
            followupDate, DateTimeUtils.YYYY_MM_DD_HYPHEN, TimeZone.getDefault()
        )
        return followupTime.before(DateTimeUtils.getCurrentDate(TimeZone.getDefault()))
    }

    private fun noFollowupContent(returnBlock: () -> String): String {
        followupDate ?: return ""
        if (followupDate == "No") return "No Follow Up"
        return returnBlock()
    }

    private fun hasDoctorProfile(): Boolean {
        return !doctorProfileJson.isNullOrEmpty()
    }

    fun extractDoctorProfile() {
        if (hasDoctorProfile()) {
            doctorProfile = Gson().fromJson(doctorProfileJson, DoctorProfile::class.java)
        }
    }

    fun drWhatsappNumberAvailable(): Boolean {
        return hasDoctorProfile() && !doctorProfile?.whatsapp.isNullOrEmpty()
    }

    fun drPhoneNumberAvailable(): Boolean {
        return hasDoctorProfile() && !doctorProfile?.phoneNumber.isNullOrEmpty()
    }

    fun hasFollowup(): Boolean {
        return !followupDate.isNullOrEmpty() && followupDate != "No"
    }

    fun activeEndVisit(): Boolean {
        return hasPrescription == true && completed == false && (!hasFollowup() || isFollowupPastDue())
    }

    fun formatPrescribedDate() {
        prescribedDate ?: return
        DateTimeUtils.parseDate(
            prescribedDate,
            DateTimeUtils.LAST_SYNC_DB_FORMAT, TimeZone.getDefault(),
        ).apply {
            prescribedDate = DateUtils.getRelativeTimeSpanString(
                this.time, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS
            ).toString()
        }
    }

    fun cardDate(): String? {
        return if (screenView == CardScreenType.PATIENT) patientCreatedAt
        else visitStartDate ?: ""
    }

    fun visiblePrescription(): Boolean {
        return hasPrescription == true && screenView == CardScreenType.VISIT && hasVisit == true
    }

    enum class TabType(val value: String) {
        RECEIVED("received"), PENDING("pending")
    }

    companion object {
        const val CONDITION_CURRENT_MONTH =
            " substr(V.startdate, 1, 10) BETWEEN date('now', 'start of month') AND date('now', 'start of month', '+1 month', '-1 day')"

        const val PATIENT_AGE =
            "(CASE WHEN (date_of_birth = '' OR date_of_birth IS NULL ) " + "  THEN " + "  (strftime('%Y', 'now') - strftime('%Y', 'now', '-10 years')) - " + "  (strftime('%m-%d', 'now') < strftime('%m-%d',  'now', '-10 years')) " + "  ELSE " + "  (strftime('%Y', 'now') - strftime('%Y', substr(date_of_birth, 1, 10))) - " + "  (strftime('%m-%d', 'now') < strftime('%m-%d', substr(date_of_birth, 1, 10))) " + "  END) as age "

        const val SEARCHABLE =
            "(CASE WHEN P.middle_name IS NULL THEN P.first_name || ' ' || P.last_name || ' ' || P.openmrs_id " + "ELSE P.first_name || ' ' || P.middle_name || ' ' || P.last_name || ' ' || P.openmrs_id  END) searchable "

        private const val PATIENT_FULL_NAME = "(P.first_name || ' ' || P.last_name ) full_name "

        private const val DURATION_SECTION_TILL_CURRENT_MONTH =
            "(CASE WHEN substr(V.startdate, 1, 10) = date('now') THEN '$TODAY' " +
                    "WHEN substr(V.startdate, 1, 10) = date('now', '-1 day') THEN '$YESTERDAY' " +
                    "WHEN substr(V.startdate, 1, 10) BETWEEN date('now', '-7 day') AND date('now', '-2 day') THEN '${THIS_WEEK}' " +
                    "ELSE '${THIS_MONTH}' END) AS section "

        private const val REQUIRED_COLUMNS =
            " V.uuid as visitId, P.uuid as patientId, P.first_name, P.last_name, P.date_of_birth, P.gender, V.startdate "

        const val COLUMNS_WITH_CURRENT_MONTH_DURATION = " $REQUIRED_COLUMNS, " +
                "$PATIENT_AGE, $PATIENT_FULL_NAME, $SEARCHABLE, $DURATION_SECTION_TILL_CURRENT_MONTH, 0 AS priority "

        private const val OTHER_SECTION_ONLY = "'${OTHER}' AS section "

        const val COLUMN_OTHER_SECTION =
            "$REQUIRED_COLUMNS, $PATIENT_AGE, $PATIENT_FULL_NAME, $SEARCHABLE, $OTHER_SECTION_ONLY, 0 AS priority "

        const val BELOW_CURRENT_MONTH = " substr(V.startdate, 1, 10) < date('now', 'start of month') "

    }

    enum class CardScreenType(val value: String) {
        PATIENT("patient"),
        PRESCRIPTION("prescription"),
        VISIT("visit")
    }
}
