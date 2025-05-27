package org.intelehealth.data.provider.sync.data

import android.content.res.Resources.NotFoundException
import com.google.gson.Gson
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.common.utility.PreferenceUtils
import org.intelehealth.data.network.RestClient
import org.intelehealth.data.network.model.SetupLocation
import org.intelehealth.common.data.BaseDataSource
import org.intelehealth.data.network.model.request.PushRequest
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 25-11-2024 - 14:05.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class SyncDataSource @Inject constructor(
    private val apiClient: RestClient,
    private val preferenceUtils: PreferenceUtils,
    networkHelper: NetworkHelper
) : BaseDataSource(networkHelper = networkHelper) {

    fun pullData(
        basicToken: String,
        locationId: String,
        lastSyncedTime: String,
        pageNo: Int,
        pageLimit: Int
    ) = getResult {
        apiClient.pullData(basicToken, locationId, lastSyncedTime, pageNo, pageLimit)
    }

    fun pushData(basicToken: String, pushRequest: PushRequest) = getResult {
        apiClient.pushData(basicToken, pushRequest)
    }
}
