package org.intelehealth.app.ui.onboarding.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.databinding.ActivityOnboardingBinding
import org.intelehealth.app.ui.onboarding.viewmodel.LauncherViewModel
import org.intelehealth.app.utility.getAppMarketUrl
import org.intelehealth.app.utility.getAppPlayStoreUrl
import org.intelehealth.common.extensions.showCommonDialog
import org.intelehealth.common.model.DialogParams
import org.intelehealth.common.ui.activity.BaseStatusBarActivity
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 31-12-2024 - 17:02.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class OnboardingActivity : BaseStatusBarActivity() {

    private val launcherViewModel by viewModels<LauncherViewModel>()
//    private val triagingRuleViewModel by viewModels<TriagingRuleViewModel>()

    private val binding by lazy { ActivityOnboardingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        launcherViewModel.updateFcmToken()
        checkForceUpdate()
//        triagingRuleViewModel.loadTriagingRuleData(TRIAGING_RULE_FILE_NAME)
//        triagingRuleViewModel.triagingResultData.observe(this, {
//            Timber.d { "Triaging Rule Data : $it" }
//        })
    }

    fun checkForceUpdate() {
        launcherViewModel.checkForceUpdate {
            DialogParams(icon = ResourceR.drawable.ic_dialog_alert,
                title = ResourceR.string.title_new_update,
                message = ResourceR.string.content_warning_app_update,
                positiveLbl = ResourceR.string.action_update,
                negativeLbl = ResourceR.string.action_cancel,
                onPositiveClick = { updateApp() },
                onNegativeClick = { finish() }).let {
                showCommonDialog(it)
            }
        }
    }

    @Suppress("SwallowedException")
    private fun updateApp() {
        try {
            startActivity(getAppIntent(getAppMarketUrl(this@OnboardingActivity)))
        } catch (e: ActivityNotFoundException) {
            startActivity(getAppIntent(getAppPlayStoreUrl(this@OnboardingActivity)))
        }
    }

    private fun getAppIntent(url: String) = Intent(
        Intent.ACTION_VIEW, Uri.parse(url)
    )
}
