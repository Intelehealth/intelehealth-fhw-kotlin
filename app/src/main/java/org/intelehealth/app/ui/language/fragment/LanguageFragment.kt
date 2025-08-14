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
/**
 * An open base fragment that provides language management functionality.
 *
 * This fragment handles the retrieval and application of language settings
 * within the application. It fetches supported languages, applies the user's
 * preferred language, and provides a callback for when the language data is
 * loaded.
 *
 * @param layoutResId The layout resource ID for the fragment's content view.
 */
open class LanguageFragment(@LayoutRes layoutResId: Int) : Fragment(layoutResId) {
    @Inject
    lateinit var preferenceUtils: PreferenceUtils
    private val languageViewModel by viewModels<LanguageViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        languageViewModel.fetchSupportedLanguage().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                it.map { lang ->
                    lang.isDefault = lang.code == preferenceUtils.currentLanguage
                    return@map lang
                }.apply { onLanguageLoaded(this) }
            }
        }
        setupLanguage()
    }

    /**
     * Sets up the application's language based on user preferences.
     *
     * This method retrieves the user's preferred language from [PreferenceUtils]
     * and applies it using the [changeLanguage] extension function on the
     * activity.
     *
     * @return The updated [Context] with the selected language applied.
     */
    open fun setupLanguage(): Context {
        if (::preferenceUtils.isInitialized) {
            val appLanguage = preferenceUtils.currentLanguage
            return requireActivity().changeLanguage(appLanguage)
        }
        return requireContext()
    }

    /**
     * Callback method invoked when the list of supported languages is loaded.
     *
     * This method is called after the supported languages are fetched from the
     * [LanguageViewModel]. Subclasses should override this method to handle the
     * language data, such as updating a language selection UI.
     *
     * @param languages The list of supported [ActiveLanguage] objects.
     */
    open fun onLanguageLoaded(languages: List<ActiveLanguage>) {}
}
