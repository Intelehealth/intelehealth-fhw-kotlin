package org.intelehealth.app.model.address;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * Represents a State's data, including its name, name in Hindi, and a list of districts.
 * <p>
 * This class is designed to hold structured data about a state,
 * which can be used for displaying information or for data processing.
 * It implements the {@link Serializable} interface to allow for easy
 * serialization and deserialization, such as when passing data between
 * activities or storing it in a cache.
 */
public class StateData implements Serializable {
    @Expose
    @SerializedName("state")
    private String state;

    @Expose
    @SerializedName("state-hi")
    private String stateHindi;

    @Expose
    @SerializedName("districts")
    private List<DistData> distDataList;

    /**
     * Gets the list of districts in the state.
     *
     * @return A {@link List} of {@link DistData} representing the districts.
     */
    public List<DistData> getDistDataList() {
        return distDataList;
    }

    /**
     * Sets the list of districts in the state.
     *
     * @param distDataList A {@link List} of {@link DistData} representing the districts.
     */
    public void setDistDataList(List<DistData> distDataList) {
        this.distDataList = distDataList;
    }

    /**
     * Gets the name of the state in English.
     *
     * @return The name of the state as a {@link String}.
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the name of the state in English.
     *
     * @param state The name of the state as a {@link String}.
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Gets the name of the state in Hindi.
     *
     * @return The name of the state in Hindi as a {@link String}.
     */
    public String getStateHindi() {
        return stateHindi;
    }

    /**
     * Sets the name of the state in Hindi.
     *
     * @param stateHindi The name of the state in Hindi as a {@link String}.
     */
    public void setStateHindi(String stateHindi) {
        this.stateHindi = stateHindi;
    }

    /**
     * Returns the name of the state, prioritizing the Hindi name if the current locale is Hindi.
     * <p>
     * This method overrides the default `toString()` method to provide a more user-friendly
     * representation of the StateData object. It checks the current locale's language. If it's Hindi,
     * it returns the `stateHindi`; otherwise, it returns the English `state`.
     *
     * @return The name of the state in the appropriate language.
     */
    @NonNull
    @Override
    public String toString() {
        if (Locale.getDefault().getLanguage().equals("hi")) {
            return stateHindi;
        } else {
            return state;
        }
    }
}
