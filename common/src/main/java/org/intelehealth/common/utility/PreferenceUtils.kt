package org.intelehealth.common.utility

import org.intelehealth.common.helper.PreferenceHelper
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vaghela Mithun R. on 01-01-2025 - 19:55.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

@Singleton
class PreferenceUtils @Inject constructor(private val preferenceHelper: PreferenceHelper) {
    var initialLaunchStatus: Boolean
        get() = preferenceHelper.get(PreferenceHelper.INITIAL_LAUNCH, true)
        set(value) = preferenceHelper.save(PreferenceHelper.INITIAL_LAUNCH, value)

    var currentLanguage: String
        get() = preferenceHelper.get(PreferenceHelper.CURRENT_LANGUAGE, "en")
        set(value) = preferenceHelper.save(PreferenceHelper.CURRENT_LANGUAGE, value)

    var location: String
        get() = preferenceHelper.get(PreferenceHelper.KEY_PREF_LOCATION_UUID, "")
        set(value) = preferenceHelper.save(PreferenceHelper.KEY_PREF_LOCATION_UUID, value)

    var authToken: String
        get() = preferenceHelper.get(PreferenceHelper.JWT_AUTH_TOKEN, "")
        set(value) = preferenceHelper.save(PreferenceHelper.JWT_AUTH_TOKEN, value)

    var basicAuthToken: String
        get() = preferenceHelper.get(PreferenceHelper.AUTH_BASIC_TOKEN, "")
        set(value) = preferenceHelper.save(PreferenceHelper.AUTH_BASIC_TOKEN, value)

    var fcmToken: String
        get() = preferenceHelper.get(PreferenceHelper.FCM_TOKEN, "")
        set(value) = preferenceHelper.save(PreferenceHelper.FCM_TOKEN, value)

    var userLoggedInStatus: Boolean
        get() = preferenceHelper.get(PreferenceHelper.USER_LOGGED_IN_STATUS, false)
        set(value) = preferenceHelper.save(PreferenceHelper.USER_LOGGED_IN_STATUS, value)

    var lastSyncedTime: String
        get() = preferenceHelper.get(PreferenceHelper.PULL_EXECUTED_TIME, "2006-08-22 22:21:48 ")
        set(value) = preferenceHelper.save(PreferenceHelper.PULL_EXECUTED_TIME, value)

    var userId: String
        get() = preferenceHelper.get(PreferenceHelper.USER_ID, "")
        set(value) = preferenceHelper.save(PreferenceHelper.USER_ID, value)

    var blackoutActiveStatus: Boolean
        get() = preferenceHelper.get(PreferenceHelper.KEY_BLACKOUT_PERIOD_ACTIVE_STATUS, false)
        set(value) = preferenceHelper.save(PreferenceHelper.KEY_BLACKOUT_PERIOD_ACTIVE_STATUS, value)

    var fingerprintAppLock: Boolean
        get() = preferenceHelper.get(PreferenceHelper.KEY_FINGERPRINT_APP_LOCK, false)
        set(value) = preferenceHelper.save(PreferenceHelper.KEY_FINGERPRINT_APP_LOCK, value)
}
