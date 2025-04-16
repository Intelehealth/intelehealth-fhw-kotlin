package org.intelehealth.data.provider.user

import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.data.network.BuildConfig
import org.intelehealth.data.network.RestClient
import org.intelehealth.data.network.model.request.DeviceTokenReq
import org.intelehealth.data.network.model.request.JWTParams
import org.intelehealth.data.network.model.request.OtpRequestParam
import org.intelehealth.common.data.BaseDataSource
import org.intelehealth.data.network.KEY_ATTRIBUTE_TYPE_ID
import org.intelehealth.data.network.KEY_FILE
import org.intelehealth.data.network.KEY_NEW_PASSWORD
import org.intelehealth.data.network.KEY_OLD_PASSWORD
import org.intelehealth.data.network.KEY_PERSON_ID
import org.intelehealth.data.network.KEY_VALUE
import org.intelehealth.data.network.model.request.UserProfileEditableDetails
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

    fun changePassword(bearerToken: String, oldPassword: String, newPassword: String) = getResult {
        restClient.changePassword(
            hashMapOf(
                KEY_OLD_PASSWORD to oldPassword, KEY_NEW_PASSWORD to newPassword
            ), bearerToken
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

    suspend fun fetchUserProfile(
        basicAuth: String,
        userId: String
    ) = restClient.fetchUserProfile(userId = userId, authHeader = basicAuth)

    fun updateUserProfileEditableDetails(
        basicAuth: String,
        personId: String,
        editableDetails: UserProfileEditableDetails
    ) = getResult {
        restClient.updateUserProfileEditableDetails(
            authHeader = basicAuth,
            personId = personId,
            editableDetails = editableDetails
        )
    }

    suspend fun createUserProfileAttribute(
        basicAuth: String,
        providerId: String,
        attributeUuid: String,
        value: String
    ) = restClient.createUserProfileAttribute(
        authHeader = basicAuth,
        providerId = providerId,
        value = hashMapOf(
            KEY_VALUE to value,
            KEY_ATTRIBUTE_TYPE_ID to attributeUuid
        )
    )

    suspend fun updateUserProfileAttribute(
        basicAuth: String,
        providerId: String,
        attributeUuid: String,
        value: String
    ) = restClient.updateUserProfileAttribute(
        authHeader = basicAuth,
        providerId = providerId,
        attributeUuid = attributeUuid,
        value = hashMapOf(KEY_VALUE to value)
    )

    suspend fun updateUserProfilePicture(
        basicAuth: String,
        personId: String,
        image: String
    ) = restClient.uploadProfilePicture(
        basicAuth, hashMapOf(KEY_PERSON_ID to personId, KEY_FILE to image)
    )

//    companion object {
//        const val KEY_USER_UUID = "userUuid"
//        const val KEY_NEW_PASSWORD = "newPassword"
//        const val KEY_OLD_PASSWORD = "oldPassword"
//        const val KEY_RESULT = "results"
//        const val KEY_PERSON_ID = "person"
//        const val KEY_FILE = "base64EncodedImage"
//        const val KEY_VALUE = "value"
//        const val KEY_ATTRIBUTE_TYPE_ID = "attributeType"
//    }
}
