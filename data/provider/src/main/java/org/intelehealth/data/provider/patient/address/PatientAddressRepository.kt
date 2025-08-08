package org.intelehealth.data.provider.patient.address

import org.intelehealth.data.offline.dao.PersonAddressDao
import org.intelehealth.data.offline.entity.PersonAddress
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 17-04-2025 - 13:22.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class PatientAddressRepository @Inject constructor(private val personAddressDao: PersonAddressDao) {
    fun getPatientAddressById(patientId: String) = personAddressDao.getLivePatientAddressByUuid(patientId)

    suspend fun addAddress(patientAddress: PersonAddress) = personAddressDao.add(patientAddress)
}
