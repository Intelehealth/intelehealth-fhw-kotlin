package org.intelehealth.config.presenter.fields.patient.data

import org.intelehealth.config.room.dao.VitalDao


/**
 * Created by Lincon Pradhan R. on 24-06-2024 - 11:22.
 * Email : lincon@intelehealth.org
 * Mob   :
 **/
class PatientVitalRepository(private val patientVitalDao: VitalDao) {

    fun getAllEnabledLiveFields() = patientVitalDao.getAllEnabledLiveFields()
    suspend fun getAllEnabledFields() = patientVitalDao.getAllEnabledFields()


}