package org.intelehealth.app.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Vaghela Mithun R. on 15-01-2025 - 13:14.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
data class AppConfig(

    @SerializedName("mFirstName") var mFirstName: Boolean? = null,
    @SerializedName("mMiddleName") var mMiddleName: Boolean? = null,
    @SerializedName("mLastName") var mLastName: Boolean? = null,
    @SerializedName("mGenderM") var mGenderM: Boolean? = null,
    @SerializedName("mGenderF") var mGenderF: Boolean? = null,
    @SerializedName("mDOB") var mDOB: Boolean? = null,
    @SerializedName("mAge") var mAge: Boolean? = null,
    @SerializedName("mPhoneNum") var mPhoneNum: Boolean? = null,
    @SerializedName("mCountry") var mCountry: String? = null,
    @SerializedName("countryText") var countryText: Boolean? = null,
    @SerializedName("mState") var mState: String? = null,
    @SerializedName("stateText") var stateText: Boolean? = null,
    @SerializedName("mCity") var mCity: Boolean? = null,
    @SerializedName("mAddress1") var mAddress1: Boolean? = null,
    @SerializedName("mAddress2") var mAddress2: Boolean? = null,
    @SerializedName("mOccupation") var mOccupation: Boolean? = null,
    @SerializedName("casteLayout") var casteLayout: Boolean? = null,
    @SerializedName("educationLayout") var educationLayout: Boolean? = null,
    @SerializedName("economicLayout") var economicLayout: Boolean? = null,
    @SerializedName("mPostal") var mPostal: Boolean? = null,
    @SerializedName("countryStateLayout") var countryStateLayout: Boolean? = null,
    @SerializedName("mRelationship") var mRelationship: Boolean? = null,
    @SerializedName("mHeight") var mHeight: Boolean? = null,
    @SerializedName("mWeight") var mWeight: Boolean? = null,
    @SerializedName("mPulse") var mPulse: Boolean? = null,
    @SerializedName("mBpSys") var mBpSys: Boolean? = null,
    @SerializedName("mBpDia") var mBpDia: Boolean? = null,
    @SerializedName("mTemperature") var mTemperature: Boolean? = null,
    @SerializedName("mCelsius") var mCelsius: Boolean? = null,
    @SerializedName("mFahrenheit") var mFahrenheit: Boolean? = null,
    @SerializedName("mSpo2") var mSpo2: Boolean? = null,
    @SerializedName("mBMI") var mBMI: Boolean? = null,
    @SerializedName("mResp") var mResp: Boolean? = null,
    @SerializedName("health_scheme_card") var healthSchemeCard: Boolean? = null,
    @SerializedName("video_library") var videoLibrary: String? = null,
    @SerializedName("privacyNotice") var privacyNotice: Boolean? = null
)