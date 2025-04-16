package org.intelehealth.app.model.address;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * Represents a District's data, including its name, name in Hindi,
 * a list of Tahasils, and lists of Blocks in both English and Hindi.
 * <p>
 * This class is designed to hold structured data about a district,
 * which can be used for displaying information or for data processing.
 * It implements the {@link Serializable} interface to allow for easy
 * serialization and deserialization, such as when passing data between
 * activities or storing it in a cache.
 */
public class DistData implements Serializable {
    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("name-hi")
    private String nameHindi;

    @Expose
    @SerializedName("tahasil")
    private List<String> tahasilList;

    @Expose
    @SerializedName("block")
    private List<Block> blocks;

    @Expose
    @SerializedName("block-hi")
    private List<Block> blocksHindi;

    /**
     * Gets the list of Tahasils in the district.
     *
     * @return A {@link List} of {@link String} representing the Tahasils.
     */
    public List<String> getTahasilList() {
        return tahasilList;
    }

    /**
     * Sets the list of Tahasils in the district.
     *
     * @param tahasilList A {@link List} of {@link String} representing the Tahasils.
     */
    public void setTahasilList(List<String> tahasilList) {
        this.tahasilList = tahasilList;
    }

    /**
     * Gets the name of the district in English.
     *
     * @return The name of the district as a {@link String}.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the district in English.
     *
     * @param name The name of the district as a {@link String}.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the district in Hindi.
     *
     * @return The name of the district in Hindi as a {@link String}.
     */
    public String getNameHindi() {
        return nameHindi;
    }

    /**
     * Sets the name of the district in Hindi.
     *
     * @param nameHindi The name of the district in Hindi as a {@link String}.
     */
    public void setNameHindi(String nameHindi) {
        this.nameHindi = nameHindi;
    }

    /**
     * Gets the list of Blocks in the district in English.
     *
     * @return A {@link List} of {@link Block} representing the Blocks.
     */
    public List<Block> getBlocks() {
        return blocks;
    }

    /**
     * Sets the list of Blocks in the district in English.
     *
     * @param blocks A {@link List} of {@link Block} representing the Blocks.
     */
    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    /**
     * Gets the list of Blocks in the district in Hindi.
     *
     * @return A {@link List} of {@link Block} representing the Blocks in Hindi.
     */
    public List<Block> getBlocksHindi() {
        return blocksHindi;
    }

    /**
     * Sets the list of Blocks in the district in Hindi.
     *
     * @param blocksHindi A {@link List} of {@link Block} representing the Blocks in Hindi.
     */
    public void setBlocksHindi(List<Block> blocksHindi) {
        this.blocksHindi = blocksHindi;
    }

    /**
     * Returns the name of the district, prioritizing the Hindi name if the current locale is Hindi.
     * <p>
     * This method overrides the default `toString()` method to provide a more user-friendly
     * representation of the DistData object. It checks the current locale's language. If it's Hindi,
     * it returns the `nameHindi`; otherwise, it returns the English `name`.
     *
     * @return The name of the district in the appropriate language.
     */
    @NonNull
    @Override
    public String toString() {
        if (Locale.getDefault().getLanguage().equals("hi")) {
            return nameHindi;
        } else {
            return name;
        }
    }
}
