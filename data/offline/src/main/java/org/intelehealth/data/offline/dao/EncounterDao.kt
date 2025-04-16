package org.intelehealth.data.offline.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import kotlinx.coroutines.flow.Flow
import org.intelehealth.data.offline.entity.Encounter
import org.intelehealth.data.offline.entity.UnSyncedEncounter

/**
 * Created by Vaghela Mithun R. on 02-04-2024 - 10:24.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Dao
interface EncounterDao : CoreDao<Encounter> {

    @Query("SELECT * FROM tbl_encounter WHERE uuid = :uuid")
    fun getEncounterByUuid(uuid: String): LiveData<Encounter>

    @Query("SELECT * FROM tbl_encounter WHERE visituuid = :visitId")
    fun getEncounterByVisitId(visitId: String): LiveData<List<Encounter>>

    @Query("SELECT * FROM tbl_encounter WHERE provider_uuid = :providerId")
    fun getEncounterByProviderId(providerId: String): LiveData<List<Encounter>>

    @Query("SELECT * FROM tbl_encounter WHERE encounter_type_uuid = :encounterTypeId")
    fun getEncounterByTypeId(encounterTypeId: String): LiveData<List<Encounter>>

    @Query("SELECT * FROM tbl_encounter WHERE visituuid = :visitId AND provider_uuid = :providerId")
    fun getProviderVisitEncounters(visitId: String, providerId: String): LiveData<List<Encounter>>

    @Query("SELECT * FROM tbl_encounter WHERE visituuid = :visitId AND encounter_type_uuid = :encounterTypeId")
    fun getVisitEncounterByTypeId(visitId: String, encounterTypeId: String): LiveData<List<Encounter>>

    @Query("SELECT * FROM tbl_encounter WHERE visituuid = :visitId AND provider_uuid = :providerId AND encounter_type_uuid = :encounterTypeId")
    fun getProviderVisitEncounterByTypeId(
        visitId: String,
        providerId: String,
        encounterTypeId: String
    ): LiveData<List<Encounter>>

    @Query(
        "SELECT COUNT( DISTINCT E.visituuid) as completed FROM tbl_encounter E " +
                "INNER JOIN tbl_user U ON U.provider_uuid = E.provider_uuid " +
                "WHERE E.encounter_type_uuid = :encounterTypeId " +
                "AND U.uuid = :userId "
    )
    fun getUserCompletedVisitEncounterCountByTypeId(userId: String, encounterTypeId: String): Flow<Int>

    @Query(
        "SELECT COUNT( DISTINCT E.visituuid) as completed FROM tbl_encounter E " +
                "INNER JOIN tbl_user U ON U.provider_uuid = E.provider_uuid " +
                "WHERE E.encounter_type_uuid = :encounterTypeId " +
                "AND U.uuid = :userId and date(datetime(E.encounter_time)) = :date "
    )
    fun getUserCompletedVisitEncounterCountByDate(userId: String, encounterTypeId: String, date: String): Flow<Int>

    @Query(
        "SELECT COUNT( DISTINCT E.visituuid) as completed FROM tbl_encounter E " +
                "INNER JOIN tbl_user U ON U.provider_uuid = E.provider_uuid " +
                "WHERE E.encounter_type_uuid = :encounterTypeId " +
                "AND U.uuid = :userId and (date(datetime(encounter_time)) BETWEEN :fromDate AND :toDate) "
    )
    fun getUserCompletedVisitEncounterCountByDateRange(
        userId: String,
        encounterTypeId: String,
        fromDate: String,
        toDate: String
    ): Flow<Int>

    //    @RewriteQueriesToDropUnusedColumns
    @Query(
        "SELECT E.visituuid, E.encounter_type_uuid, E.encounter_time, E.provider_uuid, E.updated_at, "
                + "E.uuid, E.created_at, E.synced, E.voided, V.locationuuid as locationId, V.patientuuid as patientId "
                + "FROM tbl_encounter E LEFT JOIN tbl_visit V ON V.uuid = E.visituuid WHERE E.synced = :synced AND E.voided = 0"
    )
    fun getAllUnsyncedEncounters(synced: Boolean = false): List<UnSyncedEncounter>?
}
