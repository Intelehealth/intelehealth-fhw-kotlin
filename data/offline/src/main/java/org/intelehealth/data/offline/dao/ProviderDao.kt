package org.intelehealth.data.offline.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import org.intelehealth.data.offline.entity.Provider
import org.intelehealth.data.offline.entity.VisitDetail

@Dao
interface ProviderDao : CoreDao<Provider> {

    @Query("SELECT * FROM tbl_provider WHERE uuid = :uuid")
    fun getProviderDetails(uuid: String): LiveData<List<Provider>>

    @Query("UPDATE tbl_provider SET given_name = :givenName WHERE uuid = :uuid")
    fun updateProviderGivenName(givenName: String, uuid: String)

    @Query("UPDATE tbl_provider SET family_name = :familyName WHERE uuid = :uuid")
    fun updateProviderFamilyName(familyName: String, uuid: String)

    @Query("UPDATE tbl_provider SET emailId = :emailId WHERE uuid = :uuid")
    fun updateProviderEmailId(emailId: String, uuid: String)

    @Query("UPDATE tbl_provider SET telephone_number = :telephoneNumber WHERE uuid = :uuid")
    fun updateProviderTelephoneNumber(telephoneNumber: String, uuid: String)

    @Query("UPDATE tbl_provider SET date_of_birth = :dateOfBirth WHERE uuid = :uuid")
    fun updateProviderDateOfBirth(dateOfBirth: String, uuid: String)

    @Query("UPDATE tbl_provider SET gender = :gender WHERE uuid = :uuid")
    fun updateProviderGender(gender: String, uuid: String)

    @Query("UPDATE tbl_provider SET imagePath = :imagePath WHERE uuid = :uuid")
    fun updateProviderImagePath(imagePath: String, uuid: String)

    @Query("UPDATE tbl_provider SET country_code = :countryCode WHERE uuid = :uuid")
    fun updateProviderCountryCode(countryCode: String, uuid: String)

    @Query("UPDATE tbl_provider SET voided = :voided WHERE uuid = :uuid")
    fun updateProviderVoided(voided: String, uuid: String)

    @Query("UPDATE tbl_provider SET updated_at = :modifiedDate WHERE uuid = :uuid")
    fun updateProviderModifiedDate(modifiedDate: String, uuid: String)

    @Query("UPDATE tbl_provider SET synced = :sync WHERE uuid = :uuid")
    fun updateProviderSync(sync: String, uuid: String)

    @Query("SELECT * FROM tbl_provider WHERE synced = :synced AND voided = 0")
    fun getAllUnsyncedProviders(synced: Boolean = false): List<Provider>?

    @Query("SELECT ${VisitDetail.PATIENT_AGE} FROM tbl_provider WHERE uuid = :uuid AND voided = 0")
    fun getProviderAge(uuid: String): LiveData<Int?>
}
