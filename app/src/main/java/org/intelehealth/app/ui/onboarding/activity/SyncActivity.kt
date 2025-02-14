package org.intelehealth.app.ui.onboarding.activity

import androidx.work.WorkInfo
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.common.helper.PreferenceHelper
import org.intelehealth.common.helper.PreferenceHelper.Companion.AUTH_BASIC_TOKEN
import org.intelehealth.common.helper.PreferenceHelper.Companion.KEY_PREF_LOCATION_UUID
import org.intelehealth.common.ui.activity.CircularProgressActivity
import org.intelehealth.common.utility.CommonConstants.MAX_PROGRESS
import org.intelehealth.data.provider.sync.worker.SyncDataWorker
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 11-12-2024 - 11:50.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class SyncActivity : CircularProgressActivity() {
    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    override fun onViewCreated() {
        preferenceHelper.save(AUTH_BASIC_TOKEN, "bWl0aHVubnVyc2U6TnVyc2VAMTIz")
        preferenceHelper.save(KEY_PREF_LOCATION_UUID, "513f5ae0-29c5-4b7d-990d-dd73eb571e76")
        progressTitle("Please wait, Initial sync on going")
        progressTask("Syncing...")
        SyncDataWorker.startSyncWorker(this) {
            if (it.state == WorkInfo.State.SUCCEEDED) {
                onProgress(MAX_PROGRESS)
                runOnUiThread { progressTask("Sync Completed") }
            } else {
                val progress = it.progress.getInt(SyncDataWorker.WORK_PROGRESS, 0)
                onProgress(progress)
            }
        }
    }

    override fun onRetry() {
        TODO("Not yet implemented")
    }

    override fun onClose() {
        TODO("Not yet implemented")
    }
}
