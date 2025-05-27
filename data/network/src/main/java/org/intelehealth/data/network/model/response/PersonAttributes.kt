package org.intelehealth.data.network.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Vaghela Mithun R. on 26-03-2025 - 13:08.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
data class PersonAttributes(
    @SerializedName("uuid") var uuid: String? = null,
    @SerializedName("display") val display: String? = null,
    @SerializedName("value") val value: String? = null,
    @SerializedName("attributeType") val attributeTpe: PersonAttributes? = null,
    @SerializedName("voided") val voided: Boolean = false
)

//data class AttributeType(
//    @SerializedName("uuid") var uuid: String? = null,
//    @SerializedName("display") val display: String? = null,
//)
