package org.intelehealth.data.provider.patient.otherinfo

import org.intelehealth.data.offline.dao.PatientAttributeDao
import org.intelehealth.data.offline.entity.PatientOtherInfo
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 17-04-2025 - 13:21.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class PatientOtherDataRepository @Inject constructor(private val patientAttributeDao: PatientAttributeDao) {
    fun getPatientOtherDataById(patientId: String) = patientAttributeDao.getLivePatientOtherDataByUuid(patientId)

    suspend fun updatePatientOtherData(
        patientOtherInfo: PatientOtherInfo
    ) {

    }
}
