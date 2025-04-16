package org.intelehealth.common.triagingrule.model.rules

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


data class Fields(
    @SerializedName("key") var key: String,
    @SerializedName("name") var name: String,
    @SerializedName("type") var type: String,
    @SerializedName("value") var value: Int = 0
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}