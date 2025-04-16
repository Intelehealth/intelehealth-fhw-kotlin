package org.intelehealth.app.ui.onboarding.fragment

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentConsentBinding
import org.intelehealth.app.model.Consent
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
 * Represents the different types of consent that a user can provide.
 *
 * Each enum value corresponds to a specific type of consent, identified by a unique key.
 * These keys are used to reference the consent type in data storage or when interacting with
 * consent-related APIs.
 *
 * @property key The unique identifier for this consent type.
 */
enum class ConsentType(val key: String) {
    /**
     * Consent for the processing of personal data.
     *
     * This consent type covers the user's agreement to how their personal data is collected,
     * used, stored, and processed.
     */
    PERSONAL_DATA_POLICY("personal_data_processing_policy"),

    /**
     * Consent for the privacy policy.
     *
     * This consent type covers the user's agreement to the terms outlined in the privacy policy.
     */
    PRIVACY_POLICY("privacy_policy"),

    /**
     * Consent for the terms and conditions.
     *
     * This consent type covers the user's agreement to the general terms and conditions of service.
     */
    TERMS_AND_CONDITIONS("terms_and_conditions"),

    /**
     * Consent for teleconsultation.
     *
     * This consent type covers the user's agreement to participate in remote consultations.
     */
    TELECONSULTATION("teleconsultation_consent"),

    /**
     * Consent for the terms of use.
     *
     * This consent type covers the user's agreement to the specific terms of use for the service.
     */
    TERMS_OF_USE("terms_of_use"),

    /**
     * Consent for the privacy notice.
     *
     * This consent type covers the user's acknowledgment of the privacy notice.
     */
    PRIVACY_NOTICE("privacy_notice"),

    /**
     * Consent for the collection and use of personal data.
     *
     * This consent type covers the user's explicit agreement to the collection and use of their personal data.
     */
    PERSONAL_DATA_CONSENT("personal_data_consent"),

    /**
     * Consent for the prescription disclaimer.
     *
     * This consent type covers the user's acknowledgment of the disclaimer related to prescriptions.
     */
    PRESCRIPTION_DISCLAIMER("prescription_disclaimer")
}

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
class ConsentFragment : Fragment(R.layout.fragment_consent) {
    private lateinit var binding: FragmentConsentBinding
    private val args: ConsentFragmentArgs by navArgs<ConsentFragmentArgs>()
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
        binding = FragmentConsentBinding.bind(view)
        binding.contentLoadingProgressBar.hide()
        updateWebViewSettings()
        binding.buttonVisibility = false
        consentViewModel.loadConsentData(ASSET_FILE_CONSENT)
        loadPage()
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    /**
     * Loads the consent page content.
     *
     * If an internet connection is available and a URL is provided in the arguments, it loads the content from the URL.
     * Otherwise, it attempts to load the content from locally stored data.
     */
    private fun loadPage() {
        if (consentViewModel.isInternetAvailable()) {
            args.url?.let {
                binding.webview.loadUrl(it)
                binding.screenTitle = args.screenTitle
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
        when (args.consentType) {
            ConsentType.PERSONAL_DATA_POLICY -> {
                binding.screenTitle = getString(ResourceR.string.title_personal_data_processing_policy)
                loadHTMLContent(consent.personalDataProcessingPolicy ?: "")
            }

            ConsentType.PRIVACY_POLICY -> {
                binding.screenTitle = getString(ResourceR.string.title_privacy_policy)
                loadHTMLContent(consent.privacyPolicy ?: "")
            }

            ConsentType.TERMS_AND_CONDITIONS -> {
                binding.screenTitle = getString(ResourceR.string.title_terms_and_conditions)
                loadHTMLContent(consent.termsAndConditions ?: "")
            }

            ConsentType.TELECONSULTATION -> {
                binding.screenTitle = getString(ResourceR.string.title_teleconsultation_consent)
                loadHTMLContent(consent.teleconsultationConsent ?: "")
            }

            ConsentType.TERMS_OF_USE -> {
                binding.screenTitle = getString(ResourceR.string.title_terms_of_use)
                loadHTMLContent(consent.termsOfUse ?: "")
            }

            ConsentType.PERSONAL_DATA_CONSENT -> {
                binding.screenTitle = getString(ResourceR.string.title_personal_data_consent)
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
            val title = binding.screenTitle
            if (it.contains(URL_PRIVACY_POLICY)) {
                val direction = ConsentFragmentDirections.actionToSelf(ConsentType.PRIVACY_POLICY, it, title)
                findNavController().navigate(direction)
            } else if (it.contains(URL_TERM_OF_USE)) {
                val direction = ConsentFragmentDirections.actionToSelf(ConsentType.TERMS_OF_USE, it, title)
                findNavController().navigate(direction)
            } else if (it.contains(URL_PERSONAL_DATA)) {
                val direction = ConsentFragmentDirections.actionToSelf(ConsentType.PERSONAL_DATA_POLICY, it, title)
                findNavController().navigate(direction)
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

    companion object {
        const val TAG = "ConsentFragment"
        const val URL_PRIVACY_POLICY = "intelehealth.org/privacy-policy"
        const val URL_TERM_OF_USE = "intelehealth.org/terms-of-use"
        const val URL_PERSONAL_DATA = "intelehealth.org/personal-data-processing-policy"
        const val MAILTO = "mailto"
        const val FORMAT_PDF = ".pdf"
    }
}