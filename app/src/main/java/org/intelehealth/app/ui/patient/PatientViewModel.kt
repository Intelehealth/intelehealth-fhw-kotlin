package org.intelehealth.app.ui.patient

import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 20-04-2025 - 17:30.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltViewModel
class PatientViewModel @Inject constructor(
    networkHelper: NetworkHelper
): BaseViewModel(networkHelper = networkHelper) {
}
