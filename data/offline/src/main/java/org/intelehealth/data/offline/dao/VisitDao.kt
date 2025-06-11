package org.intelehealth.data.offline.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.intelehealth.common.utility.CommonConstants
import org.intelehealth.data.offline.entity.VisitDetail
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

    @Query(
        "SELECT sum(completed) as completed, sum(received) as received, sum(pending) as pending, "
                + " sum(total) as total from (SELECT CASE WHEN custom.product_category = 'Completed'"
                + " then custom.total END as completed, CASE WHEN custom.product_category = 'Received'"
                + " then custom.total END as received, CASE WHEN custom.product_category = 'Pending'"
                + " then custom.total END as pending, total from "
                + " (SELECT CASE WHEN uuid in (select visituuid from tbl_encounter where "
                + " encounter_type_uuid =:visitSurveyExitId) THEN 'Completed' "
                + " WHEN uuid in (select visituuid from tbl_encounter where encounter_type_uuid =:visitCompletedId) "
                + " THEN  'Received' " + "WHEN uuid not in (select visituuid from tbl_encounter where "
                + " encounter_type_uuid in (:visitSurveyExitId, :visitCompletedId)) THEN  'Pending' "
                + " END AS product_category, count(uuid) AS total FROM tbl_visit GROUP BY product_category) as custom)"
    )
    fun getVisitStatusCount(
        visitCompletedId: String,
        visitSurveyExitId: String
    ): LiveData<PrescriptionStatusCount>

    @Query("SELECT * FROM tbl_visit WHERE synced = :synced AND voided = 0")
    fun getAllUnsyncedVisits(synced: Boolean = false): List<Visit>

