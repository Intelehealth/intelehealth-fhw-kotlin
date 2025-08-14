package org.intelehealth.data.provider.patient.search

import org.intelehealth.common.utility.DateTimeUtils
import org.intelehealth.common.utility.DateTimeUtils.LAST_SYNC_DB_FORMAT
import org.intelehealth.data.offline.dao.PatientAttributeDao
import org.intelehealth.data.offline.dao.PatientDao
import org.intelehealth.data.offline.dao.RecentHistoryDao
import org.intelehealth.data.offline.entity.RecentHistory
import org.intelehealth.data.provider.utils.EncounterType
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 26-05-2025 - 17:23.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class SearchPatientRepository @Inject constructor(
    private val patientDao: PatientDao, private val recentHistoryDao: RecentHistoryDao
) {
    fun searchPatient(searchQuery: String, offset: Int) = patientDao.searchPatient(
        presConceptId = EncounterType.VISIT_COMPLETE.value,
        visitCloseConceptId = EncounterType.PATIENT_EXIT_SURVEY.value,
        emergencyConceptId = EncounterType.EMERGENCY.value,
        searchQuery = searchQuery,
        offset = offset
    )

    fun getRecentSearchHistory() = recentHistoryDao.getRecentHistory(
        tag = RecentHistory.Tag.PATIENT.name, action = RecentHistory.Action.SEARCH.name
    )

    suspend fun addRecentSearchHistory(value: String) {
        recentHistoryDao.add(
            RecentHistory.create(
                tag = RecentHistory.Tag.PATIENT, action = RecentHistory.Action.SEARCH, value = value
            )
        )
    }

    suspend fun updateRecentSearchHistory(recentHistory: RecentHistory) {
        recentHistory.updatedAt = DateTimeUtils.getCurrentDateInUTC(LAST_SYNC_DB_FORMAT)
        recentHistoryDao.update(recentHistory)
    }

    suspend fun deleteRecentSearchHistory() {
        recentHistoryDao.deleteRecentHistory(
            tag = RecentHistory.Tag.PATIENT.name, action = RecentHistory.Action.SEARCH.name
        )
    }
}
