package org.intelehealth.app.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/** * Represents a doctor's profile in the application.
 *
 * This data class holds various attributes related to a doctor's profile,
 * including personal information, qualifications, contact details, and
 * professional experience.
 *
 * @property name The name of the doctor.
 * @property uuid The unique identifier for the doctor.
 * @property qualification The doctor's qualifications.
 * @property fontOfSign The font used for the doctor's signature.
 * @property whatsapp The WhatsApp contact number for the doctor.
 * @property registrationNumber The doctor's registration number.
 * @property consultationLanguage The language(s) in which the doctor can consult.
 * @property typeOfProfession The type of profession the doctor practices.
 * @property workExperience The doctor's work experience summary.
 * @property researchExperience The doctor's research experience summary.
 * @property textOfSign The text representation of the doctor's signature.
 * @property specialization The doctor's area of specialization.
 * @property phoneNumber The doctor's phone number.
 * @property countryCode The country code for the doctor's phone number.
 * @property emailId The doctor's email address.
 * @property workExperienceDetails Detailed description of the doctor's work experience.
 * @property signatureType Type of signature (e.g., digital, handwritten).
 * @property signature The actual signature image or representation.
 */
@Parcelize
data class DoctorProfile(

    @SerializedName("name") var name: String? = null,
    @SerializedName("uuid") var uuid: String? = null,
    @SerializedName("qualification") var qualification: String? = null,
    @SerializedName("fontOfSign") var fontOfSign: String? = null,
    @SerializedName("whatsapp") var whatsapp: String? = null,
    @SerializedName("registrationNumber") var registrationNumber: String? = null,
    @SerializedName("consultationLanguage") var consultationLanguage: String? = null,
    @SerializedName("typeOfProfession") var typeOfProfession: String? = null,
    @SerializedName("workExperience") var workExperience: String? = null,
    @SerializedName("researchExperience") var researchExperience: String? = null,
    @SerializedName("textOfSign") var textOfSign: String? = null,
    @SerializedName("specialization") var specialization: String? = null,
    @SerializedName("phoneNumber") var phoneNumber: String? = null,
    @SerializedName("countryCode") var countryCode: String? = null,
    @SerializedName("emailId") var emailId: String? = null,
    @SerializedName("workExperienceDetails") var workExperienceDetails: String? = null,
    @SerializedName("signatureType") var signatureType: String? = null,
    @SerializedName("signature") var signature: String? = null

) : Parcelable
