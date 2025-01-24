package org.intelehealth.data.provider.user

import android.util.Base64
import org.intelehealth.common.utility.PreferenceUtils
import org.intelehealth.data.network.model.DeviceTokenReq
import org.intelehealth.data.network.model.JWTParams
import org.intelehealth.data.offline.dao.UserDao
import org.intelehealth.data.offline.entity.User
import java.nio.charset.StandardCharsets
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 16-01-2025 - 18:08.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class UserRepository @Inject constructor(
    private val dataSource: UserDataSource, private val preferenceUtils: PreferenceUtils, private val userDao: UserDao
) {
    suspend fun generateJWTAuthToken(param: JWTParams) = dataSource.generateJWTAuthToken(param)

    fun login(username: String, password: String) =
        Base64.encodeToString(("$username:$password").toByteArray(StandardCharsets.UTF_8), Base64.NO_WRAP).let {
            preferenceUtils.basicAuthToken = "Basic $it"
            dataSource.login(preferenceUtils.basicAuthToken)
        }

    fun saveJWTToken(jwtToken: String) {
        preferenceUtils.authToken = jwtToken
    }

    suspend fun saveUser(user: User) {
        preferenceUtils.userId = user.userId
        userDao.add(user)
    }

    fun updateUserLoggedInStatus(isLoggedIn: Boolean) {
        preferenceUtils.userLoggedInStatus = isLoggedIn
    }

    fun getLiveUser() = userDao.getLiveUser(preferenceUtils.userId)

    fun sendUserDeviceToken() = dataSource.sendUserDeviceToken(
        DeviceTokenReq(preferenceUtils.userId, preferenceUtils.currentLanguage, preferenceUtils.fcmToken)
    )

    suspend fun updateUser(user: User) = userDao.update(user)

    fun logout() {
        updateUserLoggedInStatus(false)
    }

}