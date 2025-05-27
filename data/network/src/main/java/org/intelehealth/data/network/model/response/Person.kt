package org.intelehealth.data.network.model.response

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.intelehealth.data.offline.entity.PersonAddress

/**
 * Created by Vaghela Mithun R. on 26-03-2025 - 13:28.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

data class Person(
    @SerializedName("uuid") var uuid: String,
    @SerializedName("display") var display: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("age") var age: Int? = null,
    @SerializedName("birthdate") var dateOfBirth: String? = null,
    @SerializedName("preferredName") var preferredName: PreferredName? = null,
    @SerializedName("names") @Expose
    var names: List<PreferredName>? = null,
    @SerializedName("attributes")
    var attributes: List<Map<String, String>>? = null,
    @SerializedName("addresses")
    var addresses: List<PersonAddress>? = null
){
    override fun toString(): String = Gson().toJson(this)
}
