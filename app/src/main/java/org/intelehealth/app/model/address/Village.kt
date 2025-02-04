package org.intelehealth.app.model.address

import com.google.gson.annotations.SerializedName
import java.util.Locale

/**
 * Created by Vaghela Mithun R. on 24-06-2024 - 20:54.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Represents a Village, which is a small rural settlement or community.
 *
 * This data class holds information about a village, including its name in English and Hindi.
 *
 * @property name The name of the village in English.
 * @property nameHindi The name of the village in Hindi.
 */
data class Village(
    val name: String?, @SerializedName("name-hi") val nameHindi: String?
) {
    /**
     * Returns the name of the village, prioritizing the Hindi name if the current locale is Hindi.
     *
     * This function overrides the default `toString()` method to provide a more user-friendly
     * representation of the Village object. It checks the current locale's language. If it's Hindi,
     * it returns the `nameHindi` if available; otherwise, it falls back to the English `name`.
     * If neither name is available, it returns "No Value".
     *
     * @return The name of the village in the appropriate language, or "No Value" if no name is available.
     */
    override fun toString(): String {
        return if ((Locale.getDefault().language == "hi")) nameHindi ?: name ?: "No Value"
        else name ?: "No Value"
    }
}