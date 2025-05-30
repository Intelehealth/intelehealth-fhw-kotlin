package org.intelehealth.data.provider.sync.data

import android.content.res.Resources.NotFoundException
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.intelehealth.common.service.BaseResponse
import org.intelehealth.common.state.Result
import org.intelehealth.common.utility.DateTimeUtils
import org.intelehealth.common.utility.PreferenceUtils
import org.intelehealth.data.network.KEY_ATTRIBUTE_TYPE_ID
import org.intelehealth.data.network.KEY_ENCOUNTER_ROLE
import org.intelehealth.data.network.KEY_IDENTIFIER
import org.intelehealth.data.network.KEY_PERSON_ID
import org.intelehealth.data.network.KEY_PROVIDER
import org.intelehealth.data.network.KEY_VALUE
import org.intelehealth.data.network.model.PatientIdentifier
import org.intelehealth.data.network.model.SetupLocation
import org.intelehealth.data.network.model.request.PushRequest
import org.intelehealth.data.network.model.response.Person
import org.intelehealth.data.network.model.response.PreferredName
import org.intelehealth.data.network.model.response.PullResponse
import org.intelehealth.data.offline.OfflineDatabase
import org.intelehealth.data.offline.entity.Observation
import org.intelehealth.data.offline.entity.Patient
import org.intelehealth.data.offline.entity.PatientAttribute
import org.intelehealth.data.offline.entity.PersonAddress
import org.intelehealth.data.offline.entity.UnSyncedEncounter
import org.intelehealth.data.offline.entity.Visit
import org.intelehealth.data.offline.entity.VisitAttribute
import org.intelehealth.data.provider.utils.EncounterType
import org.intelehealth.data.provider.utils.PersonIdentifier
import org.intelehealth.data.provider.utils.ProviderRole
import java.time.LocalDate
import java.util.Date
import java.util.TimeZone
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 25-11-2024 - 14:05.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

