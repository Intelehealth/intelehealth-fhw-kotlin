package org.intelehealth.app.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Vaghela Mithun R. on 15-01-2025 - 13:14.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

/**
 * Represents the application's configuration settings.
 *
 * This data class holds various configuration flags and values that
 * control the behavior and appearance of the application.  Most of the
 * properties are boolean flags indicating whether a particular feature
 * or field is enabled or visible.
 *
 * @property mFirstName Indicates whether the first name field is enabled.
 * @property mMiddleName Indicates whether the middle name field is enabled.
 * @property mLastName Indicates whether the last name field is enabled.
 * @property mGenderM Indicates whether the male gender option is enabled.
 * @property mGenderF Indicates whether the female gender option is enabled.
 * @property mDOB Indicates whether the date of birth field is enabled.
 * @property mAge Indicates whether the age field is enabled.
 * @property mPhoneNum Indicates whether the phone number field is enabled.
 * @property mCountry The default country code or identifier.
 * @property countryText Indicates whether the country name text is enabled.
 * @property mState The default state code or identifier.
 * @property stateText Indicates whether the state name text is enabled.
 * @property mCity Indicates whether the city field is enabled.
 * @property mAddress1 Indicates whether the address line 1 field is enabled.
 * @property mAddress2 Indicates whether the address line 2 field is enabled.
 * @property mOccupation Indicates whether the occupation field is enabled.
 * @property casteLayout Indicates whether the caste selection layout is enabled.
 * @property educationLayout Indicates whether the education selection layout is enabled.
 * @property economicLayout Indicates whether the economic status selection layout is enabled.
 * @property mPostal Indicates whether the postal code field is enabled.
 * @property countryStateLayout Indicates whether the combined country and state selection layout is enabled.
 * @property mRelationship Indicates whether the relationship status field is enabled.
 * @property mHeight Indicates whether the height field is enabled.
 * @property mWeight Indicates whether the weight field is enabled.
 * @property mPulse Indicates whether the pulse rate field is enabled.
 * @property mBpSys Indicates whether the systolic blood pressure field is enabled.
 * @property mBpDia Indicates whether the diastolic blood pressure field is enabled.
 * @property mTemperature Indicates whether the temperature field is enabled.
 * @property mCelsius Indicates whether the Celsius temperature unit option is enabled.
 * @property mFahrenheit Indicates whether the Fahrenheit temperature unit option is enabled.
 * @property mSpo2 Indicates whether the SpO2 (blood oxygen saturation) field is enabled.
 * @property mBMI Indicates whether the BMI (body mass index) field is enabled.
 * @property mResp Indicates whether the respiration rate field is enabled.
 * @property healthSchemeCard Indicates whether the health scheme card information section is enabled.
 * @property videoLibrary A URL or identifier for the video library content.
 * @property privacyNotice Indicates whether the privacy notice is enabled or required.
 */
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
