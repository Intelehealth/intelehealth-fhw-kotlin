package org.intelehealth.data.offline.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import org.intelehealth.data.offline.entity.PrescriptionStatusCount
import org.intelehealth.data.offline.entity.Visit

@Dao
interface VisitDao : CoreDao<Visit> {

    @Query("SELECT * FROM tbl_visit WHERE synced = '0' OR synced = 'false'")
    fun getUnsyncedVisits(): LiveData<List<Visit>>

    @Query("UPDATE tbl_visit SET synced = :sync WHERE uuid = :visitUuid")
    fun updateVisitSync(visitUuid: String, sync: String)

    @Query("UPDATE tbl_visit SET endDate = :endDate WHERE uuid = :visitUuid")
    fun updateVisitEndDate(visitUuid: String, endDate: String)

    @Query("SELECT patientUuid FROM tbl_visit WHERE uuid = :visitUuid")
    fun getPatientUuidByVisit(visitUuid: String): LiveData<String>

    @Query("SELECT endDate FROM tbl_visit WHERE uuid = :visitUuid")
    fun getVisitEndDate(visitUuid: String): LiveData<String>

    @Query("SELECT uuid FROM tbl_visit WHERE patientUuid = :patientUuid")
    fun getVisitUuidByPatientUuid(patientUuid: String): LiveData<String>

    @Query("SELECT sum(completed) as completed, " + "sum(received) as received, " + "sum(pending) as pending, " + "sum(total) as total from " + "(SELECT CASE WHEN custom.product_category = 'Completed' then custom.total END as completed, " + "CASE WHEN custom.product_category = 'Received' then custom.total END as received, " + "CASE WHEN custom.product_category = 'Pending' then custom.total END as pending, " + "total from (SELECT CASE WHEN uuid in (select visituuid from tbl_encounter where encounter_type_uuid = '629a9d0b-48eb-405e-953d-a5964c88dc30') THEN 'Completed' " + "WHEN uuid in (select visituuid from tbl_encounter where encounter_type_uuid = 'bd1fbfaa-f5fb-4ebd-b75c-564506fc309e') THEN  'Received' " + "WHEN uuid not in (select visituuid from tbl_encounter where encounter_type_uuid = '629a9d0b-48eb-405e-953d-a5964c88dc30') THEN  'Pending' " + "END AS product_category, count(uuid) AS total FROM tbl_visit GROUP BY product_category) as custom)")
    fun getVisitStatusCount(): LiveData<PrescriptionStatusCount>
}