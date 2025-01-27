package org.intelehealth.common.model.rules

import com.google.gson.annotations.SerializedName


data class ReferralRules(

    @SerializedName("name") var name: String? = null,
    @SerializedName("condition") var condition: String? = null,
    @SerializedName("result") var result: String? = null

)