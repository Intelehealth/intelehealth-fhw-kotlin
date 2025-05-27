package org.intelehealth.data.network.model.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Vaghela Mithun R. on 26-03-2025 - 13:10.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
data class PreferredName(
    @SerializedName("givenName")
    var givenName: String? = null,
    @SerializedName("middleName")
    val middleName: String? = null,
    @SerializedName("familyName")
    var familyName: String? = null
)
