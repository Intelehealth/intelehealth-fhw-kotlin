package org.intelehealth.data.provider.notification

import org.intelehealth.common.utility.PreferenceUtils
import org.intelehealth.data.offline.dao.LocalNotificationDao
import org.intelehealth.data.offline.dao.VisitDao
import org.intelehealth.data.offline.entity.LocalNotification
import org.intelehealth.data.provider.user.UserDataSource
import javax.inject.Inject

class NotificationRepository @Inject constructor(private val notificationDao: LocalNotificationDao,
                                                 private val dataSource: NotificationDataSource,
                                                 private val preferenceUtils: PreferenceUtils
) {

     suspend fun getAllNotification(): List<LocalNotification> {
          return notificationDao.getAllNotification()
     }
     suspend fun insertnotification(notifications: List<LocalNotification>)=
          notificationDao.insertAll(notifications)

     fun getNotificationData() = notificationDao.getAllNotificationData()

     fun getNotifiDatawithOffset(limit: Int, offset: Int) = notificationDao.getNotifiDataWithOffset(limit,offset)


     fun fetchAllnotification(userId: String, page: String, size: String) = dataSource.fetchAllnotification(
          preferenceUtils.basicToken.let {
               val token = it.split(" ")[1]
               return@let "Bearer $token"
          }, userId, page,size
     )
}