package org.intelehealth.config.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import org.intelehealth.config.room.entity.ActiveFeatureStatus

/**
 * Created by Vaghela Mithun R. on 15-03-2024 - 16:10.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Dao
interface ActiveFeatureStatusDao : CoreDao<ActiveFeatureStatus> {

    @Query("SELECT * FROM tbl_active_feature_status")
    suspend fun getAllRecord(): List<ActiveFeatureStatus>

    @Query("SELECT * FROM tbl_active_feature_status")
    suspend fun getRecord(): ActiveFeatureStatus

    @Query("SELECT * FROM tbl_active_feature_status")
    fun getAllLiveRecord(): LiveData<List<ActiveFeatureStatus>>

    @Query("SELECT * FROM tbl_active_feature_status")
    fun getActiveFeatureStatusLiveRecord(): LiveData<ActiveFeatureStatus>

}
