package org.intelehealth.app.ui.prescription.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import org.intelehealth.app.ui.visit.viewmodel.VisitViewModel
import org.intelehealth.common.model.ListItemHeaderSection
import org.intelehealth.common.utility.DateTimeUtils
import org.intelehealth.data.offline.entity.VisitDetail
import org.intelehealth.data.provider.prescription.PrescriptionRepository
import javax.inject.Inject
import org.intelehealth.common.state.Result
import org.intelehealth.common.utility.CommonConstants.LIMIT
import org.intelehealth.common.utility.NO_DATA_FOUND

/**
 * ViewModel for managing prescription-related data and business logic.
 *
 * This ViewModel handles fetching, storing, and providing LiveData for
 * different categories of prescriptions:
 * - Recent Received Prescriptions
 * - Older Received Prescriptions
 * - Recent Pending Prescriptions
 * - Older Pending Prescriptions
 *
 * It supports pagination (fetching data in chunks) and search functionality
 * for both received and pending prescriptions. Prescription counts are also exposed.
 * Date formatting is applied to prescription visit dates before they are exposed.
 *
 * @param prescriptionRepository Repository responsible for fetching prescription data.
 */
@HiltViewModel
class PrescriptionViewModel @Inject constructor(
    private var prescriptionRepository: PrescriptionRepository
) : VisitViewModel() {

    private var prescriptionCurrentMonthData =
        MutableLiveData<Result<List<ListItemHeaderSection>>>()
    var prescriptionCurrentMonthLiveData: LiveData<Result<List<ListItemHeaderSection>>> = prescriptionCurrentMonthData

    private var pagingData =
        MutableLiveData<List<ListItemHeaderSection>>()
    var pagingLiveData: LiveData<List<ListItemHeaderSection>> = pagingData

    fun getCurrentMonthPrescriptions(tab: VisitDetail.TabType, searchQuery: String = "") {
        viewModelScope.launch {
            resetReceivedPrescriptionData()
            Timber.d { "Other => $otherSectionAdded" }
            Timber.d { "Add more => $addMoreSectionAdded" }

            val flow = if (tab == VisitDetail.TabType.RECEIVED)
                prescriptionRepository.getCurrentMonthReceivedPrescriptions(searchQuery)
            else prescriptionRepository.getCurrentMonthPendingPrescriptions(searchQuery)

            mapFlowToResultLiveData(flow, searchQuery, tab)
        }
    }

    private fun mapFlowToResultLiveData(
        flow: Flow<List<VisitDetail>>,
        searchQuery: String,
        tab: VisitDetail.TabType,
    ) = viewModelScope.launch {
        flow.onStart {
            prescriptionCurrentMonthData.postValue(Result.Loading("Please wait..."))
        }.catch { e ->
            Timber.e(e) // Log the error
            prescriptionCurrentMonthData.postValue(Result.Error(e.message ?: "Unknown error occurred"))
        }.flowOn(dispatcher).collectLatest { data ->
            if (data.isNotEmpty()) {
                offset = data.size
                mapPrescriptionsWithDateFormat(data).apply {
                    val formattedData = generatePrescriptionListWithHeaderSection(this)
                    prescriptionCurrentMonthData.postValue(Result.Success(formattedData, ""))
                }

                if (offset <= LIMIT) fetchPrescriptionWithPagination(tab, searchQuery)
            } else fetchPrescriptionWithPagination(tab, searchQuery)
        }
    }


    fun fetchPrescriptionWithPagination(tab: VisitDetail.TabType, searchQuery: String = "") =
        viewModelScope.launch {
            val flow = if (tab == VisitDetail.TabType.RECEIVED)
                prescriptionRepository.getReceivedPrescriptionsWithPaging(offset, searchQuery)
            else prescriptionRepository.getPendingPrescriptionsWithPaging(offset, searchQuery)

            handlePagingFlow(flow, searchQuery)
        }

    private fun handlePagingFlow(flow: Flow<List<VisitDetail>>, searchQuery: String) = viewModelScope.launch {
        flow.collectLatest {
            if (it.isNotEmpty()) {
                Timber.d { "Other pagin => $otherSectionAdded" }
                Timber.d { "Add more pagin => $addMoreSectionAdded" }
                val formattedPrescriptions = mapPrescriptionsWithDateFormat(it)
                val listWithSection = if (otherSectionAdded) formattedPrescriptions
                else generatePrescriptionListWithHeaderSection(formattedPrescriptions)
                pagingData.postValue(listWithSection)
                offset += it.size
                if (it.size < LIMIT && searchQuery.isEmpty()) {
                    delay(100)
                    pagingData.postValue(emptyList())
                }
            } else {
                pagingData.postValue(emptyList())
                if (offset == 0) prescriptionCurrentMonthData.postValue(Result.Fail(NO_DATA_FOUND))
            }
        }
    }

    private fun mapPrescriptionsWithDateFormat(
        visitDetails: List<VisitDetail>
    ): List<VisitDetail> {
        return visitDetails.map { prescription ->
            prescription.visitStartDate = DateTimeUtils.formatDbToDisplay(
                prescription.visitStartDate,
                DateTimeUtils.USER_DOB_DB_FORMAT,
                DateTimeUtils.DD_MMM_AT_HH_MM_A_FORMAT
            )
            prescription
        }
    }
}
