package org.intelehealth.data.offline.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import org.intelehealth.data.offline.entity.User

@Dao
interface UserDao : CoreDao<User> {

    @Query("SELECT * FROM tbl_user WHERE uuid = :uuid")
    fun getLiveUser(uuid: String): LiveData<User>

    @Query("SELECT * FROM tbl_user WHERE uuid = :uuid")
    suspend fun getUser(uuid: String): User

    @Query("SELECT display_name FROM tbl_user WHERE uuid = :uuid")
    suspend fun getUserName(uuid: String): String

    @Query("UPDATE tbl_user SET last_login_in_time = :lastLoginInTime WHERE uuid = :uuid")
    fun updateLastLoginInTime(lastLoginInTime: String, uuid: String)
}
