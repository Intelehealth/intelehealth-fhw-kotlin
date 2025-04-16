package org.intelehealth.data.network.model.request

import java.util.Locale.IsoCountryCode

/**
 * Created by Vaghela Mithun R. on 28-03-2025 - 13:44.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
data class UserProfileEditableDetails(
    var age: String? = null,
    var birthdate: String? = null,
    var gender: String? = null,
    var email: String? = null,
    var countryCode: String? = null,
    var phoneNumber: String? = null,
    var profilePicture: String? = null
)
