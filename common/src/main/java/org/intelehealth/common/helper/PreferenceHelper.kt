package org.intelehealth.common.helper

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceHelper @Inject constructor(@ApplicationContext private val context: Context) {
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        const val JWT_AUTH_TOKEN: String = "AUTH_TOKEN"
        const val RTC_DATA = "rtc_data"
        const val ACTIVE_ROOM_ID = "active_room_id"
        const val RTC_CONFIG = "rtc_config"
        const val MESSAGE_BODY = "message_body"
        const val IS_NOTIFICATION = "isNotification"
        const val CONFIG_VERSION = "config_version"
        const val AUTH_BASIC_TOKEN: String = "basic_auth_token"
        const val KEY_PREF_LOCATION_UUID = "location_uuid"
        const val PULL_EXECUTED_TIME = "pull_executed_time"
        const val INITIAL_LAUNCH = "initial_launch"
        const val CURRENT_LANGUAGE = "current_language"
        const val FCM_TOKEN = "fcm_token"
        const val USER_LOGGED_IN_STATUS = "user_logged_in_status"
        const val USER_ID = "user_id"
    }

    fun save(key: String?, value: Any?) {
        val editor = getEditor()
        if (value is Boolean) {
            editor.putBoolean(key, (value as Boolean?)!!)
        } else if (value is Int) {
            editor.putInt(key, (value as Int?)!!)
        } else if (value is Float) {
            editor.putFloat(key, (value as Float?)!!)
        } else if (value is Long) {
            editor.putLong(key, (value as Long?)!!)
        } else if (value is String) {
            editor.putString(key, value as String?)
        } else if (value is Enum<*>) {
            editor.putString(key, value.toString())
        } else if (value != null) {
            throw UnsupportedOperationException("Attempting to save non-supported preference")
        }
        editor.commit()
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String?): T {
        return sharedPreferences.all[key] as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String?, defValue: T): T {
        return (sharedPreferences.all[key] ?: defValue) as T
    }

    private fun getEditor(): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    fun clear(key: String) {
        sharedPreferences.edit().run {
            remove(key)
        }.apply()
    }
}
