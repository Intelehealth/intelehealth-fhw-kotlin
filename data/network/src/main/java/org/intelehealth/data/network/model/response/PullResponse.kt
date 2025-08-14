package org.intelehealth.data.network.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.intelehealth.data.offline.entity.Encounter
import org.intelehealth.data.offline.entity.Observation
import org.intelehealth.data.offline.entity.PatientAttribute
import org.intelehealth.data.offline.entity.PatientAttributeTypeMaster
import org.intelehealth.data.offline.entity.PatientLocation
import org.intelehealth.data.offline.entity.Provider
import org.intelehealth.data.offline.entity.ProviderAttribute
import org.intelehealth.data.offline.entity.Visit
import org.intelehealth.data.offline.entity.VisitAttribute

/**
 * Created by - Prajwal W. on 14/10/24.
 * Email: prajwalwaingankar@gmail.com
 * Mobile: +917304154312
 **/
data class PullResponse(

    @SerializedName("patientlist") @Expose var patients: List<PatientWithAddress>,

    @SerializedName("pullexecutedtime") @Expose var pullExecutedTime: String,

    @SerializedName("patientAttributeTypeListMaster") @Expose var patientAttributeTypeListMaster: List<PatientAttributeTypeMaster>,

    @SerializedName("patientAttributesList") @Expose var patientAttributesList: List<PatientAttribute>,

    @SerializedName("visitlist") @Expose var visitlist: List<Visit>,

    @SerializedName("encounterlist") @Expose var encounterlist: List<Encounter>,

    @SerializedName("obslist") @Expose var obslist: List<Observation>,

    @SerializedName("locationlist") @Expose var locationlist: List<PatientLocation>,

    @SerializedName("providerlist") @Expose var providerlist: List<Provider>,

    @SerializedName("providerAttributeTypeList") @Expose var providerAttributeTypeList: List<ProviderAttribute>,

    @SerializedName("providerAttributeList") @Expose var providerAttributeList: List<ProviderAttribute>,

    @SerializedName("visitAttributeTypeList") @Expose var visitAttributeTypeList: List<VisitAttribute>,

    @SerializedName("visitAttributeList") @Expose var visitAttributeList: List<VisitAttribute>,

    @SerializedName("pageNo") @Expose var pageNo: Int,

    @SerializedName("totalCount") @Expose var totalCount: Int,

    /*@SerializedName("propertyContents") @Expose
     var propertyContents: ConfigResponse*/
)
