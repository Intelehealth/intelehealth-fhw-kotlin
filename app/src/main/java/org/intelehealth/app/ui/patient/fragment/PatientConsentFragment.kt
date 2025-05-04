package org.intelehealth.app.ui.patient.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.ContentViewConsentBinding
import org.intelehealth.app.databinding.FragmentPatientConsentBinding
import org.intelehealth.app.model.ConsentArgs
import org.intelehealth.app.ui.base.fragment.ConsentFragment
import org.intelehealth.common.extensions.showCommonDialog
import org.intelehealth.common.model.DialogParams
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
class PatientConsentFragment : ConsentFragment(R.layout.fragment_patient_consent) {
    private lateinit var binding: FragmentPatientConsentBinding
    private val navArgs: PatientConsentFragmentArgs by navArgs<PatientConsentFragmentArgs>()

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
        binding = FragmentPatientConsentBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        handleClientEvent()
    }

    private fun handleClientEvent() {
        binding.btnConsentAccept.setOnClickListener { onAccept() }
        binding.btnConsentDecline.setOnClickListener { declineConsent() }
    }

    private fun declineConsent() {
        showCommonDialog(
            DialogParams(icon = org.intelehealth.resource.R.drawable.ic_dialog_alert,
                         title = ResourceR.string.dialog_title_decline_consent,
                         message = ResourceR.string.content_decline_consent_message,
                         positiveLbl = ResourceR.string.action_exit,
                         negativeLbl = ResourceR.string.action_cancel,
                         onPositiveClick = {
                             requireActivity().finish()
                         })
        )
    }

    private fun onAccept() {
        val direction = if (args.consentType == ConsentArgs.ConsentType.PERSONAL_DATA_CONSENT) {
            val consentArgs = ConsentArgs(ConsentArgs.ConsentType.TELECONSULTATION, null, null)
            PatientConsentFragmentDirections.actionToSelf(consentArgs)
        } else if (args.consentType == ConsentArgs.ConsentType.TELECONSULTATION) {
            PatientConsentFragmentDirections.actionToPatientPersonalInfo("0")
        } else {
            val consentArgs = ConsentArgs(ConsentArgs.ConsentType.PERSONAL_DATA_CONSENT, null, null)
            PatientConsentFragmentDirections.actionToSelf(consentArgs)
        }
        findNavController().navigate(direction)
    }

    override fun getConsentBinding(): ContentViewConsentBinding = binding.contentConsentView

    override fun getConsentArgs(): ConsentArgs = navArgs.consentArgs

    override fun screenTitle(title: String) {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = title
    }

    override fun navigateToSelf(args: ConsentArgs) {
        val direction = PatientConsentFragmentDirections.actionToSelf(args)
        findNavController().navigate(direction)
    }
}
