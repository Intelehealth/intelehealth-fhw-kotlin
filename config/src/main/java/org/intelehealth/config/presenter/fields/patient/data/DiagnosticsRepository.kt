package org.intelehealth.config.presenter.fields.patient.data

import org.intelehealth.config.room.dao.PatientDiagnosticsDao
class DiagnosticsRepository(private val diagnosticsDao: PatientDiagnosticsDao) {

    fun getAllEnabledLiveFields() = diagnosticsDao.getAllEnabledLiveFields()
    suspend fun getAllEnabledFields() = diagnosticsDao.getAllEnabledFields()


}