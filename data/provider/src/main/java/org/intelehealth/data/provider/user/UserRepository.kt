package org.intelehealth.data.provider.user

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import org.intelehealth.common.extensions.milliToLogTime
import org.intelehealth.common.extensions.toDate
import org.intelehealth.common.utility.DateTimeUtils.LAST_SYNC_DB_FORMAT
import org.intelehealth.common.utility.DateTimeUtils.LAST_SYNC_DISPLAY_FORMAT
import org.intelehealth.common.utility.PreferenceUtils
import org.intelehealth.data.network.KEY_RESULT
import org.intelehealth.data.network.model.request.DeviceTokenReq
import org.intelehealth.data.network.model.request.JWTParams
import org.intelehealth.data.network.model.request.OtpRequestParam
import org.intelehealth.data.network.model.request.UserProfileEditableDetails
import org.intelehealth.data.network.model.response.Profile
import org.intelehealth.data.offline.dao.UserDao
import org.intelehealth.data.offline.entity.User
import org.intelehealth.data.provider.utils.PersonAttributeType
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 16-01-2025 - 18:08.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Repository for managing user-related data and operations.
 *
 * This repository acts as an intermediary between the [UserDataSource] (for
 * network operations), [UserDao] (for local database operations), and
 * [PreferenceUtils] (for shared preferences). It provides methods for
 * performing actions such as login, logout, saving user data, generating JWT
 * tokens, handling OTP requests, and managing the last sync time.
 *
 * @property dataSource The [UserDataSource] instance for interacting with the network.
 * @property preferenceUtils The [PreferenceUtils] instance for interacting with shared preferences.
 * @property userDao The [UserDao] instance for interacting with the local database.
 */
