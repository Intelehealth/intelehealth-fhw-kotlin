package org.intelehealth.app.ui.home.viewmodel

import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.data.offline.OfflineDatabase
import org.intelehealth.data.provider.home.HomeRepository
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 21-01-2025 - 20:19.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * ViewModel for the home screen, responsible for providing data about visit,
 * follow-up, and appointment statuses.
 *
 * This ViewModel interacts with the [HomeRepository] to fetch status counts
 * and exposes them as LiveData for observation by the UI.
 *
 * @property repository The repository used to access home screen data.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : BaseViewModel() {
    /**
     * Retrieves the count of visits with a specific status.
     *
     * @return A LiveData object containing the visit status count.
     */
    fun getVisitStatusCount() = repository.getVisitStatusCount()

    /**
     * Retrieves the count of follow-ups with a specific status.
     *
     * @return A LiveData object containing the follow-up status count.
     */
    fun getFollowUpStatusCount() = repository.getFollowUpStatusCount()

    /**
     * Retrieves the count of appointments with a specific status.
     *
     * @return A LiveData object containing the appointment status count.
     */
    fun getAppointmentStatusCount() = repository.getAppointmentStatusCount()
}
