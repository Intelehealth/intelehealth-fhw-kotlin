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
/**
 * The entry point activity of the application, responsible for initial setup
 * and checks before navigating to the main content.
 *
 * This activity performs tasks such as updating the FCM token, checking for
 * mandatory app updates, and handling navigation based on the app's state.
 */
@AndroidEntryPoint
class OnboardingActivity : BaseStatusBarActivity() {

    private val launcherViewModel by viewModels<LauncherViewModel>()
    private val binding by lazy { ActivityOnboardingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        launcherViewModel.updateFcmToken()
        checkForceUpdate()
    }

    /**
     * Checks for a mandatory app update and prompts the user to update if necessary.
     *
     * This method interacts with the [LauncherViewModel] to determine if a
     * force update is required. If so, it displays a dialog with options to
     * update or cancel. Canceling will close the app.
     */
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

    /**
     * Initiates the app update process.
     *
     * This method attempts to open the app's page in the device's app market.
     * If the market app is not found, it falls back to opening the app's page
     * on the Google Play Store website.
     */
    @Suppress("SwallowedException")
    private fun updateApp() {
        try {
            startActivity(getAppIntent(getAppMarketUrl(this@OnboardingActivity)))
        } catch (e: ActivityNotFoundException) {
            startActivity(getAppIntent(getAppPlayStoreUrl(this@OnboardingActivity)))
        }
    }

    /**
     * Creates an intent to view a URL, typically for opening an app store page.
     *
     * @param url The URL to view.
     * @return An [Intent] that can be used to start an activity to view the URL.
     */
    private fun getAppIntent(url: String) = Intent(
        Intent.ACTION_VIEW, Uri.parse(url)
    )
}
