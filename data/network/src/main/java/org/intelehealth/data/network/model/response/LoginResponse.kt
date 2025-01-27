package org.intelehealth.data.network.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by - Prajwal W. on 14/10/24.
 * Email: prajwalwaingankar@gmail.com
 * Mobile: +917304154312
 **/
data class LoginResponse(
    @SerializedName("sessionId") @Expose val sessionId: String,

    @SerializedName("authenticated") @Expose val authenticated: Boolean,

    @SerializedName("user") @Expose val user: User,

    @SerializedName("currentProvider") @Expose val provider: Person
)

data class User(
    @SerializedName("uuid") @Expose val uuid: String,

    @SerializedName("display") @Expose val display: String,

    @SerializedName("username") @Expose val username: String,

    @SerializedName("systemId") @Expose val systemId: String,

    @SerializedName("person") @Expose val person: Person,
)

data class Person(
    @SerializedName("uuid") val uuid: String,

    @SerializedName("display") val display: String,
)



