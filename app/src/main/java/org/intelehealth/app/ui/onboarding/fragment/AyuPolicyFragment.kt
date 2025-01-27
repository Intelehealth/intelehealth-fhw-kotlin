package org.intelehealth.app.ui.onboarding.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentAyuPolicyBinding
import org.intelehealth.common.extensions.enableMovementMethod
import org.intelehealth.common.extensions.setClickableText
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:22.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class AyuPolicyFragment : Fragment(R.layout.fragment_ayu_policy) {
    private lateinit var binding: FragmentAyuPolicyBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAyuPolicyBinding.bind(view)
        binding.tvPrivacyNoticeLink1.enableMovementMethod()
        binding.checkboxPrivacyPolicy.setOnCheckedChangeListener { _, b -> binding.btnSetup.isEnabled = b }
        binding.btnSetup.isEnabled = binding.checkboxPrivacyPolicy.isChecked
        setClickableTermsAndPrivacyPolicy()
        handleBtnClick()
    }

    private fun setClickableTermsAndPrivacyPolicy() {
        val termAndCondition = resources.getString(ResourceR.string.title_terms_and_conditions)
        val privacyPolicy = resources.getString(ResourceR.string.title_privacy_policy)
        binding.tvPrivacyNoticeLink1.setClickableText(termAndCondition) {
            AyuPolicyFragmentDirections.actionAyuPolicyToConsent(ConsentType.TERMS_AND_CONDITIONS, null).apply {
                findNavController().navigate(this)
            }
        }

        binding.tvPrivacyNoticeLink1.setClickableText(privacyPolicy) {
            AyuPolicyFragmentDirections.actionAyuPolicyToConsent(ConsentType.PRIVACY_POLICY, null).apply {
                findNavController().navigate(this)
            }
        }
    }

    private fun handleBtnClick() {
        binding.btnSetup.setOnClickListener {
            findNavController().navigate(AyuPolicyFragmentDirections.actionAyuPolicyToSetup())
        }
    }
}