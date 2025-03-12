package org.intelehealth.data.provider.user

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.intelehealth.common.utility.PreferenceUtils
import org.intelehealth.data.offline.dao.UserSessionDao
import org.intelehealth.data.offline.entity.UserSession
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 11-03-2025 - 17:56.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class UserSessionRepository @Inject constructor(
    private val preferenceUtils: PreferenceUtils,
    private val userSessionDao: UserSessionDao
) {
    private lateinit var userSession: UserSession
    fun startSession() {
        userSession = UserSession(
            userId = preferenceUtils.userId,
            startTime = System.currentTimeMillis().toString(),
            endTime = "",
            sessionDuration = ""
        )
    }

    fun endSession() {
        // End session
        userSession.endTime = System.currentTimeMillis().toString()
        userSession.userId = preferenceUtils.userId
        userSession.sessionDuration = (userSession.endTime.toLong() - userSession.startTime.toLong()).toString()
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        scope.launch { userSessionDao.add(userSession) }
    }
}
