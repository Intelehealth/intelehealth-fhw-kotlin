package org.intelehealth.data.offline.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import org.intelehealth.data.offline.entity.Concept
import org.intelehealth.data.offline.entity.RecentHistory

/**
 * Created by Vaghela Mithun R. on 02-04-2024 - 10:24.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Dao
interface RecentHistoryDao : CoreDao<RecentHistory> {

    @Query("SELECT * FROM tbl_recent_history WHERE tag = :tag AND `action` = :action ORDER BY updated_at DESC LIMIT 5")
    fun getRecentHistory(tag: String, action: String): LiveData<List<RecentHistory>>

    @Query("DELETE FROM tbl_recent_history WHERE tag = :tag AND `action` = :action")
    suspend fun deleteRecentHistory(tag: String, action: String)
}
