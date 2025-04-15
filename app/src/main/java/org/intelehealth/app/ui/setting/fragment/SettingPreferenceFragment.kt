package org.intelehealth.app.ui.setting.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentSettingPreferencesBinding
import org.intelehealth.app.ui.setting.viewmodel.SettingViewModel
import org.intelehealth.config.presenter.language.viewmodel.LanguageViewModel
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 19-03-2025 - 12:17.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * A fragment that displays the application's settings preferences.
 *
 * This fragment provides a UI for managing various settings, including:
 * - Blackout period (restricting app usage during certain hours).
 * - Changing the application language.
 * - Downloading new protocols.
 *
 * It interacts with the [SettingViewModel] and [LanguageViewModel] to retrieve
 * and update settings data.
 */
@AndroidEntryPoint
class SettingPreferenceFragment : Fragment(R.layout.fragment_setting_preferences) {
    private lateinit var binding: FragmentSettingPreferencesBinding
    private val settingViewModel by activityViewModels<SettingViewModel>()
    private val languageViewModel by viewModels<LanguageViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingPreferencesBinding.bind(view)
        setBlackoutPeriod()
        handleClickAction()
        fetchSelectedLanguage()
    }

    /**
     * Sets up the blackout period preference.
     *
     * This method displays the current blackout period duration and observes the
     * blackout status from the [SettingViewModel] to update the UI. It also
     * handles changes to the blackout period switch, calling the
     * [SettingViewModel] to update the status.
     */
    private fun setBlackoutPeriod() {
        // TODO: Consider retrieving the blackout period start and end times
        // from a configuration or preferences instead of hardcoding them.
        val blackoutPeriod = getString(ResourceR.string.content_blackout_period_duration, "9:00 PM", "6:00 AM")
        binding.tvBlackoutPeriodTime.text = blackoutPeriod

        settingViewModel.blackoutStateData.observe(viewLifecycleOwner) { isBlackedOut ->
            binding.blackoutStatus = isBlackedOut
        }

        binding.switchBlackout.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.setBlackoutPeriodStatus(isChecked)
        }
    }

    /**
     * Fetches and displays the currently selected language.
     *
     * This method observes the selected language code from the
     * [SettingViewModel] and then uses the [LanguageViewModel] to fetch the
     * corresponding language details and display the language name.
     */
    private fun fetchSelectedLanguage() {
        settingViewModel.languageStateData.observe(viewLifecycleOwner) { languageCode ->
            setSelectedLanguage(languageCode)
        }
    }

    /**
     * Sets the displayed selected language.
     *
     * This method uses the [LanguageViewModel] to fetch the language details
     * based on the provided language code and updates the text of the "Change
     * Language" button with the language name.
     *
     * @param language The code of the selected language.
     */
    private fun setSelectedLanguage(language: String) {
        languageViewModel.fetchLanguage(language).observe(viewLifecycleOwner) { languageDetails ->
            binding.btnChangeLanguage.text = languageDetails.name
        }
    }

    /**
     * Handles click events for the settings options.
     *
     * This method sets up click listeners for the "Change Language" and "Update
     * Protocols" buttons, navigating to the corresponding fragments when
     * clicked.
     */
    private fun handleClickAction() {
        binding.btnChangeLanguage.setOnClickListener {
            findNavController().navigate(SettingPreferenceFragmentDirections.navSettingPreferenceToChangeLanguage())
        }

        binding.btnUpdateProtocols.setOnClickListener {
            findNavController().navigate(SettingPreferenceFragmentDirections.navSettingPreferenceToDownloadProtocols())
        }
    }
}
