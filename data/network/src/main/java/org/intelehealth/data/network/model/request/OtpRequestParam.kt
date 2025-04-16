package org.intelehealth.data.network.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Vaghela Mithun R. on 27-01-2025 - 16:04.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

const val OTP_FOR_USERNAME = "username"
const val OTP_FOR_MOBILE = "mobile"
const val OTP_FOR_PASSWORD = "password"

@Parcelize
data class OtpRequestParam(
    @SerializedName("otpFor") var otpFor: String? = null,

    @SerializedName("verifyFor") var verifyFor: String? = otpFor,

    @SerializedName("username") var userName: String? = null,

    @SerializedName("phoneNumber") var phoneNumber: String? = null,

    @SerializedName("countryCode") var countryCode: Int = 0,

    @SerializedName("email") var email: String? = null,

    @SerializedName("otp") var otp: String? = null
) : Parcelable