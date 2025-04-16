package org.intelehealth.common.triagingrule.model.rules

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


data class ReferralRules(
    @SerializedName("name") var name: String,
    @SerializedName("condition") var condition: String,
    @SerializedName("result") var result: String
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}