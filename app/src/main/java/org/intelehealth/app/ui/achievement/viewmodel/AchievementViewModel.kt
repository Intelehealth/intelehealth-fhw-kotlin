package org.intelehealth.app.ui.achievement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.intelehealth.app.model.achievement.Achievement
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.data.provider.achievement.AchievementRepository
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 03-03-2025 - 18:20.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * ViewModel for managing and providing achievement-related data to the UI.
 *
 * This ViewModel interacts with the [AchievementRepository] to fetch achievement
 * data based on different criteria (overall, daily, date range) and exposes it
 * as LiveData for observation by the UI.
 *
 * @property achievementRepository The repository used to access achievement data.
 */
@HiltViewModel
class AchievementViewModel @Inject constructor(private val achievementRepository: AchievementRepository) :
    BaseViewModel() {
    private var overallAchievementData = MutableLiveData<Achievement>()

    /**
     * LiveData for observing overall achievement data.
     */
    val overallAchievementLiveData: LiveData<Achievement> get() = overallAchievementData

    private var dailyAchievementData = MutableLiveData<Achievement>()

    /**
     * LiveData for observing daily achievement data.
     */
    val dailyAchievementLiveData: LiveData<Achievement> get() = dailyAchievementData

    private var dateRangeAchievementData = MutableLiveData<Achievement>()

    /**
     * LiveData for observing achievement data within a date range.
     */
    val dateRangeAchievementLiveData: LiveData<Achievement> get() = dateRangeAchievementData

    /**
     * Fetches the user's overall achievement data.
     *
     * This method retrieves the total completed visits, patients added,
     * average time spent, and satisfaction score across all time.  It updates
     * [overallAchievementLiveData] with the combined results.
     */
    fun fetchOverallAchievement() {
        val achievement = Achievement(0, 0, 0.0, "0")
        viewModelScope.launch {
            achievementRepository.getUserOverallCompletedVisitCount().collect {
                achievement.completedVisit = it
                overallAchievementData.postValue(achievement)
            }
        }

        viewModelScope.launch {
            achievementRepository.getOverallPatientAddedByUserCount().collect { count ->
                achievement.patientAdded = count
                overallAchievementData.postValue(achievement)
            }
        }

        viewModelScope.launch {
            achievementRepository.getUserOverallAverageTimeSpent().collect { time ->
                time?.let {
                    achievement.timeSpent = time
                    overallAchievementData.postValue(achievement)
                }
            }
        }

        viewModelScope.launch {
            achievementRepository.getUserOverallRatingScore().collect { score ->
                score?.let {
                    achievement.satisfactionScore = it
                    overallAchievementData.postValue(achievement)
                }
            }
        }
    }

    /**
     * Fetches the user's achievement data for a specific date.
     *
     * @param date The date for which to retrieve achievements, in "YYYY-MM-DD" format.
     *
     * This method retrieves the completed visits, patients added, average time spent,
     * and satisfaction score for the given date. It updates [dailyAchievementLiveData]
     * with the combined results.
     */
    fun fetchDailyAchievement(date: String) {
        val achievement = Achievement(0, 0, 0.0, "0")
        viewModelScope.launch {
            achievementRepository.getUserCompletedVisitCountByDate(date).collect {
                achievement.completedVisit = it
                dailyAchievementData.value = achievement
            }
        }

        viewModelScope.launch {
            achievementRepository.getPatientAddedByDateCount(date).collect {
                achievement.patientAdded = it
                dailyAchievementData.value = achievement
            }
        }

        viewModelScope.launch {
            achievementRepository.getUserAverageTimeSpentByDate(date).collect { time ->
                time?.let {
                    achievement.timeSpent = time
                    dailyAchievementData.postValue(achievement)
                }
            }
        }

        viewModelScope.launch {
            achievementRepository.getUserRatingScoreByDate(date).collect { score ->
                score?.let {
                    achievement.satisfactionScore = it
                    dailyAchievementData.postValue(achievement)
                }
            }
        }
    }

    /**
     * Fetches the user's achievement data within a specified date range.
     *
     * @param fromDate The start date of the range, in "YYYY-MM-DD" format.
     * @param toDate The end date of the range, in "YYYY-MM-DD" format.
     *
     * This method retrieves the completed visits, patients added, average time spent,
     * and satisfaction score within the given date range. It updates
     * [dateRangeAchievementLiveData] with the combined results.
     */
    fun fetchDateRangeAchievement(fromDate: String, toDate: String) {
        val achievement = Achievement(0, 0, 0.0, "0")
        viewModelScope.launch {
            achievementRepository.getUserCompletedVisitCountByDataRange(fromDate, toDate).collect {
                achievement.completedVisit = it
                dateRangeAchievementData.value = achievement
            }
        }

        viewModelScope.launch {
            achievementRepository.getPatientAddedByDateRangeCount(fromDate, toDate).collect {
                achievement.patientAdded = it
                dateRangeAchievementData.value = achievement
            }
        }

        viewModelScope.launch {
            achievementRepository.getUserAverageTimeSpentByDateRange(fromDate, toDate).collect { time ->
                time?.let {
                    achievement.timeSpent = time
                    dateRangeAchievementData.postValue(achievement)
                }
            }
        }

        viewModelScope.launch {
            achievementRepository.getUserRatingScoreByDateRange(fromDate, toDate).collect { score ->
                score?.let {
                    achievement.satisfactionScore = it
                    dateRangeAchievementData.postValue(achievement)
                }
            }
        }
    }
}
