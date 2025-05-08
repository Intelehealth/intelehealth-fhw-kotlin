package org.intelehealth.app.ui.base.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.databinding.ContentViewConsentBinding
import org.intelehealth.app.model.Consent
import org.intelehealth.app.model.ConsentArgs
import org.intelehealth.app.model.ConsentArgs.ConsentType
import org.intelehealth.app.ui.onboarding.viewmodel.ConsentViewModel
import org.intelehealth.app.utility.ASSET_FILE_CONSENT
import org.intelehealth.common.extensions.hide
import org.intelehealth.common.extensions.show
import org.intelehealth.common.extensions.showToast
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 08-01-2025 - 17:26.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/


/**
 * A Fragment responsible for displaying various types of consent content to the user,
 * such as privacy policies, terms and conditions, and other legal agreements.
 *
 * This fragment can load content from either a URL or from locally stored data.
 * It uses a WebView to display the content and handles navigation within the WebView.
 *
 * The fragment supports different types of consent, as defined by the [ConsentType] enum.
 * It dynamically loads the appropriate content based on the selected consent type.
 *
 * @property binding The view binding for the fragment's layout.
 * @property args The navigation arguments passed to the fragment, including the consent type, URL, and screen title.
 * @property consentViewModel The ViewModel responsible for managing consent-related data and logic.
 */
@AndroidEntryPoint
abstract class ConsentFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {
    private lateinit var binding: ContentViewConsentBinding
    protected lateinit var args: ConsentArgs
    private val consentViewModel by viewModels<ConsentViewModel>()

    /**
     * Called when the fragment's view has been created.
     *
     * Initializes the view binding, sets up the WebView, loads the consent data, and handles
     * navigation events.
     *
     * @param view The root view of the fragment.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getConsentBinding()
        args = getConsentArgs()
        binding.contentLoadingProgressBar.hide()
        updateWebViewSettings()
        consentViewModel.loadConsentData(ASSET_FILE_CONSENT)
        loadPage()
    }


    /**
     * Loads the consent page content.
     *
     * If an internet connection is available and a URL is provided in the arguments, it loads the content from the URL.
     * Otherwise, it attempts to load the content from locally stored data.
     */
    private fun loadPage() {
        Timber.d { "loadPage: ${args.url} ${args.consentType}" }
        if (consentViewModel.isInternetAvailable()) {
            args.url?.let {
                binding.webview.loadUrl(it)
//                consentViewModel.saveConsentPage(args.consentType.key, it)
            } ?: loadFromCatch()
        } else loadFromCatch()
    }

    /**
     * Loads consent content from locally stored data.
     *
     * Retrieves the consent data based on the consent type key. If the data is found, it loads it into the WebView.
     * Otherwise, it observes the consent data from the ViewModel and binds it to the view.
     */
    private fun loadFromCatch() {
        val consent = consentViewModel.loadConsentPage(args.consentType.key)
        if (consent.isNotEmpty()) binding.webview.loadData(consent, "text/html", "utf-8")
        else consentViewModel.consentData.observeForever { bindConsentData(it) }
    }

