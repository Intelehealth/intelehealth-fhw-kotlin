package org.intelehealth.app.ui.prescription.viewmodel

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.data.offline.entity.Patient
import javax.inject.Inject

@HiltViewModel
class PrescriptionViewModel @Inject constructor() : BaseViewModel() {
    private var _receivedPrescription = MutableLiveData<List<Patient>>()
    var receivedPrescription = _receivedPrescription
    var isRecentReceivedEmpty = MutableLiveData<Boolean>()
    var isRecentPendingEmpty = MutableLiveData<Boolean>()

    init {
        fetchReceivedPrescription()
        fetchPendingPrescription()
    }

    private fun fetchReceivedPrescription() {
        _receivedPrescription.value = listOf(
            Patient(
                openMrsId = "432323",
                firstName = "Tanvir",
                middleName = "Hasan",
                lastName = "Tvr"
            ),
            Patient(
                openMrsId = "432323",
                firstName = "Tanvir",
                middleName = "Hasan",
                lastName = "Tvr"
            ),
            Patient(
                openMrsId = "432323",
                firstName = "Tanvir",
                middleName = "Hasan",
                lastName = "Tvr"
            )
        )

        isRecentReceivedEmpty.value = false
    }

    private fun fetchPendingPrescription() {
        _receivedPrescription.value = listOf(
            Patient(
                openMrsId = "432323",
                firstName = "Tanvir",
                middleName = "Hasan",
                lastName = "Tvr"
            ),
            Patient(
                openMrsId = "432323",
                firstName = "Tanvir",
                middleName = "Hasan",
                lastName = "Tvr"
            ),
            Patient(
                openMrsId = "432323",
                firstName = "Tanvir",
                middleName = "Hasan",
                lastName = "Tvr"
            )
        )

        isRecentPendingEmpty.value = false
    }
}