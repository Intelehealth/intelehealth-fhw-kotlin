package org.intelehealth.app

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.databinding.ActivityTestBinding
import org.intelehealth.common.helper.PreferenceHelper
import org.intelehealth.common.socket.SocketManager
import org.intelehealth.common.socket.SocketViewModel
import org.intelehealth.config.data.ConfigRepository
import org.intelehealth.config.presenter.language.viewmodel.LanguageViewModel
import org.intelehealth.config.worker.ConfigSyncWorker
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 18-12-2024 - 16:42.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class TestActivity : AppCompatActivity(R.layout.activity_test) {

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

//    @Inject
//    lateinit var configRepository: ConfigRepository

    private val socketViewModel by viewModels<SocketViewModel>()

    private val languageViewModel by viewModels<LanguageViewModel>()

    private val binding: ActivityTestBinding by lazy {
        ActivityTestBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        preferenceHelper.save(PreferenceHelper.MESSAGE_BODY, "Hello, World!")
        binding.tvMessage.text = preferenceHelper.getString(PreferenceHelper.MESSAGE_BODY)
        socketViewModel.printSocketStatus()

//        configRepository.fetchAndUpdateConfig {
//            Timber.d { "ConfigRepository fetchAndUpdateConfig result: $it" }
//        }
//
        languageViewModel.fetchSupportedLanguage().observe(this) {
            it.forEach { language ->
                Timber.d { "LanguageViewModel fetchSupportedLanguage language: $language" }
            }
        }
        ConfigSyncWorker.startConfigSyncWorker(this) {
            // Handle the result
            Timber.d { "ConfigSyncWorker result: $it" }
        }
    }
}
