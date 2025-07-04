package org.intelehealth.data.offline.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.intelehealth.common.utility.CommonConstants
import org.intelehealth.data.offline.entity.PatientAttributeTypeMaster
import org.intelehealth.data.offline.entity.PersonAddress
import org.intelehealth.data.offline.entity.VisitDetail
import org.intelehealth.data.offline.entity.VisitDetail.Companion.SEARCHABLE

/**
 * Created by Vaghela Mithun R. on 02-04-2024 - 10:24.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Dao
interface PersonAddressDao : CoreDao<PersonAddress> {

    @Query("SELECT ${PersonAddress.ADDRESS_FIELDS} FROM tbl_person_address WHERE uuid = :patientId")
    fun getLivePatientAddressByUuid(patientId: String): LiveData<PersonAddress>

    @Query("SELECT ${PersonAddress.ADDRESS_FIELDS} FROM tbl_person_address WHERE synced = 0 AND voided = 0")
    fun getAllUnsyncedPatientAddress(): List<PersonAddress>

    @Query(
        "UPDATE tbl_person_address SET address1 = :address1, address2 = :address2, address3 = :address3, " +
                " address4 = :address4, address5 = :address5, address6 = :address6, city_village = :cityVillage, " +
                " district = :district, state = :state, country = :country, postal_code = :postalCode, " +
                " addressOfHf = :addressOfHf WHERE uuid = :patientId"
    )
    suspend fun updatePatientAddress(
        patientId: String,
        address1: String?,
        address2: String?,
        address3: String?,
        address4: String?,
        address5: String?,
        address6: String?,
        cityVillage: String?,
        district: String?,
        state: String?,
        postalCode: String?,
        country: String?,
        addressOfHf: String?
    ): Int
}
