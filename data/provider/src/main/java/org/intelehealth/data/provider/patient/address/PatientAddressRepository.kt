package org.intelehealth.data.provider.patient.address

import org.intelehealth.data.offline.dao.PatientDao
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

    suspend fun updatePatientAddress(patientAddress: PersonAddress) = personAddressDao.updatePatientAddress(
        patientId = patientAddress.uuid,
        address1 = patientAddress.address1,
        address2 = patientAddress.address2,
        address3 = patientAddress.address3,
        address4 = patientAddress.address4,
        address5 = patientAddress.address5,
        address6 = patientAddress.address6,
        cityVillage = patientAddress.cityVillage,
        district = patientAddress.district,
        state = patientAddress.state,
        country = patientAddress.country,
        postalCode = patientAddress.postalCode,
        addressOfHf = patientAddress.addressOfHf
    )
}
