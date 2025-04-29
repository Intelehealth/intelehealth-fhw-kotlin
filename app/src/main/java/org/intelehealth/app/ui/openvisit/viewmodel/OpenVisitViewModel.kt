package org.intelehealth.app.ui.openvisit.viewmodel

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.data.offline.entity.Patient
import javax.inject.Inject

@HiltViewModel
class OpenVisitViewModel @Inject constructor() : BaseViewModel() {

    private var _recentVisitList = MutableLiveData<List<Patient>>()
    private var _olderVisitList = MutableLiveData<List<Patient>>()

    val recentVisitList: MutableLiveData<List<Patient>> get() = _recentVisitList
    val olderVisitList: MutableLiveData<List<Patient>> get() = _olderVisitList

    var isRecentVisitEmpty = MutableLiveData<Boolean>()
    var isOlderVisitEmpty = MutableLiveData<Boolean>()

    init {
        getRecentOpenVisits()
        getOlderOpenVisits()
    }

    private fun getRecentOpenVisits() {
        recentVisitList.value = listOf(
            Patient(
                openMrsId = "1",
                firstName = "John",
                middleName = "Doe",
                lastName = "Smith",
                dateOfBirth = "29th June"
            )
        )
        isRecentVisitEmpty.value = false
    }

    private fun getOlderOpenVisits() {
        olderVisitList.value = listOf(
            Patient(
                openMrsId = "4",
                firstName = "John",
                middleName = "Doe",
                lastName = "Smith",
                dateOfBirth = "29th June"
            ),
            Patient(
                openMrsId = "5",
                firstName = "Jane",
                middleName = "Doe",
                lastName = "Smith",
                dateOfBirth = "29th June"
            ),
            Patient(
                openMrsId = "6",
                firstName = "John",
                middleName = "Doe",
                lastName = "Smith",
                dateOfBirth = "29th June"
            )
        )
        isOlderVisitEmpty.value = false
    }
}