package org.intelehealth.app.ui.prescription.viewmodel

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import javax.inject.Inject
/**
 * Created by Tanvir Hasan on 28-04-25
 * Email : mhasan@intelehealth.org
 *
 * This viewmodel is responsible to handle the prescription details screens data
 **/
@HiltViewModel
class PrescriptionDetailsViewModel @Inject constructor() : BaseViewModel() {
    var optionsVisibility = MutableLiveData(false)

    /**
     * Updates the visibility of the options menu.
     */
    fun updateOptionsVisibility() {
        optionsVisibility.value = !optionsVisibility.value!!
    }
}