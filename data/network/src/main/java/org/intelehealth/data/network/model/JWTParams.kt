package org.intelehealth.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Vaghela Mithun R. on 16-01-2025 - 17:38.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
data class JWTParams(
    @SerializedName("username") var username: String,
    @SerializedName("password") val password: String,
    @SerializedName("rememberme") var rememberme: Boolean = true
)