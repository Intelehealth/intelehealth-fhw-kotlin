package org.intelehealth.data.provider.user

import org.intelehealth.data.network.RestClient
import org.intelehealth.data.network.model.request.DeviceTokenReq
import org.intelehealth.data.network.model.request.JWTParams
import org.intelehealth.data.network.model.request.OtpRequestParam
import org.intelehealth.data.provider.BaseDataSource
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 16-01-2025 - 18:07.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class UserDataSource @Inject constructor(private val restClient: RestClient) : BaseDataSource() {
    suspend fun generateJWTAuthToken(param: JWTParams) = restClient.generateJWTAuthToken(body = param)

    fun login(basicAuth: String) = getResult { restClient.login(basicAuth) }

    fun sendUserDeviceToken(deviceToken: DeviceTokenReq) = getResult {
        restClient.sendUserDeviceToken(
            deviceTokenReq = deviceToken
        )
    }

    fun changePassword(basicAuth: String, oldPassword: String, newPassword: String) = getResult {
        restClient.changePassword(
            hashMapOf(
                "oldPassword" to oldPassword, "newPassword" to newPassword
            ), basicAuth
        )
    }

    fun requestOtp(otpRequestParam: OtpRequestParam) =
        getResult { restClient.requestOTP(otpReqParam = otpRequestParam) }

    suspend fun verifyOtp(otpRequestParam: OtpRequestParam) = restClient.verifyOTP(otpReqParam = otpRequestParam)

    suspend fun resetPassword(userUuid: String, map: HashMap<String, String>) =
        restClient.resetPassword(userUuid = userUuid, map = map)
}