package org.intelehealth.data.provider.achievement

import org.intelehealth.common.utility.PreferenceUtils
import org.intelehealth.data.offline.OfflineDatabase
import org.intelehealth.data.provider.utils.EncounterType
import org.intelehealth.data.provider.utils.ObsConcept
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 22-01-2025 - 16:33.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class AchievementRepository @Inject constructor(
    private val database: OfflineDatabase,
    private val preferenceUtils: PreferenceUtils
) {
    fun getOverallPatientAddedByUserCount() = database.patientDao().getPatientCountByCreatorId(preferenceUtils.userId)

    fun getPatientAddedByDateCount(date: String) =
        database.patientDao().getPatientByCreatorIdAndDate(preferenceUtils.userId, date)

    fun getPatientAddedByDateRangeCount(fromDate: String, toDate: String) =
        database.patientDao().getPatientByCreatorIdAndDateRange(preferenceUtils.userId, fromDate, toDate)

    fun getUserOverallCompletedVisitCount() =
        database.encounterDao().getUserCompletedVisitEncounterCountByTypeId(
            preferenceUtils.userId,
            EncounterType.PATIENT_EXIT_SURVEY.value
        )

    fun getUserCompletedVisitCountByDate(date: String) =
        database.encounterDao().getUserCompletedVisitEncounterCountByDate(
            preferenceUtils.userId,
            EncounterType.PATIENT_EXIT_SURVEY.value,
            date
        )

    fun getUserCompletedVisitCountByDataRange(fromDate: String, toDate: String) =
        database.encounterDao().getUserCompletedVisitEncounterCountByDateRange(
            preferenceUtils.userId,
            EncounterType.PATIENT_EXIT_SURVEY.value,
            fromDate, toDate
        )

    fun getUserOverallAverageTimeSpent() =
        database.userSessionDao().getOverallAverageSessionDuration(preferenceUtils.userId)

    fun getUserAverageTimeSpentByDate(date: String) =
        database.userSessionDao().getAverageSessionDurationByDate(preferenceUtils.userId, date)

    fun getUserAverageTimeSpentByDateRange(fromDate: String, toDate: String) =
        database.userSessionDao().getAverageSessionDurationByDateRange(preferenceUtils.userId, fromDate, toDate)

    fun getUserOverallRatingScore() =
        database.observationDao().getOverallUserRatingScore(preferenceUtils.userId, ObsConcept.RATING.value)

    fun getUserRatingScoreByDate(date: String) =
        database.observationDao().getUserRatingScoreByDate(preferenceUtils.userId, ObsConcept.RATING.value, date)

    fun getUserRatingScoreByDateRange(fromDate: String, toDate: String) =
        database.observationDao().getUserRatingScoreByDateRange(
            preferenceUtils.userId,
            ObsConcept.RATING.value,
            fromDate,
            toDate
        )
}
