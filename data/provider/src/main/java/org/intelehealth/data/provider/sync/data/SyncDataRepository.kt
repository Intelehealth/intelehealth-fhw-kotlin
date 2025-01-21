package org.intelehealth.data.provider.sync.data

import org.intelehealth.common.utility.PreferenceUtils
import org.intelehealth.data.network.model.PullResponse
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
        if (pullResponse.visitlist.isNotEmpty()) db.visitDao().insert(pullResponse.visitlist)
        if (pullResponse.encounterlist.isNotEmpty()) db.encounterDao().insert(pullResponse.encounterlist)
        if (pullResponse.obslist.isNotEmpty()) {
            pullResponse.obslist.map { it.synced = true }.apply {
                db.observationDao().insert(pullResponse.obslist)
            }
        }
        if (pullResponse.locationlist.isNotEmpty()) {
            pullResponse.locationlist.map { it.synced = true }.apply {
                db.patientLocationDao().insert(pullResponse.locationlist)
            }
        }
        if (pullResponse.providerlist.isNotEmpty()) {
            pullResponse.providerlist.map { it.synced = true }.apply {
                db.providerDao().insert(pullResponse.providerlist)
            }
        }
//        if (pullResponse.providerAttributeTypeList.isNotEmpty()) ihDb.providerAttributeDao()
//            .insert(pullResponse.providerAttributeTypeList)
        if (pullResponse.providerAttributeList.isNotEmpty()) {
            pullResponse.providerAttributeList.map { it.synced = true }.apply {
                db.providerAttributeDao().insert(pullResponse.providerAttributeList)
            }
        }
//        if (pullResponse.visitAttributeTypeList.isNotEmpty()) ihDb.visitAttributeDao()
//            .insert(pullResponse.visitAttributeTypeList)
        if (pullResponse.visitAttributeList.isNotEmpty()) {
            pullResponse.visitAttributeList.map { it.synced = true }.apply {
                db.visitAttributeDao().insert(pullResponse.visitAttributeList)
            }
        }
        onSaved(pullResponse.totalCount, pullResponse.pageNo)
    }
}