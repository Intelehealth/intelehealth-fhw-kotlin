package org.intelehealth.data.offline.entity

import android.icu.text.UnicodeSet.CASE
import android.os.Build.VERSION_CODES.P
import android.webkit.WebSettings.RenderPriority
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import org.intelehealth.common.model.ListItemHeaderSection
import org.intelehealth.common.utility.CommonConstants.OTHER
import org.intelehealth.common.utility.CommonConstants.THIS_MONTH
import org.intelehealth.common.utility.CommonConstants.THIS_WEEK
import org.intelehealth.common.utility.CommonConstants.TODAY
import org.intelehealth.common.utility.CommonConstants.YESTERDAY


/**
 * Created by Tanvir Hasan on 04-05-25
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

@Entity
data class Prescription(
    @ColumnInfo("uuid")
    var encounterUuid: String? = null,
    @ColumnInfo("visituuid")
    var visitUuid: String? = null,
    @ColumnInfo("patientuuid")
    var patientUuid: String? = null,
    @ColumnInfo("openmrs_id")
    var openmrsId: String? = null,
    @ColumnInfo("first_name")
    var firstName: String? = null,
    @ColumnInfo("middle_name")
    var middleName: String? = null,
    @ColumnInfo("last_name")
    var lastName: String? = null,
    @ColumnInfo("phone_number")
    var phoneNumber: String? = null,
    var gender: String? = null,
    @ColumnInfo("date_of_birth")
    var dob: String? = null,
    @ColumnInfo("startdate")
    var visitStartDate: String? = null,
    var visitSpeciality: String? = null,
    var followupDate: String? = null,
    var sync: String? = null,
    var isPriority: Boolean = false,
    var patientPhoto: String? = null,
    var chiefComplaint: String? = null,
    var section: String? = null,
    /* var hasPrescription: Boolean = false,
     var obsserverModifiedDate: String = ""*/
) : ListItemHeaderSection {
    override fun isHeader(): Boolean = false

    enum class TabType(val value: String) {
        RECEIVED("received"),
        PENDING("pending")
    }

    companion object {
        const val CONDITION_CURRENT_MONTH =
            " V.startdate BETWEEN date('now', 'start of month') AND date('now', 'start of month', '+1 month', '-1 day')"

        const val SELECT_FROM =
            "SELECT V.uuid as visitId, P.uuid as patientId, P.first_name, P.last_name, P.gender, V.startdate, " +
                    "(CASE WHEN P.middle_name IS NULL THEN P.first_name || ' ' || P.last_name || ' ' || P.openmrs_id " +
                    "ELSE P.first_name || ' ' || P.middle_name || ' ' || P.last_name || ' ' || P.openmrs_id  END) searchable, " +
                    "(CASE " +
                    "WHEN V.startdate = date('now') THEN '$TODAY' " +
                    "WHEN V.startdate = date('now', '-1 day') THEN '$YESTERDAY' " +
                    "WHEN V.startdate BETWEEN date('now', '-2 day') AND date('now', '-7 day') THEN '${THIS_WEEK}' " +
                    "WHEN $CONDITION_CURRENT_MONTH THEN '${THIS_MONTH}' " +
                    "ELSE '${OTHER}' " +
                    "END) AS section, 0 AS isPriority " +
                    "FROM tbl_visit V LEFT JOIN tbl_patient P ON P.uuid = V.patientuuid "

        const val SEARCHABLE =
            "(CASE WHEN P.middle_name IS NULL THEN P.first_name || ' ' || P.last_name || ' ' || P.openmrs_id \n" +
                    "ELSE P.first_name || ' ' || P.middle_name || ' ' || P.last_name || ' ' || P.openmrs_id  END) searchable "

    }
}
