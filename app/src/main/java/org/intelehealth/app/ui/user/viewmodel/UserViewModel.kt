package org.intelehealth.app.ui.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.intelehealth.app.utility.BiometricAuth
import org.intelehealth.common.extensions.containsDigit
import org.intelehealth.common.extensions.hide
import org.intelehealth.common.extensions.toBase64
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.common.state.Result
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.common.utility.CommonConstants.MIN_PASSWORD_LENGTH
import org.intelehealth.common.utility.DateTimeUtils
import org.intelehealth.data.network.model.request.JWTParams
import org.intelehealth.data.network.model.request.OtpRequestParam
import org.intelehealth.data.network.model.request.UserProfileEditableDetails
import org.intelehealth.data.network.model.response.LoginResponse
import org.intelehealth.data.network.model.response.PersonAttributes
import org.intelehealth.data.network.model.response.UserResponse
import org.intelehealth.data.offline.entity.User
import org.intelehealth.data.provider.user.UserRepository
import org.intelehealth.data.provider.utils.PersonAttributeType
import retrofit2.Response
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
    private val userRepository: UserRepository,
    private val biometricAuth: BiometricAuth,
    networkHelper: NetworkHelper
) : BaseViewModel(networkHelper = networkHelper) {

    private val mutableFingerprintAppLock = MutableLiveData(userRepository.fingerprintAppLock())
    val fingerprintAppLock = mutableFingerprintAppLock.hide()

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

    lateinit var user: User

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


    fun fetchUserProfile() = executeNetworkCallAndSaveInLocal(
        { userRepository.fetchUserProfile() },
        { userRepository.saveProfileData(it) }
    ).asLiveData()

//    fun fetchUserProfile() = viewModelScope.launch { userRepository.fetchUserProfile() }

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
        @Suppress("UnusedPrivateProperty") for (i in 0 until MIN_PASSWORD_LENGTH) {
            sb.append(pattern[rnd.nextInt(pattern.length)])
        }

        return if (sb.toString().containsDigit()) {
            sb.toString()
        } else generatePassword()
    }

    /**
     * Updates the user's profile information.
     *
     * This function handles updating various user profile attributes, including
     * profile picture, email, phone number, and country code. It uses a queue
     * to manage network calls for each attribute update and executes them
     * sequentially.
     *
     * @param user The [User] object representing the user whose profile is being updated.
     * @param editableDetails A [UserProfileEditableDetails] object containing the
     *   attributes to be updated.  If a field in this object is null, it will be
     *   skipped and not updated.
     * @param onResult A callback function that is invoked with the updated
     *   [PersonAttributes] if the update is successful.
     */
    fun updateUserProfile(
        user: User,
        editableDetails: UserProfileEditableDetails,
        onResult: (PersonAttributes) -> Unit
    ) = viewModelScope.launch {
        val callQueue = ArrayList<suspend () -> Response<PersonAttributes>>()
        // Update profile picture if provided
        withContext(Dispatchers.IO) {
            editableDetails.profilePicture?.let {
                async { it.toBase64() }.await()?.let { image ->
                    callQueue.add { userRepository.updateUserProfilePicture(user.personId, image) }
                } ?: Timber.d { "Image didn't converted into base64" }
            } ?: Timber.d { "Image is null" }
            Timber.d { "Image process" }
        }

        // Update email if provided
        withContext(Dispatchers.IO) {
            editableDetails.email?.let {
                attributeTypeCall(user, it, PersonAttributeType.EMAIL) { callQueue.add { it } }
            }
            Timber.d { "email process" }
        }

        // Update phone number if provided
        withContext(Dispatchers.IO) {
            editableDetails.phoneNumber?.let {
                attributeTypeCall(user, it, PersonAttributeType.PHONE_NUMBER) { callQueue.add { it } }
            }
            Timber.d { "phoneNumber process" }
        }

        // Update country code if provided
        withContext(Dispatchers.IO) {
            editableDetails.countryCode?.let {
                attributeTypeCall(user, it, PersonAttributeType.COUNTRY_CODE) { callQueue.add { it } }
            }
            Timber.d { "countryCode process" }
        }

        // Execute network calls in queue and handle the response
        withContext(Dispatchers.IO) {
            executeNetworkCallInQueue(callQueue).collect { handleResponse(it) { success -> onResult(success) } }
            Timber.d { "executeNetworkCallInQueue process" }
        }
    }


    /**
     * Determines the appropriate network call for updating or creating a user attribute.
     *
     * This function checks if a given user attribute (email, phone number, or country code)
     * needs to be updated (if it exists and is different) or created (if it doesn't exist).
     * It then invokes the provided [networkCall] lambda with the corresponding API call
     * to either update or create the attribute.
     *
     * @param user The [User] object representing the user whose attribute is being modified.
     * @param value The new value for the attribute.
     * @param type The [PersonAttributeType] indicating the type of attribute being modified
     *   (e.g., EMAIL, PHONE_NUMBER, COUNTRY_CODE).
     * @param networkCall A lambda function that accepts a [Response<PersonAttributes>]
     *   representing the network call to be executed.  This lambda should add the
     *   provided call to a queue or execute it directly.
     */
    private suspend fun attributeTypeCall(
        user: User,
        value: String,
        type: PersonAttributeType,
        networkCall: suspend (Response<PersonAttributes>) -> Unit
    ) {

        // Check if the attribute is editable (exists and the new value is different)
        val isEditable = when (type) {
            PersonAttributeType.EMAIL -> !user.emailId.isNullOrEmpty() && value != user.emailId
            PersonAttributeType.PHONE_NUMBER -> !user.phoneNumber.isNullOrEmpty() && value != user.phoneNumber
            PersonAttributeType.COUNTRY_CODE -> !user.countryCode.isNullOrEmpty() && value != user.countryCode
        }

        // Check if the attribute is new (doesn't exist)
        val isNew = when (type) {
            PersonAttributeType.EMAIL -> !isEditable && user.emailId.isNullOrEmpty()
            PersonAttributeType.PHONE_NUMBER -> !isEditable && user.phoneNumber.isNullOrEmpty()
            PersonAttributeType.COUNTRY_CODE -> !isEditable && user.countryCode.isNullOrEmpty()
        }

        // Invoke the appropriate network call based on whether the attribute is editable or new
        if (isEditable) networkCall(getUpdateAttributeCall(value, type.value, user.providerId))
        else if (isNew) networkCall(getCreateAttributeCall(value, type.value, user.providerId))
    }

    /**
     * Creates a network call to create a new user profile attribute.
     *
     * This function constructs and returns a network call (likely a Retrofit call)
     * that, when executed, will create a new attribute for the user's profile.
     *
     * @param value The value of the attribute to be created.
     * @param typeId The identifier for the type of attribute (e.g., "email", "phone_number").
     *   This should correspond to the value expected by your API.
     * @param providerId The identifier for the user's provider.
     * @return A [Response<PersonAttributes>] representing the network call to create the attribute.
     *   When this call is executed, it will return a response containing the updated
     *   [PersonAttributes] if successful.
     */
    private suspend fun getCreateAttributeCall(
        value: String,
        typeId: String,
        providerId: String
    ) = userRepository.createUserProfileAttribute(
        providerId, typeId, value
    )

    /**
     * Creates a network call to update an existing user profile attribute.
     *
     * This function constructs and returns a network call (likely a Retrofit call)
     * that, when executed, will update an existing attribute in the user's profile.
     *
     * @param value The new value for the attribute.
     * @param typeId The identifier for the type of attribute being updated (e.g., "email", "phone_number").
     *   This should correspond to the value expected by your API.
     * @param providerId The identifier for the user's provider.
     * @return A [Response<PersonAttributes>] representing the network call to update the attribute.
     *   When this call is executed, it will return a response containing the updated
     *   [PersonAttributes] if successful.
     */
    private suspend fun getUpdateAttributeCall(
        value: String,
        typeId: String,
        providerId: String
    ) = userRepository.updateUserProfileAttribute(
        providerId, typeId, value
    )

    /**
     * Changes the state of the fingerprint app lock.
     *
     * This function updates the application's fingerprint lock state, both in the
     * user interface (via [mutableFingerprintAppLock]) and persistently (via
     * [userRepository.changeFingerprintAppLockState]).
     *
     * @param state A boolean value representing the new state of the fingerprint
     *   app lock. `true` enables the lock, `false` disables it.
     */
    fun changeFingerprintAppLockState(state: Boolean) {
        mutableFingerprintAppLock.postValue(state)
        userRepository.changeFingerprintAppLockState(state)
    }

    /**
     * Checks if biometric authentication (e.g., fingerprint, face unlock) is available on the device.
     *
     * This function delegates the check to the [biometricAuth] object and returns
     * its result.
     *
     * @return `true` if biometric authentication is available, `false` otherwise.
     */
    fun isBiometricAvailable() = biometricAuth.isBiometricAvailable()

    /**
     * Contains constant values related to OTP (One-Time Password) handling.
     */
    companion object {
        /**
         * The default expiry time for an OTP, in milliseconds (60 seconds).
         */
        const val OTP_EXPIRY_TIME = 60 * 1000L

        /**
         * The time interval for checking OTP expiry, in milliseconds (1 second).
         */
        const val OTP_EXPIRY_TIME_INTERVAL = 1000L
    }

}
