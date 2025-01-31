package org.intelehealth.data.network

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.intelehealth.common.service.BaseResponse
import org.intelehealth.data.network.model.request.DeviceTokenReq
import org.intelehealth.data.network.model.request.JWTParams
import org.intelehealth.data.network.model.response.LoginResponse
import org.intelehealth.data.network.model.request.OtpRequestParam
import org.intelehealth.data.network.model.response.PullResponse
import org.intelehealth.data.network.model.request.PushRequestApiCall
import org.intelehealth.data.network.model.SetupLocation
import org.intelehealth.data.network.model.response.UserResponse
import org.intelehealth.data.offline.entity.ObsJsonResponse
import org.intelehealth.data.offline.entity.Observation
import org.intelehealth.data.offline.entity.PatientProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Created by - Prajwal W. on 10/10/24.
 * Email: prajwalwaingankar@gmail.com
 * Mobile: +917304154312
 **/

interface RestClient {
    @GET("/openmrs/ws/rest/v1/location?tag=Login%20Location")
    suspend fun fetchLocationList(): Response<HashMap<String, List<SetupLocation>>>

    @POST
    suspend fun generateJWTAuthToken(
        @Url url: String = BuildConfig.SERVER_URL + ":3030/auth/login", @Body body: JWTParams
    ): Response<BaseResponse<Boolean, String>>

    @GET("/EMR-Middleware/webapi/pull/pulldata/{locationId}/{lastPullExecutedTime}/{pageNo}/{pageLimit}")
    suspend fun pullData(
        @Header("Authorization") header: String,
        @Path("locationId") locationId: String,
        @Path("lastPullExecutedTime") lastPullExecutedTime: String,
        @Path("pageNo") pageNo: Int,
        @Path("pageLimit") pageLimit: Int
    ): Response<BaseResponse<String, PullResponse>>

    @GET("/openmrs/ws/rest/v1/session")
    suspend fun login(@Header("Authorization") basicAuth: String): Response<LoginResponse>

    @PUT
    suspend fun sendUserDeviceToken(
        @Url url: String = BuildConfig.SERVER_URL + ":3004/api/mindmap/user_settings",
        @Body deviceTokenReq: DeviceTokenReq
    ): Response<ResponseBody>

    @GET
    fun getHtmlPage(@Url url: String?): Response<ResponseBody>

    @POST("/openmrs/ws/rest/v1/password")
    suspend fun changePassword(
        @Body map: HashMap<String, String>, @Header("Authorization") authHeader: String
    ): Response<ResponseBody>

    @POST
    @Headers("Accept: application/json")
    suspend fun requestOTP(
        @Url url: String = BuildConfig.SERVER_URL + ":3004/api/auth/requestOtp", @Body otpReqParam: OtpRequestParam
    ): Response<UserResponse<Any?>>

    @POST
    @Headers("Accept: application/json")
    suspend fun verifyOTP(
        @Url url: String = BuildConfig.SERVER_URL + ":3004/api/auth/verifyOtp", @Body otpReqParam: OtpRequestParam
    ): Response<UserResponse<HashMap<String, String>>>

    @POST
    suspend fun resetPassword(@Url url: String, @Body map: HashMap<String, String>): Response<UserResponse<Any?>>

    @GET("/openmrs/ws/rest/v1/provider")
    suspend fun fetchUserProviderDetails(
        @Header("Authorization") authHeader: String, @Query("user") userId: String
    ): Response<List<SetupLocation>>

    @POST
    @Headers("Accept: application/json")
    suspend fun pushApiDetails(
        @Url url: String, @Header("Authorization") authHeader: String, @Body pushRequestApiCall: PushRequestApiCall
    ): Response<PushRequestApiCall>

    @GET
    suspend fun downloadPersonProfilePicture(
        @Url url: String, @Header("Authorization") authHeader: String
    ): Response<ResponseBody>

    @POST
    suspend fun uploadPersonProfilePicture(
        @Url url: String, @Header("Authorization") authHeader: String, @Body patientProfile: PatientProfile
    ): Response<ResponseBody>

