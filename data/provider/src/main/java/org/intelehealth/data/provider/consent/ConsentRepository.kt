package org.intelehealth.data.provider.consent

import org.intelehealth.common.helper.PreferenceHelper
import org.intelehealth.common.utility.PreferenceUtils
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 26-01-2025 - 18:24.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class ConsentRepository @Inject constructor(
    private val consentDataSource: ConsentDataSource, private val preferenceHelper: PreferenceHelper
) {
    fun getConsent(url: String) = consentDataSource.getConsent(url)

    fun saveConsentPage(key: String, value: String) {
        preferenceHelper.save(key, value)
    }

    fun getConsentPage(key: String): String {
        return preferenceHelper.get(key, "")
    }
}