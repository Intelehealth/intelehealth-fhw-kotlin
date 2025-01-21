package org.intelehealth.config.data

import org.intelehealth.common.data.BaseDataSource
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.config.network.ConfigRestClient
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 10-04-2024 - 18:12.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class ConfigDataSource @Inject constructor(
    private val restClient: ConfigRestClient,
    private val networkHelper: NetworkHelper
) : BaseDataSource(networkHelper = networkHelper) {

    fun getConfig() = getResult { restClient.getPublishedConfig() }
}