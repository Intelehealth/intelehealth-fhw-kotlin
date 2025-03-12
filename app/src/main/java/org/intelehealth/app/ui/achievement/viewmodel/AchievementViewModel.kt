package org.intelehealth.app.ui.achievement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.ajalt.timberkt.Timber
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
@HiltViewModel
class AchievementViewModel @Inject constructor(private val achievementRepository: AchievementRepository) :
    BaseViewModel() {
    private var overallAchievementData = MutableLiveData<Achievement>()
    val overallAchievementLiveData: LiveData<Achievement> get() = overallAchievementData

    private var dailyAchievementData = MutableLiveData<Achievement>()
    val dailyAchievementLiveData: LiveData<Achievement> get() = dailyAchievementData

    private var dateRangeAchievementData = MutableLiveData<Achievement>()
    val dateRangeAchievementLiveData: LiveData<Achievement> get() = dateRangeAchievementData

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
