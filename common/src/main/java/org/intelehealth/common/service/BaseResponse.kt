package org.intelehealth.common.service

import com.google.gson.annotations.SerializedName

open class BaseResponse<S, R>(
    @SerializedName("status") val status: S? = null,
    @SerializedName("statusCode") val statusCode: Int = 500,
    @SerializedName("data", alternate = ["token"]) val data: R? = null,
    @SerializedName("message") val message: String? = null,
)