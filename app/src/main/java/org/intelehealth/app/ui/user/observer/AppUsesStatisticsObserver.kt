package org.intelehealth.app.ui.user.observer

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import org.intelehealth.data.provider.user.UserSessionRepository
import javax.inject.Inject


/**
 * Created by Vaghela Mithun R. on 07-03-2025 - 19:28.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class AppUsesStatisticsObserver @Inject constructor(
    private val userSessionRepository: UserSessionRepository
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        userSessionRepository.startSession()
        super.onStart(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        userSessionRepository.endSession()
        super.onStop(owner)
    }
}
