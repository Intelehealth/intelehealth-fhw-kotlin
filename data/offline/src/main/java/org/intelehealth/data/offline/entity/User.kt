package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by - Prajwal W. on 26/09/24.
 * Email: prajwalwaingankar@gmail.com
 * Mobile: +917304154312
 **/

@Parcelize
@Entity(tableName = "tbl_user")
data class User(
    @PrimaryKey @ColumnInfo("uuid") @SerializedName("uuid") var userId: String,
    @ColumnInfo("display_name") @SerializedName("display") var displayName: String,
    @ColumnInfo("username") @SerializedName("username") var userName: String,
    @SerializedName("password") var password: String,
    @ColumnInfo("system_id") @SerializedName("systemId") var systemId: String,
    @ColumnInfo("provider_uuid") @SerializedName("provider_uuid") var providerId: String,
    @ColumnInfo("person_uuid") @SerializedName("person_uuid") var personId: String,
    @ColumnInfo("session_id") @SerializedName("sessionId") var sessionId: String,
    @ColumnInfo("first_login_in_time") @SerializedName("first_login_in_time") var firstLoginInTime: String,
    @ColumnInfo("last_login_in_time") @SerializedName("first_login_in_time") var lastLoginInTime: String
) : Parcelable