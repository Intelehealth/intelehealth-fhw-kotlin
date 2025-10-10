package org.intelehealth.app.ui.notification.viewmodel

import android.util.Log
import android.util.Size
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.intelehealth.common.model.CommonHeaderSection
import org.intelehealth.common.model.ListItemFooter
import org.intelehealth.common.model.ListItemHeaderSection
import org.intelehealth.common.state.ListItem
import org.intelehealth.common.state.Result
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.common.utility.CommonConstants
import org.intelehealth.common.utility.CommonConstants.LIMIT
import org.intelehealth.common.utility.CommonConstants.PAGE
import org.intelehealth.common.utility.CommonConstants.SIZE
import org.intelehealth.common.utility.NO_DATA_FOUND
import org.intelehealth.data.offline.entity.NotificationList
import org.intelehealth.data.offline.entity.VisitDetail
import org.intelehealth.data.provider.notification.NotificationRepository
import org.intelehealth.data.provider.prescription.PrescriptionRepository
import org.intelehealth.resource.R
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.LinkedList
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor( private var notificationRepository: NotificationRepository): BaseViewModel() {


    private val _notifications =  MutableLiveData<Result<List<ListItemHeaderSection>>>()
    val notifications: LiveData<Result<List<ListItemHeaderSection>>> = _notifications

    private var notificationPageData = MutableLiveData<List<ListItemHeaderSection>>()
    val notificationLiveData: LiveData<List<ListItemHeaderSection>> = notificationPageData

    var addMoreSectionAdded = false
    private var offset = 0


    fun fetchAllnotification() =
        notificationRepository.fetchAllnotification(PAGE,SIZE).asLiveData()

    fun clearAllNotificationDB() = viewModelScope.launch {
        notificationRepository.clearNotificationDB()
    }
    fun deleteNotificationID(id:Int) = viewModelScope.launch {
        notificationRepository.deleteNotificationID(id)
    }

    fun clearAllNotification() = notificationRepository.clearAllNotification().asLiveData()

    fun insertdata(notifications: List<NotificationList>){
        viewModelScope.launch {
            notificationRepository.insertnotification(notifications)
        }
        getNotificationdata(LIMIT,offset)
    }

    fun splitNotificationsByDate(notifications: List<NotificationList>): Map<String, List<NotificationList>> {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val startOfWeek = today.with(DayOfWeek.MONDAY)
        val startOfMonth = today.withDayOfMonth(1)

        val result = mutableMapOf(
            CommonConstants.TODAY to mutableListOf<NotificationList>(),
            CommonConstants.YESTERDAY to mutableListOf(),
            CommonConstants.THIS_WEEK to mutableListOf(),
            CommonConstants.THIS_MONTH to mutableListOf(),
            CommonConstants.OTHER to mutableListOf(),
        )

        for (notification in notifications) {
            val dateTime = ZonedDateTime.parse(notification.updatedAt).toLocalDate()

            when {
                dateTime.isEqual(today) -> result[CommonConstants.TODAY]?.add(notification)
                dateTime.isEqual(yesterday) -> result[CommonConstants.YESTERDAY]?.add(notification)
                dateTime.isAfter(startOfWeek) || dateTime.isEqual(startOfWeek) -> result[CommonConstants.THIS_WEEK]?.add(notification)
                dateTime.isAfter(startOfMonth) || dateTime.isEqual(startOfMonth) -> result[CommonConstants.THIS_MONTH]?.add(notification)
                else->result[CommonConstants.OTHER]?.add(notification)
            }
        }

        return result
    }

    fun getcategorizedData(notifications: List<NotificationList>): List<ListItemHeaderSection> {
        val notificationList = LinkedList<ListItemHeaderSection>()
        val grouped = splitNotificationsByDate(notifications)
        val today = grouped[CommonConstants.TODAY]
        val yesterday = grouped[CommonConstants.YESTERDAY]
        val thisWeek = grouped[CommonConstants.THIS_WEEK]
        val thisMonth = grouped[CommonConstants.THIS_MONTH]
        val others = grouped[CommonConstants.OTHER]
        if (today!!.isEmpty() && yesterday!!.isEmpty() && thisWeek!!.isEmpty() && thisMonth!!.isEmpty() && 
            others!!.isEmpty()) {
            return emptyList()
        }
        if (today.isNotEmpty()) {
            notificationList.add(CommonHeaderSection(R.string.lbl_today_visits))
            notificationList.addAll(today)
        }
        if (yesterday!!.isNotEmpty()) {
            notificationList.add(CommonHeaderSection(R.string.lbl_yesterday_visits))
            notificationList.addAll(yesterday)
        }

        if (thisWeek!!.isNotEmpty()) {
            notificationList.add(CommonHeaderSection(R.string.lbl_this_week_visits))
            notificationList.addAll(thisWeek)
        }

        if (thisMonth!!.isNotEmpty()) {
            notificationList.add(CommonHeaderSection(R.string.lbl_this_month_visits))
            notificationList.addAll(thisMonth)
        }

        if (others!!.isNotEmpty()) {
            notificationList.add(CommonHeaderSection(R.string.lbl_this_other_visits))
            notificationList.addAll(others)

        }


        return notificationList
    }

    fun getNotificationdata(limit: Int, offset: Int){
        viewModelScope.launch {
            val flow = notificationRepository.getNotifiDatawithOffset(limit,offset)
            mapFlowToResultLiveData(flow)
        }
    }

    fun loadPage() = viewModelScope.launch {
        val flow = notificationRepository.getNotifiDatawithOffset(LIMIT,offset)
        flow.collectLatest {
            if (it.isNotEmpty()) {
                offset += it.size
                notificationPageData.postValue(it)
                if (it.size < LIMIT) {
                    delay(100)
                    notificationPageData.postValue(emptyList())
                }
            } else  notificationPageData.postValue(emptyList())
        }
    }

    private fun mapFlowToResultLiveData(
        flow: Flow<List<NotificationList>>) = viewModelScope.launch {
        flow.onStart {
            _notifications.postValue(Result.Loading("Please wait..."))
        }.catch { e ->
            Timber.e(e) // Log the error
            _notifications.postValue(Result.Error(e.message ?: "Unknown error occurred"))
        }.flowOn(dispatcher).collectLatest { data ->
            if (data.isNotEmpty()) {
                offset = data.size

                val formattedData = getcategorizedData(data)
                val notificationList = LinkedList<ListItemHeaderSection>()
                notificationList.addAll(formattedData)
                if (data.size >= LIMIT) notificationList.addLast(ListItemFooter())
                _notifications.postValue(Result.Success(notificationList, ""))
                } else _notifications.postValue(Result.Fail(NO_DATA_FOUND))

            }
        }
    }

