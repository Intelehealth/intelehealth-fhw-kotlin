package org.intelehealth.data.provider.sync.data

import org.intelehealth.common.utility.PreferenceUtils
import org.intelehealth.data.network.model.response.PullResponse
import org.intelehealth.data.offline.OfflineDatabase
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
    fun pullData(pageNo: Int, pageLimit: Int = 50) = dataSource.pullData(pageNo, pageLimit)

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
        if (pullResponse.visitlist.isNotEmpty()) db.visitDao().insert(pullResponse.visitlist)
        if (pullResponse.visitAttributeList.isNotEmpty()) {
            pullResponse.visitAttributeList.map { it.synced = true }.apply {
                db.visitAttributeDao().insert(pullResponse.visitAttributeList)
            }
        }
    }

    private suspend fun saveEncounterData(pullResponse: PullResponse) {
        if (pullResponse.encounterlist.isNotEmpty()) db.encounterDao().insert(pullResponse.encounterlist)
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
}
