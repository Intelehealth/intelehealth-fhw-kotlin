package org.intelehealth.config.room

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.intelehealth.config.room.dao.ConfigDao
import org.intelehealth.config.room.dao.ActiveFeatureStatusDao
import org.intelehealth.config.room.dao.ActiveSectionDao
import org.intelehealth.config.room.dao.LanguageDao
import org.intelehealth.config.room.dao.PatientDiagnosticsDao
import org.intelehealth.config.room.dao.PatientRegFieldDao
import org.intelehealth.config.room.dao.VitalDao
import org.intelehealth.config.room.dao.SpecializationDao
import org.intelehealth.config.room.entity.ActiveLanguage
import org.intelehealth.config.room.entity.ConfigDictionary
import org.intelehealth.config.room.entity.ActiveFeatureStatus
import org.intelehealth.config.room.entity.ActiveSection
import org.intelehealth.config.room.entity.Diagnostics
import org.intelehealth.config.room.entity.PatientRegistrationFields
import org.intelehealth.config.room.entity.Vital
import org.intelehealth.config.room.entity.Specialization

/**
 * Created by Vaghela Mithun R. on 15-03-2024 - 15:43.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Database(
    entities = [
        ConfigDictionary::class,
        Specialization::class,
        ActiveLanguage::class,
        PatientRegistrationFields::class,
        ActiveFeatureStatus::class,
        Vital::class,
        Diagnostics::class,
        ActiveSection::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ConfigDatabase : RoomDatabase() {

    abstract fun configDao(): ConfigDao

    abstract fun specializationDao(): SpecializationDao

    abstract fun languageDao(): LanguageDao

    abstract fun patientRegFieldDao(): PatientRegFieldDao

    abstract fun patientVitalDao(): VitalDao

    abstract fun featureActiveStatusDao(): ActiveFeatureStatusDao

    abstract fun patientDiagnosticsDao(): PatientDiagnosticsDao
    abstract fun activeSectionDao(): ActiveSectionDao



    companion object {

        @Volatile
        private var INSTANCE: ConfigDatabase? = null

        @VisibleForTesting
        private val DATABASE_NAME = "config-db"

        @JvmStatic
        fun getInstance(context: Context): ConfigDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context.applicationContext).also {
                    INSTANCE = it
                }
            }

        /**
         * Set up the database configuration.
         * The SQLite database is only created when it's accessed for the first time.
         */
        fun buildDatabase(appContext: Context): ConfigDatabase {
            val databaseName = "${appContext.packageName}.$DATABASE_NAME"
            return Room.databaseBuilder(appContext, ConfigDatabase::class.java, databaseName)
                .fallbackToDestructiveMigration()
                .build()
        }
//
//        private fun getAppName(context: Context) = getApplicationName(context).let {
//            return@let it.replace(" ", "-").lowercase(Locale.getDefault())
//        }
//
//        private fun getApplicationName(context: Context): String {
//            val applicationInfo = context.applicationInfo
//            val stringId = applicationInfo.labelRes
//            return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString()
//            else context.getString(stringId)
//        }
    }
}
