package org.intelehealth.app.ui.prescription.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.intelehealth.common.state.Result
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.data.offline.entity.Patient
import org.intelehealth.data.offline.entity.Prescription
import org.intelehealth.data.provider.prescription.PrescriptionRepository
import javax.inject.Inject

@HiltViewModel
class PrescriptionViewModel @Inject constructor(
    private var prescriptionRepository: PrescriptionRepository
) : BaseViewModel() {
    private var _receivedPrescription = MutableLiveData<List<Prescription>>()
    var receivedPrescription = _receivedPrescription

    private var _pendingPrescription = MutableLiveData<List<Prescription>>()
    var pendingPrescription = _receivedPrescription

    private var _isLoadingRecentReceived = MutableLiveData<Boolean>()
    var isLoadingRecentReceived = _isLoadingRecentReceived

    private var _isLoadingOlderReceived = MutableLiveData<Boolean>()
    var isLoadingOlderReceived = _isLoadingRecentReceived

    var isRecentReceivedEmpty = MutableLiveData<Boolean>()
    var isRecentPendingEmpty = MutableLiveData<Boolean>()

    init {
        fetchReceivedPrescription()
        fetchPendingPrescription()
    }

    fun fetchReceivedPrescription() {
        viewModelScope.launch {
            executeLocalQuery {
                prescriptionRepository.getReceivedPrescriptions(
                    searchQuery = "",
                    limit = 20,
                    offset = _receivedPrescription.value?.size ?: 0
                )

            }.collectLatest {
                handleResponse(it) {
                    _receivedPrescription.value = it
                }
            }
        }

        isRecentReceivedEmpty.value = false
    }

    private fun fetchPendingPrescription() {
        viewModelScope.launch {
            executeLocalQuery {
                prescriptionRepository.getPendingPrescriptions(
                    searchQuery = "",
                    limit = 20,
                    offset = 0
                )

            }.collectLatest {
                handleResponse(it) {
                    _pendingPrescription.value = it
                }
            }
        }
        isRecentPendingEmpty.value = false
    }
}