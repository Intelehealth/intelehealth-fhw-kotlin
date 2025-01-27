package org.intelehealth.data.provider.sync.data

import com.google.gson.Gson
import org.intelehealth.common.utility.PreferenceUtils
import org.intelehealth.data.network.RestClient
import org.intelehealth.data.network.model.SetupLocation
import org.intelehealth.data.provider.BaseDataSource
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 25-11-2024 - 14:05.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class SyncDataSource @Inject constructor(
    private val apiClient: RestClient, private val preferenceUtils: PreferenceUtils
) : BaseDataSource() {
    fun pullData(pageNo: Int, pageLimit: Int) = getResult {
        val locationStr = preferenceUtils.location
        val location = Gson().fromJson(locationStr, SetupLocation::class.java)
        location?.uuid ?: throw Exception("Location not found")
        apiClient.pullData(
            preferenceUtils.basicAuthToken, location.uuid!!, preferenceUtils.lastSyncedTime, pageNo, pageLimit
        )
    }
}