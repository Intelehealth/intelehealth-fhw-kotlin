package org.intelehealth.app.ui.language.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.intelehealth.common.extensions.changeLanguage
import org.intelehealth.common.utility.PreferenceUtils
import org.intelehealth.config.presenter.language.data.LanguageRepository
import org.intelehealth.config.presenter.language.factory.LanguageViewModelFactory
import org.intelehealth.config.presenter.language.viewmodel.LanguageViewModel
import org.intelehealth.config.room.ConfigDatabase
import org.intelehealth.config.room.entity.ActiveLanguage
import javax.inject.Inject


/**
 * Created by Vaghela Mithun R. on 15-04-2024 - 11:30.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * An abstract base activity that provides language management functionality.
 *
 * This activity handles the selection and application of different languages
 * within the application. It retrieves supported languages, applies the
 * user's preferred language, and provides a callback for when the language
 * data is loaded.
 */
abstract class LanguageActivity : AppCompatActivity() {
    @Inject
    lateinit var preferenceUtils: PreferenceUtils
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

    /**
     * Sets up the application's language based on user preferences.
     *
     * This method retrieves the user's preferred language from [PreferenceUtils]
     * and applies it using the [changeLanguage] extension function.
     *
     * @return The updated [Context] with the selected language applied.
     */
    open fun setupLanguage(): Context {
        if (::preferenceUtils.isInitialized) {
            val appLanguage = preferenceUtils.currentLanguage
            if (!appLanguage.equals("", ignoreCase = true)) {
                changeLanguage(appLanguage)
            }
        }
        return this
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
