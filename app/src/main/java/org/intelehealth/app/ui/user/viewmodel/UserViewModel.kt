package org.intelehealth.app.ui.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.intelehealth.common.extensions.containsDigit
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.common.state.Result
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.common.utility.CommonConstants.MIN_PASSWORD_LENGTH
import org.intelehealth.common.utility.DateTimeUtils
import org.intelehealth.data.network.model.request.JWTParams
import org.intelehealth.data.network.model.request.OtpRequestParam
import org.intelehealth.data.network.model.response.LoginResponse
import org.intelehealth.data.network.model.response.UserResponse
import org.intelehealth.data.offline.entity.User
import org.intelehealth.data.provider.user.UserRepository
import java.security.SecureRandom
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 18-01-2025 - 11:22.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * [androidx.lifecycle.ViewModel] for managing user-related data and operations.
 *
 * This ViewModel interacts with [UserRepository] to perform actions such as
 * login, logout, saving user data, generating JWT tokens, handling OTP requests,
 * and managing the OTP countdown timer. It also provides LiveData for observing
 * changes in the OTP countdown and the last sync time.
 *
 * @property userRepository The [UserRepository] instance for interacting with user data.
 * @property networkHelper The [NetworkHelper] instance for checking network connectivity.
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository, networkHelper: NetworkHelper
) : BaseViewModel(networkHelper = networkHelper) {

    /**
     * LiveData for observing the OTP countdown time.
     */
    private val otpCountDown = MutableLiveData<Long>()
    val otpCountDownLiveData: LiveData<Long> = otpCountDown

    /**
     * LiveData for observing the last sync time.
     */
    private val lastSyncTime = MutableLiveData<String>()
    val lastSyncData: LiveData<String> = lastSyncTime


    /**
     * Retrieves and posts the application's last sync time to [lastSyncTime].
     */
    fun appLastSyncTime() {
        lastSyncTime.postValue(userRepository.appLastSyncTime())
    }

    /**
     * Generates a JWT authentication token.
     *
     * @param param The [JWTParams] containing the parameters for generating the token.
     * @return A [LiveData] emitting the result of the network call.
     */
    fun generateJWTAuthToken(param: JWTParams) =
        executeNetworkCall { userRepository.generateJWTAuthToken(param) }.asLiveData()

    /**
     * Logs in a user with the given username and password.
     *
     * @param username The user's username.
     * @param password The user's password.
     * @return A [LiveData] emitting the result of the login operation.
     */
    fun login(username: String, password: String) = userRepository.login(username, password).asLiveData()

    /**
     * Saves the JWT token to the repository.
     *
     * @param jwtToken The JWT token to save.
     */
    fun saveJWTToken(jwtToken: String) = userRepository.saveJWTToken(jwtToken)

    /**
     * Saves the user data to the repository.
     *
     * This function creates a [User] object from the [LoginResponse] and saves it
     * to the repository. It also updates the user's logged-in status and calls
     * the provided callback with the saved [User] object.
     *
     * @param loginResponse The [LoginResponse] containing the user data.
     * @param onSaved A callback to be invoked with the saved [User] object.
     */
    fun saveUser(loginResponse: LoginResponse, onSaved: (User) -> Unit) {
        viewModelScope.launch {
            User(
                userId = loginResponse.user.uuid,
                displayName = loginResponse.user.person.display,
                userName = loginResponse.user.username,
                password = "",
                systemId = loginResponse.user.systemId,
                personId = loginResponse.user.person.uuid,
                providerId = loginResponse.provider.uuid,
                sessionId = loginResponse.sessionId,
                firstLoginInTime = DateTimeUtils.getCurrentDateWithDBFormat(),
                lastLoginInTime = DateTimeUtils.getCurrentDateWithDBFormat()
            ).also {
                userRepository.saveUser(it)
                userRepository.updateUserLoggedInStatus(true)
                withContext(Dispatchers.Main) { onSaved(it) }
            }
        }
    }

    /**
     * Retrieves the live user data from the repository.
     *
     * @return A [LiveData] emitting the current [User] data.
     */
    fun getUser() = userRepository.getLiveUser()

    /**
     * Retrieves the user's name and invokes the provided callback.
     *
     * @param onUserNameFetched A callback to be invoked with the user's name.
     */
    fun getUserName(onUserNameFetched: (String) -> Unit) = viewModelScope.launch {
        val username = userRepository.getUserName()
        onUserNameFetched(username)
    }

    /**
     * Sends the user's device token to the server.
     *
     * @return A [LiveData] emitting the result of the network call.
     */
    fun sendUserDeviceToken() = userRepository.sendUserDeviceToken().asLiveData()

    /**
     * Updates the user data in the repository.
     *
     * @param user The [User] object containing the updated data.
     * @param onUpdated A callback to be invoked when the update is complete.
     */
    fun updateUser(user: User, onUpdated: () -> Unit) {
        viewModelScope.launch {
            userRepository.updateUserLoggedInStatus(true)
            userRepository.updateUser(user)
            withContext(Dispatchers.Main) { onUpdated() }
        }
    }

    /**
     * Logs out the current user.
     */
    fun logout() {
        userRepository.logout()
    }

    /**
     * Changes the user's password.
     *
     * @param oldPassword The user's old password.
     * @param newPassword The user's new password.
     * @return A [LiveData] emitting the result of the password change operation.
     */
    fun changePassword(oldPassword: String, newPassword: String) =
        userRepository.changePassword(oldPassword, newPassword).asLiveData()

    /**
     * Requests an OTP (One-Time Password).
     *
     * @param otpRequestParam The [OtpRequestParam] containing the parameters for the OTP request.
     * @return A [LiveData] emitting the result of the OTP request.
     */
    fun requestOTP(otpRequestParam: OtpRequestParam) = userRepository.requestOTP(otpRequestParam).asLiveData()

    /**
     * Verifies an OTP.
     *
     * @param otpRequestParam The [OtpRequestParam] containing the parameters for the OTP verification.
     * @return A [LiveData] emitting the result of the OTP verification.
     */
    fun verifyOTP(otpRequestParam: OtpRequestParam) = executeNetworkCall {
        userRepository.verifyOTP(otpRequestParam)
    }.asLiveData()

    /**
     * Resets the user's password.
     *
     * @param userUuid The UUID of the user.
     * @param newPassword The user's new password.
     * @return A [LiveData] emitting the result of the password reset operation.
     */
    fun resetPassword(userUuid: String, newPassword: String) = executeNetworkCall {
        userRepository.resetPassword(userUuid, newPassword)
    }.asLiveData()

    /**
     * Handles the response from a user-related network call.
     *
     * @param it The [Result] containing the response.
     * @param callback A callback to be invoked with the [UserResponse] data.
     */
    fun handleUserResponse(it: Result<UserResponse<Any?>>, callback: (data: UserResponse<Any?>) -> Unit) {
        handleResponse(it) {
            if (!it.success) failResult.postValue(it.message)
            else callback(it)
        }
    }

    /**
     * Starts a countdown timer for the OTP (One-Time Password).
     *
     * This function initializes a countdown timer with a duration of [OTP_EXPIRY_TIME] milliseconds.
     * It updates the [otpCountDown] LiveData with the remaining time in seconds at regular intervals.
     * Once the timer expires, it sets [otpCountDown] to 0.
     *
     * The timer uses [OTP_EXPIRY_TIME_INTERVAL] as the interval between updates.
     */
    fun startOTPCountDownTimer() {
        viewModelScope.launch {
            var time = OTP_EXPIRY_TIME
            otpCountDown.postValue(time / OTP_EXPIRY_TIME_INTERVAL)
            while (time > 0) {
                otpCountDown.postValue(time / OTP_EXPIRY_TIME_INTERVAL)
                time -= OTP_EXPIRY_TIME_INTERVAL
                delay(OTP_EXPIRY_TIME_INTERVAL)
            }

            otpCountDown.postValue(0)
        }
    }

    /**
     * Generates a random password.
     *
     * This function generates a random password of a specified length ([MIN_PASSWORD_LENGTH])
     * using a combination of uppercase letters, lowercase letters, and digits.
     * It ensures that the generated password contains at least one digit.
     * If the generated password does not contain a digit, it recursively calls itself
     * to generate a new password until the condition is met.
     *
     * @return A random password string.
     */
    fun generatePassword(): String {
        val pattern = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz"
        val rnd = SecureRandom()
        val sb = StringBuilder(MIN_PASSWORD_LENGTH)
        @Suppress("UnusedPrivateProperty")
        for (i in 0 until MIN_PASSWORD_LENGTH) {
            sb.append(pattern[rnd.nextInt(pattern.length)])
        }

        return if (sb.toString().containsDigit()) {
            sb.toString()
        } else generatePassword()
    }

    companion object {
        const val OTP_EXPIRY_TIME = 60 * 1000L
        const val OTP_EXPIRY_TIME_INTERVAL = 1000L
    }

}
