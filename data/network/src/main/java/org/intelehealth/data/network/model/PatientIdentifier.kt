package org.intelehealth.data.network.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Vaghela Mithun R. on 11-04-2025 - 12:32.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
data class PatientIdentifier(
    var identifierType: String? = null,
    var preferred: Boolean = false,
    @SerializedName("location")
    var locationId: String? = null,
)
