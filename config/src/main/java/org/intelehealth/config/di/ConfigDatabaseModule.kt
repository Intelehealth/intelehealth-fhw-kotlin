package org.intelehealth.config.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.intelehealth.config.room.ConfigDatabase
import javax.inject.Singleton

/**
 * Created by Vaghela Mithun R. on 19-12-2024 - 18:07.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Module
@InstallIn(SingletonComponent::class)
object ConfigDatabaseModule {

    @Provides
    @Singleton
    fun provideConfigDatabase(@ApplicationContext context: Context) = ConfigDatabase.buildDatabase(context)

    @Provides
    @Singleton
    fun provideConfigDao(configDatabase: ConfigDatabase) = configDatabase.configDao()

    @Provides
    @Singleton
    fun provideSpecializationDao(configDatabase: ConfigDatabase) = configDatabase.specializationDao()

    @Provides
    @Singleton
    fun provideLanguageDao(configDatabase: ConfigDatabase) = configDatabase.languageDao()

    @Provides
    @Singleton
    fun providePatientRegFieldDao(configDatabase: ConfigDatabase) = configDatabase.patientRegFieldDao()

    @Provides
    @Singleton
    fun providePatientVitalDao(configDatabase: ConfigDatabase) = configDatabase.patientVitalDao()

    @Provides
    @Singleton
    fun provideFeatureActiveStatusDao(configDatabase: ConfigDatabase) = configDatabase.featureActiveStatusDao()

}