    /**
     * Configures the settings for the WebView.
     *
     * Enables JavaScript and sets the WebViewClient to handle page loading and navigation.
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun updateWebViewSettings() {
        binding.webview.settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
        }

        binding.webview.webViewClient = webViewClient
    }

    /**
     * Binds the consent data to the view.
     *
     * Determines the appropriate content to display based on the consent type and loads it into the WebView.
     *
     * @param consent The consent data to bind.
     */
    private fun bindConsentData(consent: Consent) {
        binding.contentLoadingProgressBar.hide()
//        Timber.d { "bindConsentData: $consent" }
        when (args.consentType) {
            ConsentType.PERSONAL_DATA_POLICY -> {
                screenTitle(getString(ResourceR.string.title_personal_data_processing_policy))
                loadHTMLContent(consent.personalDataProcessingPolicy ?: "")
            }

            ConsentType.PRIVACY_POLICY -> {
                screenTitle(getString(ResourceR.string.title_privacy_policy))
                loadHTMLContent(consent.privacyPolicy ?: "")
            }

            ConsentType.TERMS_AND_CONDITIONS -> {
                screenTitle(getString(ResourceR.string.title_terms_and_conditions))
                loadHTMLContent(consent.termsAndConditions ?: "")
            }

            ConsentType.TELECONSULTATION -> {
                screenTitle(getString(ResourceR.string.title_teleconsultation_consent))
                loadHTMLContent(consent.teleconsultationConsent ?: "")
            }

            ConsentType.TERMS_OF_USE -> {
                screenTitle(getString(ResourceR.string.title_terms_of_use))
                loadHTMLContent(consent.termsOfUse ?: "")
            }

            ConsentType.PERSONAL_DATA_CONSENT -> {
                screenTitle(getString(ResourceR.string.title_personal_data_consent))
                loadHTMLContent(consent.personalDataConsent ?: "")
            }

            else -> {}
        }
    }

    /**
     * Loads HTML content into the WebView.
     *
     * Formats the content as HTML and loads it using `loadDataWithBaseURL`.
     *
     * @param content The HTML content to load.
     */
    private fun loadHTMLContent(content: String) {
        val data = consentViewModel.formatToHtml(content)
        binding.webview.loadDataWithBaseURL(null, data, "text/html", "utf-8", null)
    }

    /**
     * The WebViewClient for handling page loading and navigation events.
     *
     * Shows and hides the progress bar during page loading, handles errors, and overrides URL loading.
     */
    private val webViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            binding.contentLoadingProgressBar.show()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            binding.contentLoadingProgressBar.hide()
        }

        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            binding.contentLoadingProgressBar.hide()
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return request?.let { return@let overrideUrl(it) } ?: false
        }
    }

    /**
     * Overrides URL loading behavior for specific URLs.
     *
     * Handles navigation to other consent pages or external links based on the URL.
     *
     * @param request The web resource request.
     * @return `true` if the URL loading was handled, `false` otherwise.
     */
    private fun overrideUrl(request: WebResourceRequest): Boolean {
        request.url?.toString()?.let {
            val title = args.screenTitle ?: getString(ResourceR.string.app_name)
            if (it.contains(URL_PRIVACY_POLICY)) {
                navigateToSelf(ConsentArgs(ConsentType.PRIVACY_POLICY, it, title))
            } else if (it.contains(URL_TERM_OF_USE)) {
                navigateToSelf(ConsentArgs(ConsentType.TERMS_OF_USE, it, title))
            } else if (it.contains(URL_PERSONAL_DATA)) {
                navigateToSelf(ConsentArgs(ConsentType.PERSONAL_DATA_POLICY, it, title))
            } else if (it.lowercase().contains(FORMAT_PDF) || it.lowercase().contains(MAILTO)) {
                requireContext().startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(it)
                })
            } else {
                //if url type is other then loading the url on same webview if network is available
                return if (!consentViewModel.isInternetAvailable()) {
                    showToast(ResourceR.string.content_no_network)
                    true
                } else false
            }
        }

        return true
    }

    abstract fun getConsentBinding(): ContentViewConsentBinding

    abstract fun getConsentArgs(): ConsentArgs

    abstract fun screenTitle(title: String)

    abstract fun navigateToSelf(args: ConsentArgs)

    companion object {
        const val TAG = "ConsentFragment"
        const val URL_PRIVACY_POLICY = "intelehealth.org/privacy-policy"
        const val URL_TERM_OF_USE = "intelehealth.org/terms-of-use"
        const val URL_PERSONAL_DATA = "intelehealth.org/personal-data-processing-policy"
        const val MAILTO = "mailto"
        const val FORMAT_PDF = ".pdf"
    }
}
