package org.intelehealth.config.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import org.intelehealth.config.room.entity.Vital

/**
 * Created by Vaghela Mithun R. on 15-03-2024 - 16:10.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Dao
interface VitalDao : CoreDao<Vital> {
    //@Query("SELECT * FROM tbl_patient_vital WHERE isEnabled = 1")
    @Query("SELECT * FROM tbl_vital_field")
    fun getAllEnabledLiveFields(): LiveData<List<Vital>>

    @Query("SELECT * FROM tbl_vital_field")
    suspend fun getAllEnabledFields(): List<Vital>

}