class SyncDataRepository @Inject constructor(
    private val db: OfflineDatabase,
    private val dataSource: SyncDataSource,
    val preferenceUtils: PreferenceUtils
) {
    fun pullData(
        pageNo: Int,
        pageLimit: Int = 50
    ): Flow<Result<BaseResponse<String, PullResponse>>> {
        val locationStr = preferenceUtils.location
        val location = Gson().fromJson(locationStr, SetupLocation::class.java)
        location?.uuid ?: throw NotFoundException("Location not found")
        return dataSource.pullData(
            preferenceUtils.basicToken,
            location.uuid!!,
            preferenceUtils.lastSyncedTime,
            pageNo,
            pageLimit
        )
    }

    private fun getLocationId(): String {
        val locationStr = preferenceUtils.location
        val location = Gson().fromJson(locationStr, SetupLocation::class.java)
        return location?.uuid ?: throw NotFoundException("Location not found")
    }

    suspend fun saveData(pullResponse: PullResponse, onSaved: suspend (Int, Int) -> Unit) {
        savePatientData(pullResponse)
        saveVisitData(pullResponse)
        saveEncounterData(pullResponse)
        saveObservationData(pullResponse)
        saveLocationData(pullResponse)
        saveProviderData(pullResponse)
//        if (pullResponse.providerAttributeTypeList.isNotEmpty()) ihDb.providerAttributeDao()
//            .insert(pullResponse.providerAttributeTypeList)
//        if (pullResponse.visitAttributeTypeList.isNotEmpty()) ihDb.visitAttributeDao()
//            .insert(pullResponse.visitAttributeTypeList)

        onSaved(pullResponse.totalCount, pullResponse.pageNo)
    }

    private suspend fun saveLocationData(pullResponse: PullResponse) {
        if (pullResponse.locationlist.isNotEmpty()) {
            pullResponse.locationlist.map { it.synced = true }.apply {
                db.patientLocationDao().insert(pullResponse.locationlist)
            }
        }
    }

    private suspend fun saveVisitData(pullResponse: PullResponse) {
        if (pullResponse.visitlist.isNotEmpty()) {
            pullResponse.visitlist.map {
                it.startDate = DateTimeUtils.formatOneToAnother(
                    it.startDate,
                    DateTimeUtils.SERVER_FORMAT,
                    DateTimeUtils.USER_DOB_DB_FORMAT,
                )
                db.visitDao().insert(pullResponse.visitlist)
            }
        }
        if (pullResponse.visitAttributeList.isNotEmpty()) {
            pullResponse.visitAttributeList.map { it.synced = true }.apply {
                db.visitAttributeDao().insert(pullResponse.visitAttributeList)
            }
        }
    }

    private suspend fun saveEncounterData(pullResponse: PullResponse) {
        if (pullResponse.encounterlist.isNotEmpty()) db.encounterDao()
            .insert(pullResponse.encounterlist)
    }

    private suspend fun saveObservationData(pullResponse: PullResponse) {
        if (pullResponse.obslist.isNotEmpty()) {
            pullResponse.obslist.map { it.synced = true }.apply {
                db.observationDao().insert(pullResponse.obslist)
            }
        }
    }

    private suspend fun savePatientData(pullResponse: PullResponse) {
        if (pullResponse.patients.isNotEmpty()) db.patientDao().insert(pullResponse.patients)
        if (pullResponse.patientAttributeTypeListMaster.isNotEmpty() && pullResponse.pageNo == 1) {
            pullResponse.patientAttributeTypeListMaster.map { it.synced = true }.apply {
                db.patientAttrMasterDao().insert(pullResponse.patientAttributeTypeListMaster)
            }
        }
        if (pullResponse.patientAttributesList.isNotEmpty()) {
            pullResponse.patientAttributesList.map { it.synced = true }.apply {
                db.patientAttrDao().insert(pullResponse.patientAttributesList)
            }
        }
    }

    private suspend fun saveProviderData(pullResponse: PullResponse) {
        if (pullResponse.providerlist.isNotEmpty()) {
            pullResponse.providerlist.map { it.synced = true }.apply {
                db.providerDao().insert(pullResponse.providerlist)
            }
        }
        if (pullResponse.providerAttributeList.isNotEmpty()) {
            pullResponse.providerAttributeList.map { it.synced = true }.apply {
                db.providerAttributeDao().insert(pullResponse.providerAttributeList)
            }
        }
    }

    suspend fun pushData() = withContext(Dispatchers.IO) {
        val patients = async { db.patientDao().getAllUnsyncedPatients() }.await()
        val patientAttrs =
            async { db.patientAttrDao().getPatientAttributes(patients.map { it.uuid }) }.await()
        val visits = async { db.visitDao().getAllUnsyncedVisits() }.await()
        val visitAttrs =
            async { db.visitAttributeDao().getVisitAttributes(visits.map { it.uuid }) }.await()
        val encounters = async { db.encounterDao().getAllUnsyncedEncounters() }.await()
        val normalEncounter =
            encounters?.filter { it.encounterTypeUuid != EncounterType.EMERGENCY.value }
        val observations: List<Observation>? = normalEncounter?.let {
            return@let async {
                db.observationDao().getAllUnsyncedObservations(it.map { it.uuid })
            }.await()
        }
        val providers = async { db.providerDao().getAllUnsyncedProviders() }.await()

        val pushRequest = PushRequest(
            patients = mappingPatientsList(patients),
            persons = getPersonWithAttributes(patients, patientAttrs),
            visits = getVisitWithAttributes(visits, visitAttrs),
            encounters = getEncounterWithObservation(encounters, observations),
            providers = providers
        )

        Timber.d { "Encounter: $pushRequest" }
//        return@withContext dataSource.pushData(preferenceUtils.basicToken, pushRequest)
    }

    private fun getEncounterWithObservation(
        encounters: List<UnSyncedEncounter>?,
        observations: List<Observation>?
    ): List<UnSyncedEncounter>? = encounters?.map { encounter ->
        encounter.apply {
            this.observations = observations?.filter { it.encounterUuid == encounter.uuid }
            this.encounterProviders = listOf(
                hashMapOf(
                    KEY_ENCOUNTER_ROLE to ProviderRole.NURSE.value,
                    KEY_PROVIDER to encounter.providerUuid!!
                )
            )
            this.locationId = getLocationId()
        }
    }

    private fun getVisitWithAttributes(
        visits: List<Visit>,
        visitAttrs: List<VisitAttribute>
    ): List<Visit> = visits.map { visit ->
        visit.apply { this.visitAttrs = visitAttrs.filter { it.visitUuid == visit.uuid } }
    }.filter { it.visitAttrs.isNullOrEmpty().not() }

    private fun mappingPatientsList(patients: List<Patient>): List<HashMap<String, Any>>? {
        if (patients.isEmpty()) return null
        return patients.map { patient ->
            mappingPersonIdentifier(person = patient.uuid, identifiers = getPatientIdentifier())
        }
    }

    private fun getPatientIdentifier(): List<PatientIdentifier> = listOf(
        PatientIdentifier(
            locationId = getLocationId(),
            identifierType = PersonIdentifier.IDENTIFIER_OPENMRS_ID.value,
            preferred = true
        )
    )

    private suspend fun getPersonWithAttributes(
        patients: List<Patient>, patientAttrs: List<PatientAttribute>
    ): List<Person> {
        val personList = mutableListOf<Person>()
        return withContext(Dispatchers.IO) {
            patients.forEach { patient ->
                getPerson(
                    patient,
                    patientAttrs
                ).also { personList.add(it) }
            }
            return@withContext personList
        }
    }

    private fun getPerson(patient: Patient, patientAttrs: List<PatientAttribute>) = Person(
        dateOfBirth = patient.dateOfBirth,
        gender = patient.gender,
        uuid = patient.uuid,
        names = getListOfName(patient),
        addresses = getAddresses(patient),
        attributes = patientAttrs.filter {
            it.patientUuid == patient.uuid
        }.map { mappingPatentAttributes(it.value, it.personAttributeTypeUuid) },
    )

    private fun getListOfName(patient: Patient) = listOf(
        PreferredName(
            familyName = patient.lastName,
            givenName = patient.firstName,
            middleName = patient.middleName,
        )
    )

    private fun getAddresses(patient: Patient) = listOf(
        PersonAddress(
            address1 = patient.address1,
            address2 = patient.address2,
            address3 = patient.address3,
            address4 = patient.address4,
            address5 = patient.address5,
            address6 = patient.address6,
            cityVillage = patient.cityVillage,
            district = patient.district,
            state = patient.state,
            postalCode = patient.postalCode,
            country = patient.country
        )
    )

    private fun mappingPatentAttributes(
        value: String?, attributeType: String?
    ): HashMap<String, String> {
        val v = value ?: return hashMapOf()
        val type = attributeType ?: return hashMapOf()
        return hashMapOf(KEY_VALUE to v, KEY_ATTRIBUTE_TYPE_ID to type)
    }

    private fun mappingPersonIdentifier(
        person: String, identifiers: List<PatientIdentifier>
    ): HashMap<String, Any> {
        return hashMapOf(
            KEY_PERSON_ID to person, KEY_IDENTIFIER to identifiers
        )
    }
}
