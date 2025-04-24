package org.intelehealth.config.presenter.feature.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.config.presenter.feature.data.ActiveFeatureStatusRepository
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 12-04-2024 - 12:56.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltViewModel
class ActiveFeatureStatusViewModel @Inject constructor(
    private val repository: ActiveFeatureStatusRepository,
    networkHelper: NetworkHelper
) : BaseViewModel(networkHelper = networkHelper) {
    fun fetchActiveFeatureStatus() = repository.getActiveFeatureStatus()
}
