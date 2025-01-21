package org.intelehealth.app.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Vaghela Mithun R. on 15-01-2025 - 17:27.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
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