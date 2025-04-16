package org.intelehealth.app.ui.onboarding.viewmodel

import android.content.res.AssetManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.intelehealth.app.model.Consent
import org.intelehealth.common.extensions.hide
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.data.provider.consent.ConsentRepository
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.Locale
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 15-01-2025 - 15:17.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

/**
 * The ViewModel for managing consent-related data and operations.
 *
 * This ViewModel is responsible for:
 * - Loading consent data from asset files.
 * - Formatting consent text into HTML.
 * - Saving consent pages fetched from URLs.
 * - Loading locally stored consent pages.
 * - Handling network operations and managing the state of consent data.
 *
 * It interacts with the [AssetManager] to read files, the [ConsentRepository] to
 * store and retrieve consent data, and the [NetworkHelper] to check network connectivity.
 *
 * @property assetManager The AssetManager for accessing files in the assets directory.
 * @property consentRepository The repository for managing consent data.
 * @property networkHelper The helper for checking network connectivity.
 */
@HiltViewModel
class ConsentViewModel @Inject constructor(
    private val assetManager: AssetManager,
    private val consentRepository: ConsentRepository,
    networkHelper: NetworkHelper
) : BaseViewModel(networkHelper = networkHelper) {

    /**
     * A MutableLiveData holding the consent data.
     *
     * This LiveData is used to observe changes in the consent data.
     * It is exposed as a read-only LiveData via the [consentData] property.
     */
    private val mutableConsent: MutableLiveData<Consent> = MutableLiveData()

    /**
     * A read-only LiveData that exposes the consent data.
     *
     * Observers can use this property to receive updates when the consent data changes.
     */
    val consentData = mutableConsent.hide()

    /**
     * Loads consent data from an asset file.
     *
     * This function reads the consent data from a JSON file located in the assets directory.
     * The file name is determined by the provided [fileName] and the current locale's language code.
     * The loaded JSON data is then parsed into a [Consent] object and posted to the [mutableConsent] LiveData.
     *
     * @param fileName The name of the JSON file containing the consent data (without the language code prefix).
     */
    fun loadConsentData(fileName: String) {
        viewModelScope.launch {
            val languageCode = Locale.getDefault().language
            val assetPath = "$languageCode/$fileName"
            Timber.d { "Asset Path : $assetPath" }
            val consent = assetManager.open(assetPath).bufferedReader().use { it.readText() }
            val data = Gson().fromJson(consent, Consent::class.java)
            mutableConsent.postValue(data)
        }
    }

    /**
     * Formats the given text into HTML.
     *
     * This function wraps the provided [text] in basic HTML tags to ensure it is displayed correctly in a WebView.
     * It sets the text color to black and the font size to 0.8em.
     *
     * @param text The text to format as HTML.
     * @return The formatted HTML string.
     */
    fun formatToHtml(text: String): String {
        return "<html><body style='color:black;font-size: 0.8em;' ><p align=\"justify\"> $text </body></html>"
    }

    /**
     * Saves a consent page fetched from a URL.
     *
     * This function fetches the HTML content of a consent page from the given [url].
     * It then parses the HTML using Jsoup to extract the content within the "primary" element.
     * Finally, it saves the extracted content to the local storage using the [consentRepository],
     * associated with the provided [key].
     *
     * @param key The key to associate with the saved consent page.
     * @param url The URL of the consent page to fetch and save.
     */
    fun saveConsentPage(key: String, url: String) {
        viewModelScope.launch {
            Timber.d { "Save Consent Page : $key => $url" }
            consentRepository.getConsent(url).collect {
                handleResponse(it) { responseBody ->
                    val doc: Document = Jsoup.parse(responseBody.string())
                    val element: Element? = doc.getElementById("primary")
                    element?.let { el ->
                        consentRepository.saveConsentPage(key, el.html().replace("#", ""))
                    }
                }
            }
        }
    }

    /**
     * Loads a consent page from local storage.
     *
     * This function retrieves a consent page from local storage using the provided [key].
     *
     * @param key The key associated with the consent page to load.
     * @return The consent page content as a String.
     */
    fun loadConsentPage(key: String) = consentRepository.getConsentPage(key)
}