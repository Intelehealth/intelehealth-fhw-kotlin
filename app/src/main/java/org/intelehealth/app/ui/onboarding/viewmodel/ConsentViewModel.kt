package org.intelehealth.app.ui.onboarding.viewmodel

import android.content.res.AssetManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.intelehealth.app.model.Consent
import org.intelehealth.app.utility.ASSET_FILE_CONSENT
import org.intelehealth.common.extensions.hide
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import java.util.Locale
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 15-01-2025 - 15:17.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltViewModel
class ConsentViewModel @Inject constructor(private val assetManager: AssetManager) : BaseViewModel() {
    private val mutableConsent: MutableLiveData<Consent> = MutableLiveData()
    val consentData = mutableConsent.hide()

    fun loadConsentData(fileName: String) {
        viewModelScope.launch {
            val languageCode = Locale.getDefault().language
            val assetPath = "$languageCode/$fileName"
            Timber.d { "Asset Path : $assetPath" }
            val consent = assetManager.open(assetPath).bufferedReader().use { it.readText() }
            val data = Gson().fromJson(consent, Consent::class.java)
            mutableConsent.postValue(data)
        }
    }

    fun formatToHtml(text: String): String {
        return "<html><body style='color:black;font-size: 0.8em;' ><p align=\"justify\"> $text </body></html>"
    }
}