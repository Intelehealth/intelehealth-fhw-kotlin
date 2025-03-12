package org.intelehealth.data.offline.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.intelehealth.data.offline.entity.User
import org.intelehealth.data.offline.entity.UserSession

@Dao
interface UserSessionDao : CoreDao<UserSession> {

    @Query(
        "SELECT strftime('%Hh, %Mm', AVG(sessionDuration) / 1000, 'unixepoch') AS formatted_time "
                + "FROM tbl_user_session WHERE userId = :userId"
    )
    fun getOverallAverageSessionDuration(userId: String): Flow<String?>

    @Query(
        "SELECT strftime('%Hh, %Mm', AVG(sessionDuration) / 1000, 'unixepoch') AS formatted_time "
                + "FROM tbl_user_session WHERE userId = :userId "
                + "AND strftime('%Y-%m-%d', startTime / 1000, 'unixepoch') = :date"
    )
    fun getAverageSessionDurationByDate(userId: String, date: String): Flow<String?>

    @Query(
        "SELECT strftime('%Hh, %Mm', AVG(sessionDuration) / 1000, 'unixepoch') AS formatted_time "
                + " FROM tbl_user_session WHERE userId = :userId "
                + " AND (strftime('%Y-%m-%d', startTime / 1000, 'unixepoch') BETWEEN :fromDate AND :toDate)"
    )
    fun getAverageSessionDurationByDateRange(userId: String, fromDate: String, toDate: String): Flow<String?>

//    140ae754-96d1-47fa-a5e8-dd2fe24477b8
}