class UserRepository @Inject constructor(
    private val dataSource: UserDataSource,
    private val preferenceUtils: PreferenceUtils,
    private val userDao: UserDao
) {
    /**
     * Retrieves the application's last sync time and formats it for display.
     *
     * @return The formatted last sync time as a string.
     */
    fun appLastSyncTime(): String {
        val lastSyncDate = preferenceUtils.lastSyncedTime.toDate(LAST_SYNC_DB_FORMAT)
        return lastSyncDate.time.toString().milliToLogTime(LAST_SYNC_DISPLAY_FORMAT)
    }

    /**
     * Generates a JWT authentication token.
     *
     * @param param The [JWTParams] containing the parameters for generating the token.
     * @return The result of the network call to generate the JWT token.
     */
    suspend fun generateJWTAuthToken(param: JWTParams) = dataSource.generateJWTAuthToken(param)

    /**
     * Logs in a user with the given username and password.
     *
     * This function encodes the username and password using Base64 and sets the
     * basic authentication token in [PreferenceUtils]. It then calls the
     * [UserDataSource] to perform the login operation.
     *
     * @param username The user's username.
     * @param password The user's password.
     * @return The result of the login operation.
     */
    fun login(username: String, password: String) = Credentials.basic(username, password).let {
        preferenceUtils.basicToken = it
        dataSource.login(preferenceUtils.basicToken)
    }

    /**
     * Saves the JWT token to shared preferences.
     *
     * @param jwtToken The JWT token to save.
     */
    fun saveJWTToken(jwtToken: String) {
        preferenceUtils.jwtToken = jwtToken
    }

    /**
     * Saves the user data to the local database and shared preferences.
     *
     * @param user The [User] object to save.
     */
    suspend fun saveUser(user: User) {
        preferenceUtils.userId = user.userId
        userDao.add(user)
    }

    /**
     * Updates the user's logged-in status in shared preferences.
     *
     * @param isLoggedIn `true` if the user is logged in, `false` otherwise.
     */
    fun updateUserLoggedInStatus(isLoggedIn: Boolean) {
        preferenceUtils.userLoggedInStatus = isLoggedIn
    }

    /**
     * Retrieves the live user data from the local database.
     *
     * @return The live user data.
     */
    fun getLiveUser() = userDao.getLiveUser(preferenceUtils.userId)

    /**
     * Retrieves the live user data from the local database.
     *
     * @return The user data.
     */
    suspend fun getUser() = userDao.getUser(preferenceUtils.userId)

    /**
     * Retrieves the user's name from the local database.
     *
     * @return The user's name.
     */
    suspend fun getUserName() = userDao.getUserName(preferenceUtils.userId)

    /**
     * Sends the user's device token to the server.
     *
     * @return The result of the network call to send the device token.
     */
    fun sendUserDeviceToken() = dataSource.sendUserDeviceToken(
        DeviceTokenReq(preferenceUtils.userId, preferenceUtils.currentLanguage, preferenceUtils.fcmToken)
    )

    /**
     * Updates the user data in the local database.
     *
     * @param user The [User] object containing the updated data.
     */
    suspend fun updateUser(user: User) = userDao.update(user)

    /**
     * Logs out the current user.
     *
     * This function updates the user's logged-in status to `false`.
     */
    fun logout() {
        updateUserLoggedInStatus(false)
    }

    /**
     * Changes the user's password.
     *
     * @param oldPassword The user's old password.
     * @param newPassword The user's new password.
     * @return The result of the network call to change the password.
     */
    fun changePassword(oldPassword: String, newPassword: String) = dataSource.changePassword(
        preferenceUtils.basicToken.let {
            val token = it.split(" ")[1]
            return@let "Bearer $token"
        }, oldPassword, newPassword
    )

    /**
     * Requests an OTP (One-Time Password).
     *
     * @param otpRequestParam The [OtpRequestParam] containing the parameters for the OTP request.
     * @return The result of the network call to request the OTP.
     */
    fun requestOTP(otpRequestParam: OtpRequestParam) = dataSource.requestOtp(otpRequestParam)

    /**
     * Verifies an OTP.
     *
     * @param otpRequestParam The [OtpRequestParam] containing the parameters for the OTP verification.
     * @return The result of the network call to verify the OTP.
     */
    suspend fun verifyOTP(otpRequestParam: OtpRequestParam) = dataSource.verifyOtp(otpRequestParam)

    /**
     * Resets the user's password.
     *
     * @param userUuid The UUID of the user.
     * @param newPassword The user's new password.
     * @return The result of the network call to reset the password.
     */
    suspend fun resetPassword(userUuid: String, newPassword: String) = dataSource.resetPassword(userUuid, newPassword)

    suspend fun fetchUserProfile() = dataSource.fetchUserProfile(
        preferenceUtils.basicToken,
        preferenceUtils.userId
    )

    suspend fun saveProfileData(data: HashMap<String, List<Profile>>?) {
        data?.get(KEY_RESULT)?.let { list -> if (list.isNotEmpty()) mappingProfileData(list[0]) }
    }

    private suspend fun mappingProfileData(profile: Profile) {
        withContext(Dispatchers.IO) {
            val user = async { getUser() }.await()
            user.apply {
                firstName = profile.person?.preferredName?.givenName
                middleName = profile.person?.preferredName?.middleName
                lastName = profile.person?.preferredName?.familyName
                gender = profile.person?.gender
                age = profile.person?.age
                dob = profile.person?.dateOfBirth
                profile.attributes?.forEach { attr ->
                    if (attr.uuid == PersonAttributeType.EMAIL.value) emailId = attr.value
                    else if (attr.uuid == PersonAttributeType.PHONE_NUMBER.value) phoneNumber = attr.value
                    else if (attr.uuid == PersonAttributeType.COUNTRY_CODE.value) countryCode = attr.value
                }
            }.also { updateUser(it) }
        }
    }

    fun updateUserProfile(
        personId: String,
        editableDetails: UserProfileEditableDetails
    ) = dataSource.updateUserProfileEditableDetails(
        basicAuth = preferenceUtils.basicToken,
        personId = personId,
        editableDetails = editableDetails
    )

    suspend fun createUserProfileAttribute(
        providerId: String,
        attributeUuid: String,
        value: String
    ) = dataSource.createUserProfileAttribute(
        basicAuth = preferenceUtils.basicToken,
        providerId = providerId,
        attributeUuid = attributeUuid,
        value = value
    )


    suspend fun updateUserProfileAttribute(
        providerId: String,
        attributeUuid: String,
        value: String
    ) = dataSource.updateUserProfileAttribute(
        basicAuth = preferenceUtils.basicToken,
        providerId = providerId,
        attributeUuid = attributeUuid,
        value = value
    )

    suspend fun updateUserProfilePicture(
        personId: String,
        image: String
    ) = dataSource.updateUserProfilePicture(
        basicAuth = preferenceUtils.basicToken,
        personId = personId,
        image = image
    )

    fun fingerprintAppLock() = preferenceUtils.fingerprintAppLock

    fun changeFingerprintAppLockState(state: Boolean) {
        preferenceUtils.fingerprintAppLock = state
    }

    suspend fun getProviderId(): String = userDao.getUserProviderId(preferenceUtils.userId)
}
