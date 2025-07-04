package org.intelehealth.data.provider.patient.detail

import org.intelehealth.data.offline.dao.PatientAttributeDao
import org.intelehealth.data.offline.dao.PatientDao
import org.intelehealth.data.offline.dao.PersonAddressDao
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 26-05-2025 - 17:23.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class PatientDetailRepository @Inject constructor(
    private val patientDao: PatientDao,
    private val patientAddressDao: PersonAddressDao,
    private val patientAttributeDao: PatientAttributeDao
) {
    fun fetchPatientById(patientId: String) = patientDao.getPatientByUuid(patientId)

    fun fetchPatientAddress(patientId: String) = patientAddressDao.getLivePatientAddressByUuid(patientId)

    fun fetchPatientAttributes(patientId: String) = patientAttributeDao.getAllPatientAttrsData(patientId)
}
