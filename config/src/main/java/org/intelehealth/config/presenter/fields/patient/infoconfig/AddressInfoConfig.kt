package org.intelehealth.config.presenter.fields.patient.infoconfig

import org.intelehealth.config.room.entity.PatientRegistrationFields

/**
 * Created by Vaghela Mithun R. on 13-07-2024 - 12:23.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class AddressInfoConfig {
    var postalCode: PatientRegistrationFields? = null
    var country: PatientRegistrationFields? = null
    var state: PatientRegistrationFields? = null
    var district: PatientRegistrationFields? = null
    var cityVillage: PatientRegistrationFields? = null
    var address1: PatientRegistrationFields? = null
    var address2: PatientRegistrationFields? = null
    var province: PatientRegistrationFields? = null
    var city: PatientRegistrationFields? = null
    var registrationAddressOfHf: PatientRegistrationFields? = null
    var block: PatientRegistrationFields? = null
    var householdNumber: PatientRegistrationFields? = null

    fun isStateActive(): Boolean = state?.isEnabled == true

    fun isDistrictActive(): Boolean = district?.isEnabled == true

    fun isCityVillageActive(): Boolean = cityVillage?.isEnabled == true

    fun isAddress1Active(): Boolean = address1?.isEnabled == true

    fun isAddress2Active(): Boolean = address2?.isEnabled == true

    fun isProvinceActive(): Boolean = province?.isEnabled == true

    fun isCityActive(): Boolean = city?.isEnabled == true

    fun isRegistrationAddressOfHfActive(): Boolean = registrationAddressOfHf?.isEnabled == true

    fun isBlockActive(): Boolean = block?.isEnabled == true

    fun isHouseholdNumberActive(): Boolean = householdNumber?.isEnabled == true

    fun isPostalCodeActive(): Boolean = postalCode?.isEnabled == true

    fun isCountryActive(): Boolean = country?.isEnabled == true

    fun isStateProvinceActive(): Boolean = isStateActive() || isProvinceActive()

    fun isStateProvinceMandatory(): Boolean {
        return (isStateActive() && state?.isMandatory == true) || (isProvinceActive() && province?.isMandatory == true)
    }
}