//    @Query(
//        "SELECT COUNT(DISTINCT p.openmrs_id) " +
//                "FROM tbl_patient p " +
//                "JOIN tbl_visit v ON p.uuid = v.patientuuid " +
//                "JOIN tbl_encounter e ON v.uuid = e.visituuid " +
//                "JOIN tbl_obs o ON e.uuid = o.encounteruuid " +
//                "WHERE e.encounter_type_uuid = :visitCompleteType " +
//                "AND (o.synced = 1 OR o.synced = 'TRUE' OR o.synced = 'true') " +
//                "AND o.voided = 0"
//    )
//    fun getPrescriptionCount(
//        visitCompleteType: String
//    ): LiveData<Int>
//
//    @Query(
//        "SELECT p.first_name, p.middle_name, p.last_name, p.openmrs_id, p.date_of_birth, "
//                + "p.gender, v.startdate, v.patientuuid, e.visituuid, e.uuid AS euid, o.uuid AS ouid, "
//                + "o.server_updated_at,o.synced AS osync, "
//                + "(SELECT uuid FROM tbl_encounter where visituuid = e.visituuid "
//                + "AND encounter_type_uuid =:emergencyEnType "
//                + " AND voided='0' COLLATE NOCASE) as isPriority "
//                + "FROM tbl_patient p  "
//                + "JOIN tbl_visit v ON p.uuid = v.patientuuid "
//                + "JOIN tbl_encounter e ON v.uuid = e.visituuid "
//                + "JOIN tbl_obs o ON e.uuid = o.encounteruuid "
//                + "WHERE e.encounter_type_uuid = :visitCompleteType "
//                + "AND (o.synced = 1 OR LOWER(o.synced) = 'true') "
//                + "AND o.voided = 0  "
//                + "AND v.startdate > DATETIME('now', '-4 day') "
//                + "AND (SELECT COUNT(*) FROM tbl_encounter e1 WHERE " +
//                " e1.visituuid = v.uuid AND e1.encounter_type_uuid = :exitSurveyEnType ) <= 0 "
//                + "AND (SELECT COUNT(*) FROM tbl_encounter e2 WHERE " +
//                " e2.visituuid = v.uuid AND e2.encounter_type_uuid = :visitCompleteType) > 0 "
//                + "AND (p.first_name LIKE '%' || :searchQuery || '%' "
//                + "OR p.middle_name LIKE '%' || :searchQuery || '%' "
//                + "OR p.last_name LIKE '%' || :searchQuery || '%' "
//                + "OR p.openmrs_id LIKE '%' || :searchQuery || '%')"
//                + "GROUP BY p.openmrs_id "
//                + "ORDER BY v.startdate DESC "
//                + "LIMIT :limit OFFSET :offset"
//    )
//    fun getRecentReceivedPrescriptions(
//        visitCompleteType: String?,
//        exitSurveyEnType: String?,
//        emergencyEnType: String?,
//        searchQuery: String?,
//        limit: Int,
//        offset: Int
//    ): List<Prescription>?
//
//    @Query(
//        "SELECT p.first_name, p.middle_name, p.last_name, p.openmrs_id, p.date_of_birth, "
//                + "p.gender, v.startdate, v.patientuuid, e.visituuid, e.uuid AS euid, o.uuid AS ouid, "
//                + "o.server_updated_at,o.synced AS osync, "
//                + "(SELECT uuid FROM tbl_encounter where visituuid = e.visituuid "
//                + "AND encounter_type_uuid =:emergencyEnType "
//                + " AND voided='0' COLLATE NOCASE) as isPriority "
//                + "FROM tbl_patient p  "
//                + "JOIN tbl_visit v ON p.uuid = v.patientuuid "
//                + "JOIN tbl_encounter e ON v.uuid = e.visituuid "
//                + "JOIN tbl_obs o ON e.uuid = o.encounteruuid "
//                + "WHERE e.encounter_type_uuid = :visitCompleteType "
//                + "AND (o.synced = 1 OR LOWER(o.synced) = 'true') "
//                + "AND o.voided = 0  "
//                + "AND v.startdate <= DATETIME('now', '-4 day') "
//                + "AND (SELECT COUNT(*) FROM tbl_encounter e1 WHERE " +
//                " e1.visituuid = v.uuid AND e1.encounter_type_uuid = :exitSurveyEnType ) <= 0 "
//                + "AND (SELECT COUNT(*) FROM tbl_encounter e2 WHERE " +
//                " e2.visituuid = v.uuid AND e2.encounter_type_uuid = :visitCompleteType) > 0 "
//                + "AND (p.first_name LIKE '%' || :searchQuery || '%' "
//                + "OR p.middle_name LIKE '%' || :searchQuery || '%' "
//                + "OR p.last_name LIKE '%' || :searchQuery || '%' "
//                + "OR p.openmrs_id LIKE '%' || :searchQuery || '%')"
//                + "GROUP BY p.openmrs_id "
//                + "ORDER BY v.startdate DESC "
//                + "LIMIT :limit OFFSET :offset"
//    )
//    fun getOlderReceivedPrescriptions(
//        visitCompleteType: String?,
//        exitSurveyEnType: String?,
//        emergencyEnType: String?,
//        searchQuery: String?,
//        limit: Int,
//        offset: Int
//    ): List<Prescription>?
//
//
//    @Query(
//        "SELECT p.first_name, p.middle_name, p.last_name, p.openmrs_id, p.date_of_birth, "
//                + "p.gender, v.startdate, v.patientuuid, e.visituuid, e.uuid AS euid, o.uuid AS ouid, "
//                + "o.server_updated_at,o.synced AS osync , 0 AS isPriority "
//                + "FROM tbl_patient p  "
//                + "JOIN tbl_visit v ON p.uuid = v.patientuuid "
//                + "JOIN tbl_encounter e ON v.uuid = e.visituuid "
//                + "JOIN tbl_obs o ON e.uuid = o.encounteruuid "
//                + "WHERE (o.synced = 1 OR LOWER(o.synced) = 'true') "
//                + "AND o.voided = 0 "
//                + "AND v.startdate > DATETIME('now', '-4 day') "
//                + "AND (SELECT COUNT(*) FROM tbl_encounter e1 WHERE " +
//                " e1.visituuid = v.uuid AND e1.encounter_type_uuid = :exitSurveyEnType ) <= 0 "
//                + "AND (SELECT COUNT(*) FROM tbl_encounter e2 WHERE " +
//                " e2.visituuid = v.uuid AND e2.encounter_type_uuid = :visitCompleteType) <= 0 "
//                + "AND (p.first_name LIKE '%' || :searchQuery || '%' "
//                + "OR p.middle_name LIKE '%' || :searchQuery || '%' "
//                + "OR p.last_name LIKE '%' || :searchQuery || '%' "
//                + "OR p.openmrs_id LIKE '%' || :searchQuery || '%')"
//                + "GROUP BY p.openmrs_id "
//                + "ORDER BY v.startdate DESC "
//                + "LIMIT :limit OFFSET :offset"
//    )
//    fun getRecentPendingPrescriptions(
//        visitCompleteType: String?,
//        exitSurveyEnType: String?,
//        searchQuery: String?,
//        limit: Int,
//        offset: Int
//    ): List<Prescription>?
//
//    @Query(
//        "SELECT p.first_name, p.middle_name, p.last_name, p.openmrs_id, p.date_of_birth,"
//                + "p.gender, v.startdate, v.patientuuid, e.visituuid, e.uuid AS euid, o.uuid AS ouid, "
//                + "o.server_updated_at,o.synced AS osync, 0 AS isPriority "
//                + "FROM tbl_patient p  "
//                + "JOIN tbl_visit v ON p.uuid = v.patientuuid "
//                + "JOIN tbl_encounter e ON v.uuid = e.visituuid "
//                + "JOIN tbl_obs o ON e.uuid = o.encounteruuid "
//                + "WHERE (o.synced = 1 OR LOWER(o.synced) = 'true') "
//                + "AND o.voided = 0  "
//                + "AND v.startdate <= DATETIME('now', '-4 day') "
//                + "AND (SELECT COUNT(*) FROM tbl_encounter e1 WHERE " +
//                " e1.visituuid = v.uuid AND e1.encounter_type_uuid = :exitSurveyEnType ) <= 0 "
//                + "AND (SELECT COUNT(*) FROM tbl_encounter e2 WHERE " +
//                " e2.visituuid = v.uuid AND e2.encounter_type_uuid = :visitCompleteType) <= 0 "
//                + "AND (p.first_name LIKE '%' || :searchQuery || '%' "
//                + "OR p.middle_name LIKE '%' || :searchQuery || '%' "
//                + "OR p.last_name LIKE '%' || :searchQuery || '%' "
//                + "OR p.openmrs_id LIKE '%' || :searchQuery || '%')"
//                + "GROUP BY p.openmrs_id "
//                + "ORDER BY v.startdate DESC "
//                + "LIMIT :limit OFFSET :offset"
//    )
//    fun getOlderPendingPrescriptions(
//        visitCompleteType: String?,
//        exitSurveyEnType: String?,
//        searchQuery: String?,
//        limit: Int,
//        offset: Int
//    ): List<Prescription>?

    @Query(
        "${VisitDetail.SELECT_FROM} " +
                "WHERE V.uuid in (SELECT visituuid FROM tbl_encounter WHERE encounter_type_uuid = :visitCompleteType) " +
                "AND ${VisitDetail.CONDITION_CURRENT_MONTH} AND searchable LIKE '%' || :searchQuery || '%' " +
                "AND patientId IS NOT NULL ORDER BY V.startdate DESC"
    )
    fun getCurrentMonthReceivedPrescriptions(
        visitCompleteType: String,
        searchQuery: String = ""
    ): Flow<List<VisitDetail>>

    @Query(
        "${VisitDetail.SELECT_FROM} " +
                "WHERE V.uuid in (SELECT visituuid FROM tbl_encounter WHERE encounter_type_uuid = :visitCompleteType) " +
                "AND patientId IS NOT NULL AND searchable LIKE '%' || :searchQuery || '%' " +
                "ORDER BY V.startdate DESC LIMIT ${CommonConstants.LIMIT} OFFSET :offset"
    )
    fun getReceivedPrescriptionsWithPaging(
        visitCompleteType: String?,
        searchQuery: String = "",
        offset: Int
    ): Flow<List<VisitDetail>>

    @Query(
        "${VisitDetail.SELECT_FROM} " +
                "WHERE V.uuid in (SELECT visituuid FROM tbl_encounter WHERE encounter_type_uuid NOT IN (:visitCompleteType, :exitSurveyEnType)) " +
                "AND ${VisitDetail.CONDITION_CURRENT_MONTH} AND searchable LIKE '%' || :searchQuery || '%' " +
                "AND patientId IS NOT NULL ORDER BY V.startdate DESC"
    )
    fun getCurrentMonthPendingPrescriptions(
        visitCompleteType: String?,
        exitSurveyEnType: String?,
        searchQuery: String = ""
    ): Flow<List<VisitDetail>>

    @Query(
        "${VisitDetail.SELECT_FROM} " +
                "WHERE V.uuid in (SELECT visituuid FROM tbl_encounter WHERE encounter_type_uuid NOT IN (:visitCompleteType, :exitSurveyEnType)) " +
                "AND patientId IS NOT NULL AND searchable LIKE '%' || :searchQuery || '%' " +
                "ORDER BY V.startdate DESC LIMIT ${CommonConstants.LIMIT} OFFSET :offset"
    )
    fun getPendingPrescriptionsWithPaging(
        visitCompleteType: String?,
        exitSurveyEnType: String?,
        searchQuery: String = "",
        offset: Int
    ): Flow<List<VisitDetail>>


    @Query(
        "SELECT V.uuid as visitId, V.patientuuid as patientId, P.first_name, P.last_name, P.middle_name, P.gender, " +
                "(P.first_name || ' ' || P.last_name ) full_name, ${VisitDetail.PATIENT_AGE}, " +
                "P.openmrs_id, P.date_of_birth, PA.value as phone_number, V.startdate, O.value as chief_complain, " +
                "VA.value as dr_speciality, " +
                "MAX(CASE WHEN E.encounter_type_uuid  = :visitCompleteEnType THEN 1 ELSE 0 END) prescription, " +
                "MAX(CASE WHEN E.encounter_type_uuid  = :exitSurveyEnType THEN 1 ELSE 0 END) completed, " +
                "MAX(CASE WHEN E.encounter_type_uuid  = :emergencyEnType THEN 1 ELSE 0 END) priority, " +
                "MAX(CASE WHEN E.encounter_type_uuid  = :visitCompleteEnType THEN O.value END) doctor_profile, " +
                "MAX(CASE WHEN (E.encounter_type_uuid  = :visitCompleteEnType " +
                "OR E.encounter_type_uuid  = :adultInitialEnType) THEN O.server_updated_at END) prescribed_date, " +
                "MAX(CASE WHEN O.conceptuuid = :followUpConceptId THEN O.value END) follow_up " +
                "FROM tbl_visit V  " +
                "LEFT JOIN tbl_encounter E ON E.visituuid = V.uuid " +
                "LEFT JOIN tbl_obs O ON O.encounteruuid = E.uuid " +
                "LEFT JOIN tbl_patient P ON P.uuid = V.patientuuid " +
                "LEFT JOIN tbl_patient_attribute PA ON PA.patient_uuid = P.uuid " +
                "LEFT JOIN tbl_patient_attribute_master PAM ON PAM.uuid = PA.person_attribute_type_uuid " +
                "LEFT JOIN tbl_visit_attribute VA ON VA.visit_uuid = V.uuid " +
                "WHERE V.uuid = :visitId " +
                "AND VA.visit_attribute_type_uuid = :specialityAttrType " +
                "AND PAM.name = :patientAttrName AND V.synced = 1 AND V.voided = 0 " +
                "GROUP BY V.uuid"
    )
    fun getVisitDetailsByUuid(
        visitId: String,
        visitCompleteEnType: String,
        exitSurveyEnType: String,
        emergencyEnType: String,
        adultInitialEnType: String,
        followUpConceptId: String,
        specialityAttrType: String,
        patientAttrName: String
    ): LiveData<VisitDetail>
}
