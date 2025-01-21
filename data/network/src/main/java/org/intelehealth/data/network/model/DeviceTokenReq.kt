package org.intelehealth.data.network.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Vaghela Mithun R. on 20-01-2025 - 18:20.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
data class DeviceTokenReq(
    @SerializedName("user_uuid") private val userId: String,
    @SerializedName("local") private val local: String,
    @SerializedName("data") private val deviceToken: HashMap<String, String>
) {
    constructor(userId: String, local: String, deviceToken: String) : this(
        userId, local, hashMapOf("device_reg_token" to deviceToken)
    )
}