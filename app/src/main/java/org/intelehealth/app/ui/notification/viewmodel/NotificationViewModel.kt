package org.intelehealth.app.ui.notification.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import org.intelehealth.common.utility.NO_DATA_FOUND
import org.intelehealth.data.offline.entity.LocalNotification
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

   /* private val _notifications = MutableLiveData<List<LocalNotification>>()
    val notifications: LiveData<List<LocalNotification>> = _notifications*/

    private val _notifications =  MutableLiveData<Result<List<ListItemHeaderSection>>>()
    val notifications: LiveData<Result<List<ListItemHeaderSection>>> = _notifications

    private var notificationPageData = MutableLiveData<List<ListItemHeaderSection>>()
    val notificationLiveData: LiveData<List<ListItemHeaderSection>> = notificationPageData

    var addMoreSectionAdded = false
    private var offset = 0
   init {
     /*  loadNotifications()
       getNotificationList()*/
       insertdata()

    }

    /* fun loadNotifications() {
        viewModelScope.launch {
            val result = notificationRepository.getAllNotification()
            _notifications.postValue(result)
        }
    }*/

    fun insertdata(){
        viewModelScope.launch {
            notificationRepository.insertnotification(getNotificationList())
        }
        getNotificationdata(LIMIT,offset)
    }

    fun getNotificationList():List<LocalNotification>{
        var list = ArrayList<LocalNotification>()
        var id = 556
        for(i in 0 until 20){
            list.add(LocalNotification("${id+i}","Muskan Kala’s prescription has been received!",
                "","2025-07-19T13:25:41.000Z"))
        }
        return list
    }
    /*fun getNotificationList()= listOf(LocalNotification("556","Muskan Kala’s prescription has been received!",
                "","2025-07-19T13:25:41.000Z" ),
                LocalNotification("557","Muskan Kala’s prescription has been received!",
                "","2025-07-21T13:25:41.000Z" ),
                LocalNotification("558","Muskan Kala’s prescription has been received!",
           "","2025-07-09T13:25:41.000Z" ),
                LocalNotification("559","Muskan Kala’s prescription has been received!",
                    "","2025-07-09T13:25:41.000Z" ),
                LocalNotification("560","Muskan Kala’s prescription has been received!",
                    "","2025-07-10T13:25:41.000Z" ))*/




    fun splitNotificationsByDate(notifications: List<LocalNotification>): Map<String, List<LocalNotification>> {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val startOfWeek = today.with(DayOfWeek.MONDAY)
        val startOfMonth = today.withDayOfMonth(1)

        val result = mutableMapOf(
            CommonConstants.TODAY to mutableListOf<LocalNotification>(),
            CommonConstants.YESTERDAY to mutableListOf(),
            CommonConstants.THIS_WEEK to mutableListOf(),
            CommonConstants.THIS_MONTH to mutableListOf(),
            CommonConstants.OTHER to mutableListOf(),
        )

        for (notification in notifications) {
            val dateTime = ZonedDateTime.parse(notification.obsServerModifiedDate).toLocalDate()

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

    fun getcategorizedData(notifications: List<LocalNotification>): List<ListItemHeaderSection> {
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
           /* if (!otherSectionAdded) {
                otherSectionAdded = true
            }*/
        }

       /* if (!addMoreSectionAdded) {
            addMoreSectionAdded = true
            notificationList.add(ListItemFooter())
        }*/
        return notificationList
    }

    fun getNotificationdata(limit: Int, offset: Int){
        viewModelScope.launch {
//            val flow = notificationRepository.getNotificationData()
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
        flow: Flow<List<LocalNotification>>) = viewModelScope.launch {
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

                /*val patients = LinkedList<ListItemHeaderSection>()
                    patients.add(CommonHeaderSection(R.string.lbl_today_visits))
                    patients.addAll(data)
                    if (data.size >= LIMIT) patients.addLast(ListItemFooter())
                    _notifications.postValue(Result.Success(patients, ""))*/

                } else _notifications.postValue(Result.Fail(NO_DATA_FOUND))

            }
        }
    }

