package org.intelehealth.app.ui.onboarding.fragment

import android.os.Bundle
import android.view.View
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

/**
 * Created by Vaghela Mithun R. on 08-01-2025 - 17:26.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

enum class ConsentType {
    PERSONAL, PRIVACY_POLICY, TERMS_AND_CONDITIONS, TELECONSULTATION
}

@AndroidEntryPoint
class ConsentFragment : Fragment(R.layout.fragment_consent) {
    private lateinit var binding: FragmentConsentBinding
    private val args: ConsentFragmentArgs by navArgs<ConsentFragmentArgs>()
    private val consentViewModel by viewModels<ConsentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentConsentBinding.bind(view)
        binding.buttonVisibility = false
        consentViewModel.loadConsentData(ASSET_FILE_CONSENT)
        consentViewModel.consentData.observe(viewLifecycleOwner, { bindConsentData(it) })
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun bindConsentData(consent: Consent) {
        binding.contentLoadingProgressBar.hide()
        when (args.consentType) {
            ConsentType.PERSONAL -> loadHTMLContent(consent.personalDataConsent ?: "")
            ConsentType.PRIVACY_POLICY -> {
                binding.screenTitle = getString(org.intelehealth.resource.R.string.title_privacy_policy)
                loadHTMLContent(consent.privacyPolicy ?: "")
            }

            ConsentType.TERMS_AND_CONDITIONS -> {
                binding.screenTitle = getString(org.intelehealth.resource.R.string.title_terms_and_conditions)
                loadHTMLContent(consent.termsAndConditions ?: "")
            }

            ConsentType.TELECONSULTATION -> loadHTMLContent(consent.teleconsultationConsent ?: "")
        }
    }

    private fun loadHTMLContent(content: String) {
        val data = consentViewModel.formatToHtml(content)
        binding.webview.loadDataWithBaseURL(null, data, "text/html", "utf-8", null)
    }
}