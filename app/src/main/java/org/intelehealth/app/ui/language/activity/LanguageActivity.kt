package org.intelehealth.app.ui.language.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import org.intelehealth.common.utility.PreferenceUtils
import org.intelehealth.config.presenter.language.data.LanguageRepository
import org.intelehealth.config.presenter.language.factory.LanguageViewModelFactory
import org.intelehealth.config.presenter.language.viewmodel.LanguageViewModel
import org.intelehealth.config.room.ConfigDatabase
import org.intelehealth.config.room.entity.ActiveLanguage
import java.util.Locale
import javax.inject.Inject


/**
 * Created by Vaghela Mithun R. on 15-04-2024 - 11:30.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
abstract class LanguageActivity : AppCompatActivity() {
    @Inject lateinit var preferenceUtils: PreferenceUtils
    private val languageViewModel by viewModels<LanguageViewModel> {
        val db = ConfigDatabase.getInstance(applicationContext)
        val repository = LanguageRepository(db.languageDao())
        LanguageViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        languageViewModel.fetchSupportedLanguage().observe(this) {
            onLanguageLoaded(it)
        }
        setupLanguage()
    }

    open fun setupLanguage(): Context {
        if (::preferenceUtils.isInitialized) {
            val appLanguage = preferenceUtils.currentLanguage
            if (!appLanguage.equals("", ignoreCase = true)) {
                Locale(appLanguage).apply {
                    Locale.setDefault(this)
                    val configuration: Configuration = resources.configuration
                    val displayMetrics: DisplayMetrics = resources.displayMetrics
                    configuration.setLocale(this)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(this))
                    } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                        createConfigurationContext(configuration)
                    } else {
                        resources.updateConfiguration(configuration, displayMetrics)
                    }
                }
            }
        }
        return this
    }

    open fun onLanguageLoaded(languages: List<ActiveLanguage>) {}
}