package org.intelehealth.data.provider.protocols

import org.intelehealth.common.utility.PreferenceUtils
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 20-03-2025 - 12:31.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class ProtocolsRepository @Inject constructor(
    private val protocolsDataSource: ProtocolsDataSource,
    private val preferenceUtils: PreferenceUtils
) {
    fun getDownloadProtocolsUrl(licenseKey: String) = protocolsDataSource.getDownloadProtocolsUrl(
        licenseKey = licenseKey,
        jwtToken = "Bearer ${preferenceUtils.jwtToken}"
    )
}
