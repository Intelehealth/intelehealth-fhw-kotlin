package org.intelehealth.data.offline.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import org.intelehealth.data.offline.entity.PatientAttribute

@Dao
interface PatientAttributeDao : CoreDao<PatientAttribute> {

    @Query("SELECT * FROM tbl_patient_attribute WHERE patientUuid = :patientUuid")
    fun getAttributesByPatientUuid(patientUuid: String): LiveData<List<PatientAttribute>>

    @Query("SELECT * FROM tbl_patient_attribute WHERE patientUuid = :patientUuid AND person_attribute_type_uuid = :personAttributeTypeUuid")
    fun getAttributesByPatientUuidAndPersonAttributeTypeUuid(
        patientUuid: String, personAttributeTypeUuid: String
    ): LiveData<List<PatientAttribute>>

    @Query("SELECT DISTINCT patientUuid FROM tbl_patient_attribute WHERE value = :value")
    fun getPatientsUuidsByValue(value: String): LiveData<List<String>>

}