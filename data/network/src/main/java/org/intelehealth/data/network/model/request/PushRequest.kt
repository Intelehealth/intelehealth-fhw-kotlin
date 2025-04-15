package org.intelehealth.data.network.model.request

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.intelehealth.data.network.model.response.Person
import org.intelehealth.data.offline.entity.Encounter
import org.intelehealth.data.offline.entity.Patient
import org.intelehealth.data.offline.entity.Provider
import org.intelehealth.data.offline.entity.UnSyncedEncounter
import org.intelehealth.data.offline.entity.Visit

/**
 * Created by - Prajwal W. on 14/10/24.
 * Email: prajwalwaingankar@gmail.com
 * Mobile: +917304154312
 **/
data class PushRequest(
    @SerializedName("appointments")
    private var appointments: List<BookAppointmentRequest>? = null,

    @SerializedName("persons")
    private var persons: List<Person>? = null,

    @SerializedName("patients")
    private var patients: List<HashMap<String, Any>>? = null,

    @SerializedName("visits")
    private var visits: List<Visit>? = null,

    @SerializedName("encounters")
    private var encounters: List<UnSyncedEncounter>? = null,

    @SerializedName("providers")
    private var providers: List<Provider>? = null
)

data class BookAppointmentRequest(
    @SerializedName("uuid")
    private var uuid: String? = null,

    @SerializedName("appointmentId")
    private var appointmentId: Int? = null,

    @SerializedName("slotDay")
    private var slotDay: String? = null,

    @SerializedName("slotDate")
    private var slotDate: String?,

    @SerializedName("slotDuration")
    private var slotDuration: Int? = null,

    @SerializedName("slotDurationUnit")
    private var slotDurationUnit: String? = null,

    @SerializedName("slotTime")
    private var slotTime: String? = null,

    @SerializedName("speciality")
    private var speciality: String? = null,

    @SerializedName("userUuid")
    private var userUuid: String? = null,

    @SerializedName("drName")
    private var drName: String? = null,

    @SerializedName("visitUuid")
    private var visitUuid: String? = null,

    @SerializedName("patientName")
    private var patientName: String? = null,

    @SerializedName("openMrsId")
    private var openMrsId: String? = null,

    @SerializedName("patientId")
    private var patientId: String? = null,

    @SerializedName("locationUuid")
    private var locationUuid: String? = null,

    @SerializedName("hwUUID")
    private var hwUUID: String? = null,

    @SerializedName("reason")
    private var reason: String? = null,

    @SerializedName("patientAge")
    @Expose
    private var patientAge: String? = null,

    @SerializedName("patientGender")
    @Expose
    private var patientGender: String? = null,

    @SerializedName("patientPic")
    @Expose
    private var patientPic: String? = null,

    @SerializedName("hwName")
    @Expose
    private var hwName: String? = null,

    @SerializedName("hwAge")
    @Expose
    private var hwAge: String? = null,

    @SerializedName("hwGender")
    @Expose
    private var hwGender: String? = null,

    @SerializedName("voided")
    @Expose
    private var voided: String? = null,

    @SerializedName("sync")
    @Expose
    private var sync: String? = null
) {
    override fun toString(): String = Gson().toJson(this)
}
