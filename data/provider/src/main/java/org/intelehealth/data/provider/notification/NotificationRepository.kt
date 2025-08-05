package org.intelehealth.data.provider.notification

import org.intelehealth.common.utility.PreferenceUtils
import org.intelehealth.data.offline.dao.LocalNotificationDao
import org.intelehealth.data.offline.entity.NotificationList
import javax.inject.Inject

class NotificationRepository @Inject constructor(private val notificationDao: LocalNotificationDao,
                                                 private val dataSource: NotificationDataSource,
                                                 private val preferenceUtils: PreferenceUtils
) {

     suspend fun getAllNotification(): List<NotificationList> {
          return notificationDao.getAllNotification()
     }
     suspend fun insertnotification(notifications: List<NotificationList>)=
          notificationDao.insertAll(notifications)

     fun getNotificationData() = notificationDao.getAllNotificationData()

     fun getNotifiDatawithOffset(limit: Int, offset: Int) = notificationDao.getNotifiDataWithOffset(limit,offset)


     fun fetchAllnotification( page: String, size: String) = dataSource.fetchAllnotification(
          preferenceUtils.basicToken.let {
               val token = it.split(" ")[1]
               return@let "Bearer $token"
          }, /*preferenceUtils.userId*//*"02920f0d-1d8d-486b-9821-5e222cb57bc9"*/"28cea4ab-3188-434a-82f0-055133090a38", page,size
     )

     fun clearAllNotification() =  dataSource.clearAllnotification(
          preferenceUtils.basicToken.let {
               val token = it.split(" ")[1]
               return@let "Bearer $token"
          },/*preferenceUtils.userId*//*"02920f0d-1d8d-486b-9821-5e222cb57bc9"*/"28cea4ab-3188-434a-82f0-055133090a38"
     )

     suspend fun clearNotificationDB() = notificationDao.deleteNotification(/*preferenceUtils.userId*/"02920f0d-1d8d-486b-9821-5e222cb57bc9")
     suspend fun deleteNotificationID(id:Int) = notificationDao.deleteNotificationID(id)
}
/*
28cea4ab-3188-434a-82f0-055133090a
bnVyc2UxOk51cnNlQDEyMw==*/


