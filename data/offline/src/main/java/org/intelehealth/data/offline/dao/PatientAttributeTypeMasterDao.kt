package org.intelehealth.data.offline.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import org.intelehealth.data.offline.entity.PatientAttributeTypeMaster

@Dao
interface PatientAttributeTypeMasterDao : CoreDao<PatientAttributeTypeMaster> {

    @Query("SELECT * FROM tbl_patient_attribute_master WHERE uuid = :uuid")
    fun getAttributeNameByUuid(uuid: String): LiveData<List<PatientAttributeTypeMaster>>

    @Query("SELECT * FROM tbl_patient_attribute_master WHERE name = :name")
    fun getAttributeUuidByName(name: String): LiveData<List<PatientAttributeTypeMaster>>
}