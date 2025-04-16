package org.intelehealth.data.provider.consent

import org.intelehealth.common.data.BaseDataSource
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.data.network.RestClient
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 26-01-2025 - 18:21.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

/**
 * The data source for fetching consent-related data from remote sources.
 *
 * This class is responsible for making network requests to retrieve consent data,
 * such as HTML pages containing terms and conditions or privacy policies.
 * It uses a [RestClient] to interact with the remote API and a [NetworkHelper] to
 * check for network connectivity.
 *
 * This class extends [BaseDataSource], which provides common functionality for handling
 * network requests and responses.
 *
 * @property restClient The REST client for making API calls.
 * @property networkHelper The helper for checking network connectivity.
 */
class ConsentDataSource @Inject constructor(
    private val restClient: RestClient, private val networkHelper: NetworkHelper
) : BaseDataSource(networkHelper = networkHelper) {

    /**
     * Fetches consent data from a remote URL.
     *
     * This function uses the [restClient] to make a network request to the specified [url]
     * and retrieve the HTML content of the consent page.
     * It uses the `getResult` function from [BaseDataSource] to handle the network request
     * and any potential errors.
     *
     * @param url The URL of the consent page to fetch.
     * @return A flow that emits the result of the network request.
     */
    fun getConsent(url: String) = getResult { restClient.getHtmlPage(url) }
}
