package org.intelehealth.data.network

import okhttp3.ResponseBody
import org.intelehealth.common.service.BaseResponse
import org.intelehealth.data.network.model.SetupLocation
import org.intelehealth.data.network.model.request.DeviceTokenReq
import org.intelehealth.data.network.model.request.JWTParams
import org.intelehealth.data.network.model.request.OtpRequestParam
import org.intelehealth.data.network.model.request.PushRequest
import org.intelehealth.data.network.model.request.UserProfileEditableDetails
import org.intelehealth.data.network.model.response.LoginResponse
import org.intelehealth.data.network.model.response.PersonAttributes
import org.intelehealth.data.network.model.response.Profile
import org.intelehealth.data.network.model.response.PullResponse
import org.intelehealth.data.network.model.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Created by - Prajwal W. on 10/10/24.
 * Email: prajwalwaingankar@gmail.com
 * Mobile: +917304154312
 **/

/**
 * An interface defining the REST API endpoints for the application.
 *
 * This interface uses Retrofit annotations to define the structure of the API calls.
 * Each function in this interface represents a specific API endpoint and its associated
 * HTTP method, request parameters, and expected response type.
 *
 * The interface provides methods for:
 * - Fetching location lists.
 * - Generating JWT authentication tokens.
 * - Pulling data from the server.
 * - User login.
 * - Sending user device tokens.
 * - Retrieving HTML pages.
 * - Changing user passwords.
 * - Requesting and verifying OTPs.
 * - Resetting passwords.
 * - Fetching user provider details.
 * - Pushing API details.
 * - Downloading and uploading person profile pictures.
 * - Downloading observation images.
 * - Fetching observation JSON responses.
 * - Deleting observation images.
 *
 * All methods are defined as `suspend` functions, indicating that they are designed to be
 * used within Kotlin coroutines.
 */
interface RestClient {

    /**
     * Fetches a list of locations tagged as "Login Location".
     *
     * @return A [Response] containing a [HashMap] where the key is a [String] and the value is a [List] of [SetupLocation].
     */
    @GET("/openmrs/ws/rest/v1/location?tag=Login%20Location")
    suspend fun fetchLocationList(): Response<HashMap<String, List<SetupLocation>>>

    /**
     * Generates a JWT authentication token.
     *
     * @param url The URL for the authentication endpoint. Defaults to the server's login URL.
     * @param body The [JWTParams] containing the authentication credentials.
     * @return A [Response] containing a [BaseResponse] with a [Boolean] success indicator and a [String] message.
     */
    @POST
    suspend fun generateJWTAuthToken(
        @Url url: String = BuildConfig.SERVER_URL + ":3030/auth/login", @Body body: JWTParams
    ): Response<BaseResponse<Boolean, String>>

    /**
     * Pulls data from the server.
     *
     * @param header The authorization header.
     * @param locationId The ID of the location.
     * @param lastPullExecutedTime The timestamp of the last pull execution.
     * @param pageNo The page number for pagination.
     * @param pageLimit The maximum number of items per page.
     * @return A [Response] containing a [BaseResponse] with a [String] success indicator and a [PullResponse] data.
     */
    @GET("/EMR-Middleware/webapi/pull/pulldata/{locationId}/{lastPullExecutedTime}/{pageNo}/{pageLimit}")
    suspend fun pullData(
        @Header("Authorization") header: String,
        @Path("locationId") locationId: String,
        @Path("lastPullExecutedTime") lastPullExecutedTime: String,
        @Path("pageNo") pageNo: Int,
        @Path("pageLimit") pageLimit: Int
    ): Response<BaseResponse<String, PullResponse>>

    @POST("/EMR-Middleware/webapi/push/pushdata")
    suspend fun pushData(
        @Header("Authorization") header: String,
        @Body body: PushRequest
    ): Response<BaseResponse<String, PullResponse>>

    /**
     * Logs in a user.
     *
     * @param basicAuth The basic authentication header.
     * @return A [Response] containing a [LoginResponse].
     */
    @GET("/openmrs/ws/rest/v1/session")
    suspend fun login(@Header("Authorization") basicAuth: String): Response<LoginResponse>

    /**
     * Sends the user's device token to the server.
     *
     * @param url The URL for sending the device token. Defaults to the server's user settings URL.
     * @param deviceTokenReq The [DeviceTokenReq] containing the device token.
     * @return A [Response] containing a [ResponseBody].
     */
    @PUT
    suspend fun sendUserDeviceToken(
        @Url url: String = BuildConfig.SERVER_URL + ":3004/api/mindmap/user_settings",
        @Body deviceTokenReq: DeviceTokenReq
    ): Response<ResponseBody>

    /**
     * Retrieves an HTML page from the specified URL.
     *
     * @param url The URL of the HTML page.
     * @return A [Response] containing a [ResponseBody].
     */
    @GET
    fun getHtmlPage(@Url url: String?): Response<ResponseBody>

