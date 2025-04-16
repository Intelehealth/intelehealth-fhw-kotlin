package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_provider_attribute")
data class ProviderAttribute(

    @ColumnInfo("provider_uuid")
    @SerializedName("provideruuid", alternate = ["provider_uuid"])
    val providerUuid: String? = null,

    @ColumnInfo("provider_attribute_type_uuid")
    @SerializedName("attributetypeuuid", alternate = ["provider_attribute_type_uuid"])
    val providerAttrTypeUuid: String? = null,

    @SerializedName("value") val value: String? = null,
    @SerializedName("syncd") override var synced: Boolean = false
) : BaseEntity(), Parcelable
