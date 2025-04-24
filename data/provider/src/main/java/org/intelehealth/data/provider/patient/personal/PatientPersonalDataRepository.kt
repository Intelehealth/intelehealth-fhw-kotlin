package org.intelehealth.data.provider.patient.personal

import org.intelehealth.data.offline.dao.PatientDao
import org.intelehealth.data.offline.entity.Patient
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 20-12-2024 - 20:16.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class PatientPersonalDataRepository @Inject constructor(
    private val patientPersonalDataSource: PatientPersonalDataSource,
    private val patientDao: PatientDao
) {
    fun getPatientById(patientId: String) = patientDao.getLivePatientByUuid(patientId)

    suspend fun insertPatient(patient: Patient) = patientDao.add(patient)

    suspend fun updatePatient(patient: Patient) = patientDao.update(patient)

    fun uploadPatientProfilePicture(
        basicToken: String,
        map: HashMap<String, String>
    ) = patientPersonalDataSource.uploadPatientProfilePicture(basicToken, map)
}
