package org.intelehealth.data.provider.consent

import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.data.network.RestClient
import org.intelehealth.data.provider.BaseDataSource
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 26-01-2025 - 18:21.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class ConsentDataSource @Inject constructor(
    private val restClient: RestClient, private val networkHelper: NetworkHelper
) : BaseDataSource(networkHelper = networkHelper) {
    fun getConsent(url: String) = getResult { restClient.getHtmlPage(url) }
}