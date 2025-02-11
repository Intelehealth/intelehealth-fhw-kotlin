package org.intelehealth.data.offline.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import org.intelehealth.data.offline.entity.FollowUpStatusCount
import org.intelehealth.data.offline.entity.Observation
import org.intelehealth.data.offline.entity.PrescriptionStatusCount

/**
 * Created by Vaghela Mithun R. on 02-04-2024 - 10:24.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Dao
interface ObservationDao : CoreDao<Observation> {

    @Query("SELECT * FROM tbl_obs WHERE uuid = :uuid")
    fun getObservationByUuid(uuid: String): LiveData<Observation>

    @Query("SELECT * FROM tbl_obs WHERE encounteruuid = :encounterId")
    fun getObservationByEncounterId(encounterId: String): LiveData<List<Observation>>

    @Query("SELECT * FROM tbl_obs WHERE conceptuuid = :conceptId")
    fun getObservationByConceptId(conceptId: String): LiveData<List<Observation>>

    @Query("SELECT * FROM tbl_obs WHERE creator = :creatorId")
    fun getObservationByCreator(creatorId: String): LiveData<List<Observation>>

    @Query("UPDATE tbl_obs SET comment = :comment WHERE uuid = :uuid")
    suspend fun updateComment(uuid: String, comment: String)

    @Query("UPDATE tbl_obs SET synced = :isSync WHERE uuid = :uuid")
    suspend fun updateSyncStatus(uuid: String, isSync: Boolean)

    @Query("UPDATE tbl_obs SET obsservermodifieddate = :obsModifiedDate WHERE uuid = :uuid")
    suspend fun updateServerModifiedDate(obsModifiedDate: String, uuid: String)

    @Query("UPDATE tbl_obs SET modified_date = :modifiedDate WHERE uuid = :uuid")
    suspend fun updateModifiedDate(modifiedDate: String, uuid: String)

    @Query("UPDATE tbl_obs SET value = :value WHERE uuid = :uuid")
    suspend fun updateValue(uuid: String, value: String)

    @Query("SELECT sum(today) as today, sum(tomorrow) as tomorrow, sum(upcoming) as upcoming, " +
                   "count(follow_up_date) as total FROM (SELECT date(substr(value, 1, 10)) " +
                   "as follow_up_date, CASE WHEN date(substr(value, 1, 10)) = date('now') THEN 1 END " +
                   "as today, CASE WHEN date(substr(value, 1, 10)) = date('now', '+1 day')  THEN 1  END " +
                   "as tomorrow, CASE WHEN date(substr(value, 1, 10)) > date('now', '+1 day') THEN 1 END " +
                   "as upcoming FROM tbl_obs WHERE conceptuuid = :followUpConceptId AND " +
                   "(follow_up_date >= date('now') ) AND encounteruuid not in (select uuid from tbl_encounter " +
                   "WHERE encounter_type_uuid = :visitCloseId) GROUP BY uuid HAVING " +
                   "(follow_up_date is NOT NULL AND LOWER(follow_up_date) != 'no' AND follow_up_date != '' ))")
    fun getFollowUpStatusCount(followUpConceptId: String, visitCloseId: String): LiveData<FollowUpStatusCount>
}
