package org.intelehealth.app.model.address;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.io.Serializable;
import java.util.List;
import java.util.Locale;

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

    public List<DistData> getDistDataList() {
        return distDataList;
    }

    public void setDistDataList(List<DistData> distDataList) {
        this.distDataList = distDataList;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateHindi() {
        return stateHindi;
    }

    public void setStateHindi(String stateHindi) {
        this.stateHindi = stateHindi;
    }

    @NonNull
    @Override
    public String toString() {
        if (Locale.getDefault().getLanguage().equals("hi")) {
            return stateHindi;
        } else return state;
    }
}
