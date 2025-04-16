package org.intelehealth.data.offline.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import org.intelehealth.data.offline.entity.PatientLocation

@Dao
interface PatientLocationDao : CoreDao<PatientLocation> {

    @Query("SELECT * FROM tbl_location WHERE locationUuid = :uuid")
    fun getAllPatientsByLocationUuid(uuid: String): LiveData<List<PatientLocation>>

}