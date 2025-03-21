package org.intelehealth.data.provider.protocols

import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.data.network.RestClient
import org.intelehealth.common.data.BaseDataSource
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 20-03-2025 - 12:33.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Data source for fetching and managing protocols.
 *
 * This class is responsible for interacting with the remote server to retrieve
 * protocol data. It uses a [RestClient] to make network requests and a
 * [NetworkHelper] to handle network connectivity checks. It extends [BaseDataSource]
 * to provide common functionality for handling network responses.
 *
 * @property restClient The [RestClient] used to make network requests.
 * @property networkHelper The [NetworkHelper] used to check network connectivity.
 */
class ProtocolsDataSource @Inject constructor(
    private val restClient: RestClient,
    networkHelper: NetworkHelper
) : BaseDataSource(networkHelper = networkHelper) {

    /**
     * Retrieves the download URL for mind map protocols.
     *
     * This function makes a network request to fetch the download URL for
     * mind map protocols using the provided license key and JWT token. It
     * uses the [RestClient.downloadMindMapProtocols] method to perform the
     * request and [getResult] to handle the network response.
     *
     * @param licenseKey The license key for accessing the protocols.
     * @param jwtToken The JWT token for authentication.
     * @return A [Result] object containing the response from the server.
     */
    fun getDownloadProtocolsUrl(licenseKey: String, jwtToken: String) = getResult {
        restClient.downloadMindMapProtocols(licenseKey = licenseKey, jwtToken = jwtToken)
    }
}
