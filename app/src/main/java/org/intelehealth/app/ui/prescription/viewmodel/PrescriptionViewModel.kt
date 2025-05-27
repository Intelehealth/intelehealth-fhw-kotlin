package org.intelehealth.app.ui.prescription.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.intelehealth.common.enums.LoadingType
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.common.utility.CommonConstants
import org.intelehealth.data.offline.entity.Prescription
import org.intelehealth.data.provider.prescription.PrescriptionRepository
import javax.inject.Inject

@HiltViewModel
class PrescriptionViewModel @Inject constructor(
    private var prescriptionRepository: PrescriptionRepository
) : BaseViewModel() {
    private var _receivedRecentPrescription =
        MutableLiveData<MutableList<Prescription>>(mutableListOf())
    var receivedRecentPrescription = _receivedRecentPrescription

    private var _receivedOlderPrescription =
        MutableLiveData<MutableList<Prescription>>(mutableListOf())
    var receivedOlderPrescription = _receivedRecentPrescription

    private var _pendingRecentPrescription = MutableLiveData<List<Prescription>>()
    var pendingRecentPrescription = _pendingRecentPrescription

    private var _pendingOlderPrescription =
        MutableLiveData<MutableList<Prescription>>(mutableListOf())
    var pendingOlderPrescription = _pendingOlderPrescription

    private var receivedRecentOffset = 0
    private var receivedOlderOffset = 0

    private var pendingRecentOffset = 0
    private var pendingOlderOffset = 0

    init {
        fetchReceivedPrescription(LoadingType.INITIAL)
        fetchPendingPrescription(LoadingType.INITIAL)
    }

    fun prescriptionCount() = prescriptionRepository.getPrescriptionCount()

    fun fetchReceivedPrescription(loadingType: LoadingType) {
        viewModelScope.launch {
            executeLocalQuery(
                queryCallOne = {
                    prescriptionRepository.getReceivedPrescriptions(
                        searchQuery = "",
                        limit = CommonConstants.LIMIT,
                        offset = receivedRecentOffset
                    )
                },

                queryCallTwo = {
                    prescriptionRepository.getReceivedPrescriptions(
                        searchQuery = "",
                        limit = CommonConstants.LIMIT,
                        offset = receivedOlderOffset
                    )
                }
            ).collectLatest {
                handleResponse(loadingType, it) {
                    val data = it as Pair<*, *>
                    _receivedRecentPrescription.value =
                        (data.first as List<Prescription>).toMutableList()
                    _receivedOlderPrescription.value =
                        (data.second as List<Prescription>).toMutableList()

                    receivedRecentOffset += _receivedRecentPrescription.value?.size ?: 0
                    receivedOlderOffset += _receivedOlderPrescription.value?.size ?: 0

                }
            }
        }
    }

    fun fetchPendingPrescription(loadingType: LoadingType) {
        viewModelScope.launch {
            executeLocalQuery(
                queryCallOne = {
                    prescriptionRepository.getPendingPrescriptions(
                        searchQuery = "",
                        limit = CommonConstants.LIMIT,
                        offset = _pendingRecentPrescription.value?.size ?: 0
                    )
                },

                queryCallTwo = {
                    prescriptionRepository.getPendingPrescriptions(
                        searchQuery = "",
                        limit = CommonConstants.LIMIT,
                        offset = _pendingOlderPrescription.value?.size ?: 0
                    )
                }
            ).collectLatest {
                handleResponse(loadingType, it) {
                    val data = it as Pair<*, *>
                    _receivedRecentPrescription.value =
                        (data.first as List<Prescription>).toMutableList()
                    _receivedOlderPrescription.value =
                        (data.second as List<Prescription>).toMutableList()

                    pendingRecentOffset += _pendingRecentPrescription.value?.size ?: 0
                    pendingOlderOffset += _pendingOlderPrescription.value?.size ?: 0

                }
            }
        }
    }
}