package org.intelehealth.data.provider.location

import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.data.network.RestClient
import org.intelehealth.data.provider.BaseDataSource
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 16-01-2025 - 14:10.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class LocationDataSource @Inject constructor(
    private val restClient: RestClient,
    private val networkHelper: NetworkHelper,
) : BaseDataSource(networkHelper = networkHelper) {
    fun getLocation() = getResult { restClient.fetchLocationList() }
}