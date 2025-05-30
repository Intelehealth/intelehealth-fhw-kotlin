package org.intelehealth.app.ui.onboarding.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentAyuPolicyBinding
import org.intelehealth.app.model.ConsentArgs
import org.intelehealth.common.extensions.enableMovementMethod
import org.intelehealth.common.extensions.setClickableText
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:22.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

/**
 * A Fragment that displays the Ayu policy screen, including terms and conditions and privacy policy.
 *
 * This fragment allows users to review the terms and conditions and privacy policy,
 * and requires them to agree to the privacy policy before proceeding.
 * It provides clickable links to navigate to the full consent pages for terms and conditions
 * and privacy policy.
 *
 * The fragment uses view binding for layout access and navigation components for screen transitions.
 *
 * @property binding The view binding for the fragment's layout.
 */
@AndroidEntryPoint
class AyuPolicyFragment : Fragment(R.layout.fragment_ayu_policy) {

    private lateinit var binding: FragmentAyuPolicyBinding

    /**
     * Called when the fragment's view has been created.
     *
     * Initializes the view binding, sets up the clickable links for terms and conditions and privacy policy,
     * handles the checkbox state, and sets up the button click listener.
     *
     * @param view The root view of the fragment.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAyuPolicyBinding.bind(view)
        binding.tvPrivacyNoticeLink1.enableMovementMethod()
        binding.checkboxPrivacyPolicy.setOnCheckedChangeListener { _, b -> binding.btnSetup.isEnabled = b }
        binding.btnSetup.isEnabled = binding.checkboxPrivacyPolicy.isChecked
        setClickableTermsAndPrivacyPolicy()
        handleBtnClick()
    }

    /**
     * Sets up the clickable links for terms and conditions and privacy policy.
     *
     * This function retrieves the titles for terms and conditions and privacy policy from resources
     * and makes them clickable. Clicking on these links navigates the user to the corresponding
     * consent pages using the navigation component.
     */
    private fun setClickableTermsAndPrivacyPolicy() {
        val termAndCondition = resources.getString(ResourceR.string.title_terms_and_conditions)
        val privacyPolicy = resources.getString(ResourceR.string.title_privacy_policy)
        Timber.d { termAndCondition }
        Timber.d { privacyPolicy }
        binding.tvPrivacyNoticeLink1.setClickableText(termAndCondition) {
            val args = ConsentArgs(ConsentArgs.ConsentType.TERMS_AND_CONDITIONS, null, null)
            AyuPolicyFragmentDirections.actionAyuPolicyToConsent(args).apply {
                findNavController().navigate(this)
            }
        }

        binding.tvPrivacyNoticeLink1.setClickableText(privacyPolicy) {
            val args = ConsentArgs(ConsentArgs.ConsentType.PRIVACY_POLICY, null, null)
            AyuPolicyFragmentDirections.actionAyuPolicyToConsent(args).apply {
                findNavController().navigate(this)
            }
        }
    }

    /**
     * Handles the click event for the setup button.
     *
     * This function sets up the click listener for the setup button, which navigates the user
     * to the setup screen when clicked.
     */
    private fun handleBtnClick() {
        binding.btnSetup.setOnClickListener {
            findNavController().navigate(AyuPolicyFragmentDirections.actionAyuPolicyToSetup())
        }
    }
}
