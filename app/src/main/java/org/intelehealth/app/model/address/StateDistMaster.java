package org.intelehealth.app.model.address;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a master list of states and their associated data.
 * <p>
 * This class is designed to hold a list of {@link StateData} objects,
 * providing a top-level structure for managing state-related information.
 * It implements the {@link Serializable} interface to allow for easy
 * serialization and deserialization, such as when passing data between
 * activities or storing it in a cache.
 */
public class StateDistMaster implements Serializable {
    @Expose
    @SerializedName("states")
    private List<StateData> stateDataList;

    /**
     * Gets the list of states and their associated data.
     *
     * @return A {@link List} of {@link StateData} representing the states.
     */
    public List<StateData> getStateDataList() {
        return stateDataList;
    }

    /**
     * Sets the list of states and their associated data.
     *
     * @param stateDataList A {@link List} of {@link StateData} representing the states.
     */
    public void setStateDataList(List<StateData> stateDataList) {
        this.stateDataList = stateDataList;
    }
}
