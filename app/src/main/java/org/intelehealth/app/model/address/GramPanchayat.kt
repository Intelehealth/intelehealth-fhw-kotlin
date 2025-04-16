package org.intelehealth.app.model.address

import com.google.gson.annotations.SerializedName
import java.util.Locale

/**
 * Created by Vaghela Mithun R. on 18-06-2024 - 19:28.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Represents a Gram Panchayat, which is a village-level local self-governance body in India.
 *
 * This data class holds information about a Gram Panchayat, including its name in English and Hindi,
 * and a list of Villages within the Gram Panchayat.
 *
 * @property name The name of the Gram Panchayat in English.
 * @property nameHindi The name of the Gram Panchayat in Hindi.
 * @property villages A list of [Village] objects within this Gram Panchayat.
 */
data class GramPanchayat(
    val name: String?,
    @SerializedName("name-hi") val nameHindi: String?,
    @SerializedName("village") val villages: List<Village>?
) {
    /**
     * Returns the name of the Gram Panchayat, prioritizing the Hindi name if the current locale is Hindi.
     *
     * This function overrides the default `toString()` method to provide a more user-friendly
     * representation of the GramPanchayat object. It checks the current locale's language. If it's Hindi,
     * it returns the `nameHindi` if available; otherwise, it falls back to the English `name`.
     * If neither name is available, it returns "No Value".
     *
     * @return The name of the Gram Panchayat in the appropriate language, or "No Value" if no name is available.
     */
    override fun toString(): String {
        return if (Locale.getDefault().language == "hi") nameHindi ?: name ?: "No Value"
        else name ?: "No Value"
    }
}