    /**
     * Changes the user's password.
     *
     * @param map A [HashMap] containing the password change parameters.
     * @param bearerToken The authorization header.
     * @return A [Response] containing a [ResponseBody].
     */
    @POST("/openmrs/ws/rest/v1/password")
    suspend fun changePassword(
        @Body map: HashMap<String, String>, @Header("Authorization") bearerToken: String
    ): Response<ResponseBody>

    /**
     * Requests an OTP (One-Time Password).
     *
     * @param url The URL for requesting the OTP. Defaults to the server's OTP request URL.
     * @param otpReqParam The [OtpRequestParam] containing the OTP request parameters.
     * @return A [Response] containing a [UserResponse] with an optional data of type [Any].
     */
    @POST
    @Headers("Accept: application/json")
    suspend fun requestOTP(
        @Url url: String = BuildConfig.SERVER_URL + ":3004/api/auth/requestOtp", @Body otpReqParam: OtpRequestParam
    ): Response<UserResponse<Any?>>

    /**
     * Verifies an OTP (One-Time Password).
     *
     * @param url The URL for verifying the OTP. Defaults to the server's OTP verification URL.
     * @param otpReqParam The [OtpRequestParam] containing the OTP verification parameters.
     * @return A [Response] containing a [UserResponse] with a [HashMap] containing verification details.
     */
    @POST
    @Headers("Accept: application/json")
    suspend fun verifyOTP(
        @Url url: String = BuildConfig.SERVER_URL + ":3004/api/auth/verifyOtp", @Body otpReqParam: OtpRequestParam
    ): Response<UserResponse<HashMap<String, String>>>

    /**
     * Resets the user's password.
     *
     * @param url The URL for resetting the password.
     * @param map A [HashMap] containing the password reset parameters.
     * @return A [Response] containing a [UserResponse] with an optional data of type [Any].
     */
    @POST
    suspend fun resetPassword(@Url url: String, @Body map: HashMap<String, String>): Response<UserResponse<Any?>>

    @GET
    suspend fun downloadMindMapProtocols(
        @Url url: String = BuildConfig.SERVER_URL + ":3004/api/mindmap/download",
        @Query("key") licenseKey: String,
        @Header("Authorization") jwtToken: String
    ): Response<BaseResponse<Any?, String>>

    @GET("/openmrs/ws/rest/v1/provider")
    suspend fun fetchUserProfile(
        @Query("user") userId: String,
        @Query("v") v: String = "custom:(uuid,person:(uuid,display,gender,age,birthdate,preferredName),attributes)",
        @Header("Authorization") authHeader: String
    ): Response<HashMap<String, List<Profile>>>

    @POST("/openmrs/ws/rest/v1/person/{userUuid}")
    suspend fun updateUserProfileEditableDetails(
        @Header("Authorization") authHeader: String,
        @Path("userUuid") personId: String,
        @Body editableDetails: UserProfileEditableDetails
    ): Response<Profile>

    @POST("/openmrs/ws/rest/v1/provider/{userUuid}/attribute")
    suspend fun createUserProfileAttribute(
        @Header("Authorization") authHeader: String,
        @Path("userUuid") providerId: String,
        @Body value: HashMap<String, String>
    ): Response<PersonAttributes>

    //
    @POST("/openmrs/ws/rest/v1/provider/{providerId}/attribute/{attributeUuid}")
    suspend fun updateUserProfileAttribute(
        @Header("Authorization") authHeader: String,
        @Path("providerId") providerId: String,
        @Path("attributeUuid") attributeUuid: String,
        @Body value: HashMap<String, String>
    ): Response<PersonAttributes>

    //
    @POST("openmrs/ws/rest/v1/personimage")
    suspend fun uploadProfilePicture(
        @Header("Authorization") authHeader: String,
        @Body value: HashMap<String, String>
    ): Response<PersonAttributes>
//
//    @POST
//    suspend fun uploadPersonProfilePicture(
//        @Url url: String, @Header("Authorization") authHeader: String, @Body patientProfile: PatientProfile
//    ): Response<ResponseBody>
//
//    // Obs Image Download
//    @GET
//    suspend fun obsImageDownload(
//        @Url url: String, @Header("Authorization") authHeader: String
//    ): Response<ResponseBody>
//
//
//    // Fetch obs response in json format.
//    @POST
//    @Multipart
//    @Headers("Accept: application/json")
//    suspend fun fetchObsJsonResponse(
//        @Url url: String,
//        @Header("Authorization") authHeader: String,
//        @Part image: MultipartBody.Part,
//        @Part("json") obsJsonRequest: Observation
//    ): Response<ObsJsonResponse>
//
//    @DELETE
//    suspend fun deleteObsImage(@Url url: String, @Header("Authorization") authHeader: String)


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
