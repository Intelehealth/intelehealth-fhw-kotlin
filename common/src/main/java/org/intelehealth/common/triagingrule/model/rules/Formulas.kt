package org.intelehealth.common.triagingrule.model.rules

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


data class Formulas(
    @SerializedName("name") var name: String,
    @SerializedName("expression") var expression: String,
    @SerializedName("result") var result: Int = 0
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}