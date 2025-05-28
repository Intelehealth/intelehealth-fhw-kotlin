package org.intelehealth.data.offline.entity

import android.webkit.WebSettings.RenderPriority
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName


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
   /* var hasPrescription: Boolean = false,
    var obsserverModifiedDate: String = ""*/
)
