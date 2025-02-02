package org.intelehealth.data.provider.user

import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.data.network.BuildConfig
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
class UserDataSource @Inject constructor(
    private val restClient: RestClient, networkHelper: NetworkHelper
) : BaseDataSource(networkHelper = networkHelper) {
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
                KEY_OLD_PASSWORD to oldPassword, KEY_NEW_PASSWORD to newPassword
            ), basicAuth
        )
    }

    fun requestOtp(otpRequestParam: OtpRequestParam) = getResult {
        restClient.requestOTP(otpReqParam = otpRequestParam)
    }

    suspend fun verifyOtp(otpRequestParam: OtpRequestParam) = restClient.verifyOTP(otpReqParam = otpRequestParam)

    suspend fun resetPassword(userUuid: String, newPassword: String) = restClient.resetPassword(
        url = BuildConfig.SERVER_URL + ":3004/api/auth/resetPassword/$userUuid",
        map = hashMapOf(KEY_NEW_PASSWORD to newPassword)
    )

    companion object {
        const val KEY_USER_UUID = "userUuid"
        const val KEY_NEW_PASSWORD = "newPassword"
        const val KEY_OLD_PASSWORD = "oldPassword"
    }
}