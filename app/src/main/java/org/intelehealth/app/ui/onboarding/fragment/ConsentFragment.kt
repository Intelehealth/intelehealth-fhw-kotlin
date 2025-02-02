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

enum class ConsentType(val key: String) {
    PERSONAL_DATA_POLICY("personal_data_processing_policy"), PRIVACY_POLICY("privacy_policy"), TERMS_AND_CONDITIONS("terms_and_conditions"), TELECONSULTATION(
        "teleconsultation_consent"
    ),
    TERMS_OF_USE("terms_of_use"), PRIVACY_NOTICE("privacy_notice"), PERSONAL_DATA_CONSENT("personal_data_consent"), PRESCRIPTION_DISCLAIMER(
        "prescription_disclaimer"
    )
}

@AndroidEntryPoint
class ConsentFragment : Fragment(R.layout.fragment_consent) {
    private lateinit var binding: FragmentConsentBinding
    private val args: ConsentFragmentArgs by navArgs<ConsentFragmentArgs>()
    private val consentViewModel by viewModels<ConsentViewModel>()

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

    private fun loadPage() {
        if (consentViewModel.isInternetAvailable()) {
            args.url?.let {
                binding.webview.loadUrl(it)
                binding.screenTitle = args.screenTitle
//                consentViewModel.saveConsentPage(args.consentType.key, it)
            } ?: loadFromCatch()
        } else loadFromCatch()
    }

    private fun loadFromCatch() {
        val consent = consentViewModel.loadConsentPage(args.consentType.key)
        if (consent.isNotEmpty()) binding.webview.loadData(consent, "text/html", "utf-8")
        else consentViewModel.consentData.observeForever { bindConsentData(it) }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun updateWebViewSettings() {
        binding.webview.settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
        }

        binding.webview.webViewClient = webViewClient
    }

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

    private fun loadHTMLContent(content: String) {
        val data = consentViewModel.formatToHtml(content)
        binding.webview.loadDataWithBaseURL(null, data, "text/html", "utf-8", null)
    }

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