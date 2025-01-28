package org.intelehealth.app.model.address

import com.google.gson.annotations.SerializedName
import java.util.Locale

/**
 * Created by Vaghela Mithun R. on 24-06-2024 - 20:54.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
data class Village(
    val name: String?,
    @SerializedName("name-hi")
    val nameHindi: String?
) {
    override fun toString(): String {
        return if ((Locale.getDefault().language == "hi")) nameHindi ?: name ?: "No Value"
        else name ?: "No Value"
    }
}