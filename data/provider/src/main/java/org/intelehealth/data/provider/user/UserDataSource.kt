package org.intelehealth.data.provider.user

import org.intelehealth.data.network.RestClient
import org.intelehealth.data.network.model.DeviceTokenReq
import org.intelehealth.data.network.model.JWTParams
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
}