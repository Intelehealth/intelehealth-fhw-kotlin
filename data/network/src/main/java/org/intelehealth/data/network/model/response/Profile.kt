package org.intelehealth.data.network.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Vaghela Mithun R. on 26-03-2025 - 13:06.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
data class Profile(
    @SerializedName("uuid")
    var uuid: String? = null,
    @SerializedName("person")
    val person: Person? = null,
    @SerializedName("attributes")
    val attributes: List<PersonAttributes>? = null
)
