package org.intelehealth.common.model.rules

import com.google.gson.annotations.SerializedName


data class Fields(

    @SerializedName("key") var key: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("value") var value: Int? = null

)