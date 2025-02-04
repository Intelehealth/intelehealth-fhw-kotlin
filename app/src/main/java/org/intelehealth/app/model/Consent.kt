package org.intelehealth.app.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Vaghela Mithun R. on 15-01-2025 - 17:27.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Represents the consent information required for various aspects of the application.
 *
 * This class holds the text or URLs related to different consent agreements,
 * disclaimers, and policies that users may need to review and agree to.
 *
 * @property teleconsultationConsent The text or URL for the teleconsultation consent.
 * @property privacyNotice The text or URL for the privacy notice.
 * @property termsAndConditions The text or URL for the terms and conditions.
 * @property personalDataConsent The text or URL for the personal data consent.
 * @property prescriptionDisclaimer The text or URL for the prescription disclaimer.
 * @property privacyPolicy The text or URL for the privacy policy.
 * @property termsOfUse The text or URL for the terms of use.
 * @property personalDataProcessingPolicy The text or URL for the personal data processing policy.
 */
class Consent(
    @SerializedName("teleconsultation_consent") var teleconsultationConsent: String? = null,
    @SerializedName("privacy_notice") var privacyNotice: String? = null,
    @SerializedName("terms_and_conditions") var termsAndConditions: String? = null,
    @SerializedName("personal_data_consent") var personalDataConsent: String? = null,
    @SerializedName("prescription_disclaimer") var prescriptionDisclaimer: String? = null,
    @SerializedName("privacy_policy") var privacyPolicy: String? = null,
    @SerializedName("terms_of_use") var termsOfUse: String? = null,
    @SerializedName("personal_data_processing_policy") var personalDataProcessingPolicy: String? = null
)