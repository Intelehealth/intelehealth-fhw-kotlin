package org.intelehealth.app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Vaghela Mithun R. on 02-05-2025 - 11:30.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Parcelize
class ConsentArgs(
    var consentType: ConsentType,
    var url: String? = null,
    var screenTitle: String? = null,
) : Parcelable {
    /**
     * Represents the different types of consent that a user can provide.
     *
     * Each enum value corresponds to a specific type of consent, identified by a unique key.
     * These keys are used to reference the consent type in data storage or when interacting with
     * consent-related APIs.
     *
     * @property key The unique identifier for this consent type.
     */
    enum class ConsentType(val key: String) {
        /**
         * Consent for the processing of personal data.
         *
         * This consent type covers the user's agreement to how their personal data is collected,
         * used, stored, and processed.
         */
        PERSONAL_DATA_POLICY("personal_data_processing_policy"),

        /**
         * Consent for the privacy policy.
         *
         * This consent type covers the user's agreement to the terms outlined in the privacy policy.
         */
        PRIVACY_POLICY("privacy_policy"),

        /**
         * Consent for the terms and conditions.
         *
         * This consent type covers the user's agreement to the general terms and conditions of service.
         */
        TERMS_AND_CONDITIONS("terms_and_conditions"),

        /**
         * Consent for teleconsultation.
         *
         * This consent type covers the user's agreement to participate in remote consultations.
         */
        TELECONSULTATION("teleconsultation_consent"),

        /**
         * Consent for the terms of use.
         *
         * This consent type covers the user's agreement to the specific terms of use for the service.
         */
        TERMS_OF_USE("terms_of_use"),

        /**
         * Consent for the privacy notice.
         *
         * This consent type covers the user's acknowledgment of the privacy notice.
         */
        PRIVACY_NOTICE("privacy_notice"),

        /**
         * Consent for the collection and use of personal data.
         *
         * This consent type covers the user's explicit agreement to the collection and use of their personal data.
         */
        PERSONAL_DATA_CONSENT("personal_data_consent"),

        /**
         * Consent for the prescription disclaimer.
         *
         * This consent type covers the user's acknowledgment of the disclaimer related to prescriptions.
         */
        PRESCRIPTION_DISCLAIMER("prescription_disclaimer")
    }
}
