package org.intelehealth.config.presenter.fields.data

import org.intelehealth.config.room.dao.PatientVitalDao
import javax.inject.Inject

/**
 * Created by Lincon Pradhan R. on 24-06-2024 - 11:22.
 * Email : lincon@intelehealth.org
 * Mob   :
 **/

class PatientVitalRepository @Inject constructor(private val patientVitalDao: PatientVitalDao) {

    fun getAllEnabledLiveFields() = patientVitalDao.getAllEnabledLiveFields()

    suspend fun getAllEnabledFields() = patientVitalDao.getAllEnabledFields()
}