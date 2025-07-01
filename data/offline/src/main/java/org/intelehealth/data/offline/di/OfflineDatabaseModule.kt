package org.intelehealth.data.offline.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.intelehealth.data.offline.OfflineDatabase
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
import org.intelehealth.data.offline.entity.RecentHistory
import javax.inject.Singleton

/**
 * Created by Vaghela Mithun R. on 20-12-2024 - 18:22.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Module
@InstallIn(SingletonComponent::class)
object OfflineDatabaseModule {
    @Provides
    @Singleton
    fun provideCatchDatabase(@ApplicationContext context: Context): OfflineDatabase =
        OfflineDatabase.buildDatabase(context)

    @Provides
    @Singleton
    fun provideAppointmentDao(offlineDatabase: OfflineDatabase): AppointmentDao = offlineDatabase.appointmentDao()

    @Provides
    @Singleton
    fun provideConceptDao(offlineDatabase: OfflineDatabase): ConceptDao = offlineDatabase.conceptDao()

    @Provides
    @Singleton
    fun provideEncounterDao(offlineDatabase: OfflineDatabase): EncounterDao = offlineDatabase.encounterDao()

    @Provides
    @Singleton
    fun provideLocalNotificationDao(offlineDatabase: OfflineDatabase): LocalNotificationDao =
        offlineDatabase.localNotificationDao()

    @Provides
    @Singleton
    fun provideMediaRecordDao(offlineDatabase: OfflineDatabase): MediaRecordDao = offlineDatabase.mediaRecordDao()

    @Provides
    @Singleton
    fun provideObservationDao(offlineDatabase: OfflineDatabase): ObservationDao = offlineDatabase.observationDao()

    @Provides
    @Singleton
    fun providePatientDao(offlineDatabase: OfflineDatabase): PatientDao = offlineDatabase.patientDao()

    @Provides
    @Singleton
    fun providePatientAttributeTypeMasterDao(offlineDatabase: OfflineDatabase): PatientAttributeTypeMasterDao =
        offlineDatabase.patientAttrMasterDao()

    @Provides
    @Singleton
    fun providePatientAttributeDao(offlineDatabase: OfflineDatabase): PatientAttributeDao =
        offlineDatabase.patientAttrDao()

    @Provides
    @Singleton
    fun providePatientLocationDao(offlineDatabase: OfflineDatabase): PatientLocationDao =
        offlineDatabase.patientLocationDao()

    @Provides
    @Singleton
    fun provideProviderDao(offlineDatabase: OfflineDatabase): ProviderDao = offlineDatabase.providerDao()

    @Provides
    @Singleton
    fun provideProviderAttributeDao(offlineDatabase: OfflineDatabase): ProviderAttributeDao =
        offlineDatabase.providerAttributeDao()

    @Provides
    @Singleton
    fun provideVisitDao(offlineDatabase: OfflineDatabase): VisitDao = offlineDatabase.visitDao()

    @Provides
    @Singleton
    fun provideVisitAttributeDao(offlineDatabase: OfflineDatabase): VisitAttributeDao =
        offlineDatabase.visitAttributeDao()

    @Provides
    @Singleton
    fun provideUserDao(offlineDatabase: OfflineDatabase): UserDao = offlineDatabase.userDao()

    @Provides
    @Singleton
    fun provideUserSessionDao(offlineDatabase: OfflineDatabase): UserSessionDao = offlineDatabase.userSessionDao()

    @Provides
    @Singleton
    fun provideRecentHistoryDao(offlineDatabase: OfflineDatabase): RecentHistoryDao = offlineDatabase.recentHistoryDao()
}
