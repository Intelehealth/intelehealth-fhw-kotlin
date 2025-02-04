package org.intelehealth.data.provider.consent

import org.intelehealth.common.helper.PreferenceHelper
import org.intelehealth.common.utility.PreferenceUtils
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 26-01-2025 - 18:24.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * The repository for managing consent-related data.
 *
 * This repository acts as an intermediary between the data sources (e.g., network, local storage)
 * and the higher-level components (e.g., ViewModels) that need to access consent data.
 * It abstracts the details of data retrieval and storage, providing a clean and consistent API
 * for interacting with consent information.
 *
 * The repository uses a [ConsentDataSource] to fetch consent data from remote sources and a
 * [PreferenceHelper] to store and retrieve consent pages locally.
 *
 * @property consentDataSource The data source for fetching consent data from remote sources.
 * @property preferenceHelper The helper for managing shared preferences, used for local storage.
 */
class ConsentRepository @Inject constructor(
    private val consentDataSource: ConsentDataSource,
    private val preferenceHelper: PreferenceHelper
) {

    /**
     * Fetches consent data from a remote source.
     *
     * This function delegates the task of fetching consent data to the [consentDataSource].
     * It returns a flow that emits the result of the network request.
     *
     * @param url The URL from which to fetch the consent data.
     * @return A flow that emits the result of the network request.
     */
    fun getConsent(url: String) = consentDataSource.getConsent(url)

    /**
     * Saves a consent page to local storage.
     *
     * This function uses the [preferenceHelper] to store the provided [value] associated with the given [key].
     *
     * @param key The key to associate with the consent page.
     * @param value The consent page content to store.
     */
    fun saveConsentPage(key: String, value: String) {
        preferenceHelper.save(key, value)
    }

    /**
     * Retrieves a consent page from local storage.
     *
     * This function uses the [preferenceHelper] to retrieve the consent page associated with the given [key].
     * If no value is found for the key, it returns an empty string.
     *
     * @param key The key associated with the consent page to retrieve.
     * @return The consent page content, or an empty string if not found.
     */
    fun getConsentPage(key: String): String {
        return preferenceHelper.get(key, "")
    }
}