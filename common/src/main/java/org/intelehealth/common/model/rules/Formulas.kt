package org.intelehealth.common.model.rules

import com.google.gson.annotations.SerializedName


data class Formulas(

    @SerializedName("name") var name: String? = null,
    @SerializedName("expression") var expression: String? = null,
    @SerializedName("result") var result: Int? = null

)