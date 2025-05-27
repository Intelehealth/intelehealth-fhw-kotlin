package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Vaghela Mithun R. on 15-04-2025 - 15:40.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Parcelize
@Entity
open class PersonAddress(
    @SerializedName("address1") var address1: String? = null,
    @SerializedName("address2") var address2: String? = null,
    @SerializedName("address3") var address3: String? = null,
    @SerializedName("address4") var address4: String? = null,
    @SerializedName("address5") var address5: String? = null,
    @SerializedName("address6") var address6: String? = null,
    @ColumnInfo("city_village") @SerializedName("cityvillage") var cityVillage: String? = null,
    @ColumnInfo("district") @SerializedName("countyDistrict") var district: String? = null,
    @ColumnInfo("state") @SerializedName("stateprovince") var state: String? = null,
    @ColumnInfo("postal_code") @SerializedName("postal_code") var postalCode: String? = null,
    @SerializedName("country") var country: String? = null,
    @SerializedName("address_of_hf") var addressOfHf: String? = null,
) : BaseEntity(), Parcelable {
    override fun toString(): String = Gson().toJson(this)

    fun getVillageWithoutDistrict(): String? {
        return splitVillageAndDistrict(1)
    }

    private fun splitVillageAndDistrict(index: Int): String? {
        try {
            cityVillage?.let {
                return@let if (it.isNotEmpty() && it.contains(":")) {
                    it.split(":".toRegex())[index]
                } else cityVillage
            }

            return if (index == 1) cityVillage
            else null
        } catch (e: Exception) {
            return null
        }
    }

    companion object {
        const val ADDRESS_FIELDS = "uuid, address1, address2, address3, address4, address5, address6, " +
                " city_village, district, state, country, postal_code, addressOfHf, synced, voided "
    }
}
