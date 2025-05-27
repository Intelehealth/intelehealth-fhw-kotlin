package org.intelehealth.app.ui.user.observer

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.github.ajalt.timberkt.Timber
import org.intelehealth.data.provider.user.UserSessionRepository
import javax.inject.Inject


/**
 * Created by Vaghela Mithun R. on 07-03-2025 - 19:28.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Observes application lifecycle events to track app usage statistics.
 *
 * This class implements [DefaultLifecycleObserver] and uses a
 * [UserSessionRepository] to record the start and end times of user sessions
 * based on the application's lifecycle.
 *
 * @property userSessionRepository The repository used to manage user session
 *                                 data.
 */
class AppUsesStatisticsObserver @Inject constructor(
    private val userSessionRepository: UserSessionRepository
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        userSessionRepository.startSession()
        Timber.d { "session started" }
        super.onStart(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        userSessionRepository.endSession()
        Timber.d { "session ended" }
        super.onStop(owner)
    }
}
