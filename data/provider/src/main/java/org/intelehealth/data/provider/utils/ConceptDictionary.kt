package org.intelehealth.data.provider.utils

/**
 * Created by Vaghela Mithun R. on 27-02-2025 - 16:58.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

enum class EncounterType(val value: String) {
    VISIT_COMPLETE("bd1fbfaa-f5fb-4ebd-b75c-564506fc309e"),
    PATIENT_EXIT_SURVEY("629a9d0b-48eb-405e-953d-a5964c88dc30"),
    EMERGENCY("ca5f5dc3-4f0b-4097-9cae-5cf2eb44a09c")
}

enum class ObsConcept(val value: String) {
    //    BLOOD_PRESSURE("5085AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"),
//    PULSE("5087AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"),
//    TEMPERATURE("5088AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"),
//    RESPIRATORY_RATE("5089AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"),
//    OXYGEN_SATURATION("5092AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"),
//    HEIGHT("5090AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"),
//    WEIGHT("5089AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"),
//    BLOOD_GLUCOSE("5086AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"),
    FOLLOW_UP("e8caffd6-5d22-41c4-8d6a-bc31a44d0c86"),
    RATING("78284507-fb71-4354-9b34-046ab205e18f"),
//    COMMENTS("36d207d6-bee7-4b3e-9196-7d053c6eddce"),
}

enum class PersonAttributeType(val value: String) {
    EMAIL("d472dd87-c406-4c3f-a7ac-7a36749cd0bf"),
    PHONE_NUMBER("c5ccb5ad-1d2c-4b54-b00b-baaf93aed302"),
    COUNTRY_CODE("a0358bfa-e220-412f-9fb3-504687135575")
}

enum class PersonIdentifier(val value: String) {
    IDENTIFIER_OPENMRS_ID("05a29f94-c0ed-11e2-94be-8c13b969e334"),
}

enum class ProviderRole(val value: String) {
    NURSE("73bbb069-9781-4afc-a9d1-54b6b2270e04"),
}
