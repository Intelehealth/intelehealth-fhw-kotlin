package org.intelehealth.app.ui.language.fragment

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import org.intelehealth.common.extensions.changeLanguage
import org.intelehealth.common.utility.PreferenceUtils
import org.intelehealth.config.presenter.language.viewmodel.LanguageViewModel
import org.intelehealth.config.room.entity.ActiveLanguage
import java.util.Locale
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:24.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
open class LanguageFragment(@LayoutRes layoutResId: Int) : Fragment(layoutResId) {
    @Inject
    lateinit var preferenceUtils: PreferenceUtils
    private val languageViewModel by viewModels<LanguageViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        languageViewModel.fetchSupportedLanguage().observe(viewLifecycleOwner) {
            onLanguageLoaded(it)
        }
        setupLanguage()
    }

    open fun setupLanguage(): Context {
        if (::preferenceUtils.isInitialized) {
            val appLanguage = preferenceUtils.currentLanguage
            return requireActivity().changeLanguage(appLanguage)
        }
        return requireContext()
    }

    open fun onLanguageLoaded(languages: List<ActiveLanguage>) {}
}