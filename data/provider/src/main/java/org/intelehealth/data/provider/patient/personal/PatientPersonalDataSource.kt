package org.intelehealth.data.provider.patient.personal

import org.intelehealth.common.data.BaseDataSource
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.data.network.RestClient
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 20-12-2024 - 20:16.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

class PatientPersonalDataSource @Inject constructor(
    @Suppress("UnusedPrivateProperty")
    private val restClient: RestClient,
    networkHelper: NetworkHelper
) : BaseDataSource(networkHelper = networkHelper) {

    fun uploadPatientProfilePicture(basicToken: String, map: HashMap<String, String>) = getResult {
        restClient.uploadProfilePicture(basicToken, map)
    }
}
