package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize
import org.intelehealth.common.model.ListItemHeaderSection

/**
 * Created by Vaghela Mithun R. on 29-03-2024 - 19:04.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

/*data class LocalNotification(
    @PrimaryKey
    @SerializedName("uuid") var uuid: String,
    @SerializedName("description") var description: String? = null,
    @ColumnInfo("notification_type") @SerializedName("notification_type") var notificationType: String? = null,
    @ColumnInfo("obs_server_modified_date") @SerializedName("obs_server_modified_date")
    var obsServerModifiedDate: String? = null,
    @ColumnInfo("isdeleted") @SerializedName("isdeleted") var deleted: Boolean = false,
) : Parcelable,ListItemHeaderSection{
    override fun isHeader(): Boolean = false
}*/

/*@Parcelize*/

data class NotificationResponse (
    @SerializedName("total") var total: Int? = null,
    @SerializedName("rows") var rows: ArrayList<NotificationList> = arrayListOf(),
    @SerializedName("totalPages") var totalPages: Int? = null,
    @SerializedName("currentPage") var currentPage: Int? = null
)/*: Parcelable,*/

@Entity(tableName = "tbl_notifications")
@TypeConverters(PayloadConverter::class)
data class NotificationList(
    @PrimaryKey
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("user_uuid") var userUuid: String? = null,
    @SerializedName("payload") var payload: Payload? = Payload(),
    @SerializedName("type") var type: String? = null,
    @SerializedName("isRead") var isRead: String? = null,
    @SerializedName("createdAt") var createdAt: String? = null,
    @SerializedName("updatedAt") var updatedAt: String? = null
):ListItemHeaderSection{
    override fun isHeader(): Boolean = false
}

data class Data(

    @SerializedName("visitUuid") var visitUuid: String? = null,
    @SerializedName("patientUuid") var patientUuid: String? = null,
    @SerializedName("patientLastName") var patientLastName: String? = null,
    @SerializedName("followupDatetime") var followupDatetime: String? = null,
    @SerializedName("patientFirstName") var patientFirstName: String? = null,
    @SerializedName("patientOpenMrsId") var patientOpenMrsId: String? = null,
    @SerializedName("patientMiddleName") var patientMiddleName: String? = null

)


data class Payload(

    @SerializedName("body") var body: String? = null,
    @SerializedName("data") var data: Data? = Data(),
    @SerializedName("title") var title: String? = null,
    @SerializedName("regTokens") var regTokens: ArrayList<String> = arrayListOf()

)
class PayloadConverter {

    @TypeConverter
    fun fromPayload(payload: Payload?): String {
        return Gson().toJson(payload)
    }

    @TypeConverter
    fun toPayload(data: String): Payload {
        val type = object : TypeToken<Payload>() {}.type
        return Gson().fromJson(data, type)
    }
}