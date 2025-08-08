package org.intelehealth.data.offline.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.intelehealth.data.offline.entity.NotificationList

/**
 * Created by Vaghela Mithun R. on 02-04-2024 - 10:24.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Dao
interface LocalNotificationDao : CoreDao<NotificationList> {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notifications: List<NotificationList>)

    @Query("SELECT * FROM tbl_notifications ORDER BY id LIMIT :limit OFFSET :offset")
    fun getNotifiDataWithOffset(limit: Int, offset: Int): Flow<List<NotificationList>>
    
    @Query("SELECT * FROM tbl_notifications WHERE id = :uuid")
    fun getNotificationByUuid(uuid: String): LiveData<NotificationList>

    @Query("SELECT * FROM tbl_notifications")
    suspend fun getAllNotification(): List<NotificationList>

    @Query("SELECT * FROM tbl_notifications")
    fun getAllNotificationData(): Flow<List<NotificationList>>
    
    @Query("SELECT * FROM tbl_notifications WHERE type = :type")
    fun getNotificationByType(type: String): LiveData<NotificationList>

    @Query("DELETE FROM tbl_notifications WHERE userUuid = :user_uuid")
    suspend fun deleteNotification(user_uuid:String)

    @Query("DELETE FROM tbl_notifications WHERE id = :id")
    suspend fun deleteNotificationID(id:Int)

   /* @Query("UPDATE tbl_notifications SET isdeleted = 1 WHERE user_uuid = :uuid")
    suspend fun deleteByUuid(uuid: String)

    @Query("UPDATE tbl_notifications SET isdeleted = 1 WHERE notification_type = :type")
    suspend fun deleteByType(type: String)*/


}