    // Obs Image Download
    @GET
    suspend fun obsImageDownload(
        @Url url: String, @Header("Authorization") authHeader: String
    ): Response<ResponseBody>


    // Fetch obs response in json format.
    @POST
    @Multipart
    @Headers("Accept: application/json")
    suspend fun fetchObsJsonResponse(
        @Url url: String,
        @Header("Authorization") authHeader: String,
        @Part image: MultipartBody.Part,
        @Part("json") obsJsonRequest: Observation
    ): Response<ObsJsonResponse>

    @DELETE
    suspend fun deleteObsImage(@Url url: String, @Header("Authorization") authHeader: String)

//    @GET("/api/mindmap/download")
//    suspend fun downloadMindMap(@Query("key") licenseKey: String): Response<DownloadMindMapRes>
//
//    @GET("/intelehealth/app_update.json")
//    suspend fun checkAppUpdate(): Response<CheckAppUpdateRes>
//

//
//
//    @POST
//    suspend fun uploadProviderProfilePicture(
//        @Url url: String,
//        @Body providerProfile: ProviderProfile,
//        @Header("Authorization") authHeader: String
//    ): Response<ResponseBody>
//
//
//    @GET
//    suspend fun downloadProviderProfilePicture(
//        @Url url: String, @Header("Authorization") authHeader: String
//    ): Response<ResponseBody>
//
//    /*@GET
//    Observable<Profile> PROVIDER_PROFILE_DETAILS_DOWNLOAD(@Url String url,
//    @Header("Authorization") String authHeader);*/
//
//    @GET
//    suspend fun downloadProviderProfileDetails(
//        @Url url: String, @Header("Authorization") authHeader: String
//    ): Response<Profile>
//
//
//    @POST("/openmrs/ws/rest/v1/person/{userUuid}")
//    suspend fun updateProfileAge(
//        @Path("userUuid") userUuid: String,
//        @Body profileUpdateAge: ProfileUpdateAge,
//        @Header("Authorization") authHeader: String
//    ): Response<ResponseBody>
//
//
//    @POST("/openmrs/ws/rest/v1/provider/{userUuid}/attribute")
//    suspend fun createProfileAttribute(
//        @Path("userUuid") userUuid: String,
//        @Body profileCreateAttribute: ProfileCreateAttribute,
//        @Header("Authorization") authHeader: String
//    ): Response<ResponseBody>
//
//
//    @POST("attribute/{attributeUuid}")
//    suspend fun updateProfileAttribute(
//        @Path("attributeUuid") attributeUuid: String,
//        @Body profileUpdateAttribute: ProfileUpdateAttribute,
//        @Header("Authorization") authHeader: String
//    ): Response<ResponseBody>
//
//
//    @POST
//    @Headers("Accept: application/json")
//    suspend fun authLoginJwtApi(
//        @Url url: String, @Body authJWTBody: AuthJWTBody
//    ): Response<AuthJWTResponse>
//
//
//    /**
//     * getting html here like privacy policy, terms of service
//     *
//     * @param url
//     * @return
//     */
//    @GET
//    suspend fun getHtml(@Url url: String): Response<ResponseBody>
//
//
//    @DELETE("/api/mindmap/clearAll/{id}")
//    suspend fun clearAllNotifications(
//        @Header("Authorization") authHeader: String, @Path("id") id: String
//    ): Response<ResponseBody>
//
//
//    @PUT("/api/mindmap/acknowledge/{id}")
//    suspend fun notificationsAcknowledge(
//        @Header("Authorization") authHeader: String, @Path("id") id: String
//    ): Response<ResponseBody>
//
//
//    @GET("/api/mindmap/notifications")
//    suspend fun fetchAllNotifications(
//        @Header("Authorization") authHeader: String,
//        @Query("userId") userId: String,
//        @Query("page") page: String,
//        @Query("size") size: String
//    ): Response<NotificationResponse>

}