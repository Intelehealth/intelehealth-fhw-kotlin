package org.intelehealth.app.ui.prescription.viewmodel

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class PrescriptionDetailsViewModel @Inject constructor() : BaseViewModel() {
    var optionsVisibility = MutableLiveData(false)
}