package org.intelehealth.data.offline

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import org.intelehealth.common.extensions.appName
import org.intelehealth.data.offline.dao.AppointmentDao
import org.intelehealth.data.offline.dao.ConceptDao
import org.intelehealth.data.offline.dao.EncounterDao
import org.intelehealth.data.offline.dao.LocalNotificationDao
import org.intelehealth.data.offline.dao.MediaRecordDao
import org.intelehealth.data.offline.dao.ObservationDao
import org.intelehealth.data.offline.dao.PatientAttributeDao
import org.intelehealth.data.offline.dao.PatientAttributeTypeMasterDao
import org.intelehealth.data.offline.dao.PatientDao
import org.intelehealth.data.offline.dao.PatientLocationDao
import org.intelehealth.data.offline.dao.ProviderAttributeDao
import org.intelehealth.data.offline.dao.ProviderDao
import org.intelehealth.data.offline.dao.RecentHistoryDao
import org.intelehealth.data.offline.dao.UserDao
import org.intelehealth.data.offline.dao.UserSessionDao
import org.intelehealth.data.offline.dao.VisitAttributeDao
import org.intelehealth.data.offline.dao.VisitDao
import org.intelehealth.data.offline.entity.Appointment
import org.intelehealth.data.offline.entity.Concept
import org.intelehealth.data.offline.entity.Encounter
import org.intelehealth.data.offline.entity.FollowupScheduleNotification
import org.intelehealth.data.offline.entity.MediaRecord
import org.intelehealth.data.offline.entity.NotificationList
import org.intelehealth.data.offline.entity.NotificationResponse
import org.intelehealth.data.offline.entity.Observation
import org.intelehealth.data.offline.entity.Patient
import org.intelehealth.data.offline.entity.PatientAttribute
import org.intelehealth.data.offline.entity.PatientAttributeTypeMaster
import org.intelehealth.data.offline.entity.PatientLocation
import org.intelehealth.data.offline.entity.PayloadConverter
import org.intelehealth.data.offline.entity.Provider
import org.intelehealth.data.offline.entity.ProviderAttribute
import org.intelehealth.data.offline.entity.RecentHistory
import org.intelehealth.data.offline.entity.User
import org.intelehealth.data.offline.entity.UserSession
import org.intelehealth.data.offline.entity.Visit
import org.intelehealth.data.offline.entity.VisitAttribute


/**
 * Created by - Prajwal W. on 27/09/24.
 * Email: prajwalwaingankar@gmail.com
 * Mobile: +917304154312
 **/

@Database(
    entities = [
        Appointment::class, Concept::class, Encounter::class, FollowupScheduleNotification::class,
        NotificationList::class, MediaRecord::class, Observation::class, Patient::class, PatientAttribute::class,
        PatientAttributeTypeMaster::class, PatientLocation::class, Provider::class, ProviderAttribute::class,
        User::class, UserSession::class, Visit::class, VisitAttribute::class, RecentHistory::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters( PayloadConverter::class)
abstract class OfflineDatabase : RoomDatabase() {
    abstract fun appointmentDao(): AppointmentDao
    abstract fun conceptDao(): ConceptDao
    abstract fun encounterDao(): EncounterDao
    abstract fun localNotificationDao(): LocalNotificationDao
    abstract fun mediaRecordDao(): MediaRecordDao
    abstract fun observationDao(): ObservationDao
    abstract fun patientDao(): PatientDao
    abstract fun patientAttrMasterDao(): PatientAttributeTypeMasterDao
    abstract fun patientAttrDao(): PatientAttributeDao
    abstract fun patientLocationDao(): PatientLocationDao
    abstract fun providerDao(): ProviderDao
    abstract fun providerAttributeDao(): ProviderAttributeDao
    abstract fun visitDao(): VisitDao
    abstract fun visitAttributeDao(): VisitAttributeDao
    abstract fun userDao(): UserDao
    abstract fun userSessionDao(): UserSessionDao
    abstract fun recentHistoryDao(): RecentHistoryDao

    companion object {
        private const val DATABASE_NAME = "main.db"

        /**
         * Set up the database configuration.
         * The SQLite database is only created when it's accessed for the first time.
         */
        fun buildDatabase(appContext: Context): OfflineDatabase {
            val databaseName = "${appContext.appName()}.$DATABASE_NAME"
            return Room.databaseBuilder(appContext, OfflineDatabase::class.java, databaseName)
                .fallbackToDestructiveMigration()   // on migration if no migration scheme is provided than it will perform destructive migration.
                .build()
        }
    }
}
