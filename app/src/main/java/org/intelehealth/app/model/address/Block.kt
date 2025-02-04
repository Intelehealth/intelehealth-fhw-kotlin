package org.intelehealth.app.model.address

import com.google.gson.annotations.SerializedName
import java.util.Locale

/**
 * Created by Vaghela Mithun R. on 18-06-2024 - 19:27.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Represents a Block, which is a geographical administrative division.
 *
 * This data class holds information about a block, including its name in English and Hindi,
 * and a list of Gram Panchayats within the block.
 *
 * @property name The name of the block in English.
 * @property nameHindi The name of the block in Hindi.
 * @property gramPanchayats A list of [GramPanchayat] objects within this block.
 */
data class Block(
    val name: String?,
    @SerializedName("name-hi") val nameHindi: String?,
    @SerializedName("Gram Panchayat") val gramPanchayats: List<GramPanchayat>?
) {
    /**
     * Returns the name of the block, prioritizing the Hindi name if the current locale is Hindi.
     *
     * This function overrides the default `toString()` method to provide a more user-friendly
     * representation of the Block object. It checks the current locale's language. If it's Hindi,
     * it returns the `nameHindi` if available; otherwise, it falls back to the English `name`.
     * If neither name is available, it returns "No Value".
     *
     * @return The name of the block in the appropriate language, or "No Value" if no name is available.
     */
    override fun toString(): String {
        return if (Locale.getDefault().language == "hi") nameHindi ?: name ?: "No Value"
        else name ?: "No Value"
    }
}
