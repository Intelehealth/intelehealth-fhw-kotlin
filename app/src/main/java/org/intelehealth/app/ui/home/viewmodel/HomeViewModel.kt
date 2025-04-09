package org.intelehealth.app.ui.home.viewmodel

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
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : BaseViewModel() {
    fun getVisitStatusCount() = repository.getVisitStatusCount()

    fun getFollowUpStatusCount() = repository.getFollowUpStatusCount()

    fun getAppointmentStatusCount() = repository.getAppointmentStatusCount()
}
