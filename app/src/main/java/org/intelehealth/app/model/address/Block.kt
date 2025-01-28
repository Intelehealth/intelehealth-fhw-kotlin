package org.intelehealth.app.model.address

import com.google.gson.annotations.SerializedName
import java.util.Locale

/**
 * Created by Vaghela Mithun R. on 18-06-2024 - 19:27.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
data class Block(
    val name: String?,
    @SerializedName("name-hi")
    val nameHindi: String?,
    @SerializedName("Gram Panchayat")
    val gramPanchayats: List<GramPanchayat>?
) {
    override fun toString(): String {
        return if (Locale.getDefault().language == "hi") nameHindi ?: name ?: "No Value"
        else name ?: "No Value"
    }
}
