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
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.common.state.Result
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.common.utility.DateTimeUtils
import org.intelehealth.data.network.model.request.JWTParams
import org.intelehealth.data.network.model.response.LoginResponse
import org.intelehealth.data.network.model.request.OtpRequestParam
import org.intelehealth.data.network.model.response.UserResponse
import org.intelehealth.data.offline.entity.User
import org.intelehealth.data.provider.user.UserRepository
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 18-01-2025 - 11:22.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository, networkHelper: NetworkHelper
) : BaseViewModel(networkHelper = networkHelper) {

    private val otpCountDown = MutableLiveData<Long>()
    val otpCountDownLiveData: LiveData<Long> = otpCountDown

    fun generateJWTAuthToken(param: JWTParams) =
        executeNetworkCall { userRepository.generateJWTAuthToken(param) }.asLiveData()

    fun login(username: String, password: String) = userRepository.login(username, password).asLiveData()

    fun saveJWTToken(jwtToken: String) = userRepository.saveJWTToken(jwtToken)

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

    fun getUser() = userRepository.getLiveUser()

    fun sendUserDeviceToken() = userRepository.sendUserDeviceToken().asLiveData()

    fun updateUser(user: User, onUpdated: () -> Unit) {
        viewModelScope.launch {
            userRepository.updateUser(user)
            withContext(Dispatchers.Main) { onUpdated() }
        }
    }

    fun logout() {
        userRepository.logout()
    }

    fun changePassword(oldPassword: String, newPassword: String) =
        userRepository.changePassword(oldPassword, newPassword).asLiveData()

    fun requestOTP(otpRequestParam: OtpRequestParam) = userRepository.requestOTP(otpRequestParam).asLiveData()

    fun verifyOTP(otpRequestParam: OtpRequestParam) = executeNetworkCall {
        userRepository.verifyOTP(otpRequestParam)
    }.asLiveData()

    fun resetPassword(userUuid: String, map: HashMap<String, String>) = executeNetworkCall {
        userRepository.resetPassword(userUuid, map)
    }.asLiveData()

    fun handleUserResponse(it: Result<UserResponse<Any?>>, callback: (data: UserResponse<Any?>) -> Unit) {
        handleResponse(it) {
            if (!it.success) failResult.postValue(it.message)
            else callback(it)
        }
    }

    // countdown timer for expired OTP
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

    companion object {
        const val OTP_EXPIRY_TIME = 60 * 1000L
        const val OTP_EXPIRY_TIME_INTERVAL = 1000L
    }
}