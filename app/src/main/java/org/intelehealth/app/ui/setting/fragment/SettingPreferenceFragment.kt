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

    private fun setBlackoutPeriod() {
        val blackoutPeriod = getString(ResourceR.string.content_blackout_period_duration, "9:00 PM", "6:00 AM")
        binding.tvBlackoutPeriodTime.text = blackoutPeriod
    }

    private fun fetchSelectedLanguage() {
        settingViewModel.languageStateData.observe(viewLifecycleOwner) {
            setSelectedLanguage(it)
        }
    }

    private fun setSelectedLanguage(language: String) {
        languageViewModel.fetchLanguage(language).observe(viewLifecycleOwner) {
            binding.btnChangeLanguage.text = it.name
        }
    }

    private fun handleClickAction() {
        binding.btnChangeLanguage.setOnClickListener {
            findNavController().navigate(SettingPreferenceFragmentDirections.navSettingPreferenceToChangeLanguage())
        }

        binding.btnUpdateProtocols.setOnClickListener {
            findNavController().navigate(SettingPreferenceFragmentDirections.navSettingPreferenceToDownloadProtocols())
        }
    }
}
