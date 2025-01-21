package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_uuid_dictionary")
data class Concept(
    @PrimaryKey
    @SerializedName("uuid") var uuid: String,
    @SerializedName("name") var name: String? = null
) : Parcelable
