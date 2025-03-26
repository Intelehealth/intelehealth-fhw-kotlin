package org.intelehealth.data.network.model.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Vaghela Mithun R. on 26-03-2025 - 13:28.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

data class Person(
    @SerializedName("uuid") val uuid: String,
    @SerializedName("display") val display: String,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("birthdate") val dateOfBirth: String? = null,
    @SerializedName("preferredName") val preferredName: PreferredName? = null
)
