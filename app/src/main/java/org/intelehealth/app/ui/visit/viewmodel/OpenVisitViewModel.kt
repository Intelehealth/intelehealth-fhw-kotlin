package org.intelehealth.app.ui.visit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.intelehealth.common.model.ListItemHeaderSection
import org.intelehealth.common.state.Result
import org.intelehealth.common.utility.CommonConstants.LIMIT
import org.intelehealth.common.utility.DateTimeUtils
import org.intelehealth.common.utility.NO_DATA_FOUND
import org.intelehealth.data.offline.entity.VisitDetail
import org.intelehealth.data.provider.visit.VisitDetailRepository
import javax.inject.Inject

@HiltViewModel
class OpenVisitViewModel @Inject constructor(
    private val visitDetailRepository: VisitDetailRepository
) : VisitViewModel() {
    private var openVisitCurrentMonthData =
        MutableLiveData<Result<List<ListItemHeaderSection>>>()
    var openVisitCurrentMonthLiveData: LiveData<Result<List<ListItemHeaderSection>>> = openVisitCurrentMonthData

    private var pagingData =
        MutableLiveData<List<ListItemHeaderSection>>()
    var pagingLiveData: LiveData<List<ListItemHeaderSection>> = pagingData

    fun getCurrentMonthOpenVisits() {
        viewModelScope.launch {
            resetReceivedPrescriptionData()
            Timber.d { "Other => $otherSectionAdded" }
            Timber.d { "Add more => $addMoreSectionAdded" }
            val flow = visitDetailRepository.getCurrentMonthOpenVisits()
            mapFlowToResultLiveData(flow)
        }
    }

    private fun mapFlowToResultLiveData(
        flow: Flow<List<VisitDetail>>
    ) = viewModelScope.launch {
        flow.onStart {
            openVisitCurrentMonthData.postValue(Result.Loading("Please wait..."))
        }.catch { e ->
            Timber.e(e) // Log the error
            openVisitCurrentMonthData.postValue(Result.Error(e.message ?: "Unknown error occurred"))
        }.flowOn(dispatcher).collectLatest { data ->
            if (data.isNotEmpty()) {
                offset = data.size
                mapPrescriptionsWithDateFormat(data).apply {
                    val formattedData = generatePrescriptionListWithHeaderSection(this)
                    openVisitCurrentMonthData.postValue(Result.Success(formattedData, ""))
                }

                if (offset <= LIMIT) fetchOtherOpenVisitWithPagination()
            } else fetchOtherOpenVisitWithPagination()
        }
    }


    fun fetchOtherOpenVisitWithPagination() = viewModelScope.launch {
        val flow = visitDetailRepository.getOtherOpenVisitsWithPaging(offset)
        handlePagingFlow(flow)
    }

    private fun handlePagingFlow(flow: Flow<List<VisitDetail>>) = viewModelScope.launch {
        flow.collectLatest {
            if (it.isNotEmpty()) {
                Timber.d { "Other pagin => $otherSectionAdded" }
                Timber.d { "Add more pagin => $addMoreSectionAdded" }
                val formattedPrescriptions = mapPrescriptionsWithDateFormat(it)
                val listWithSection = if (otherSectionAdded) formattedPrescriptions
                else generatePrescriptionListWithHeaderSection(formattedPrescriptions)
                pagingData.postValue(listWithSection)
                offset += it.size
                if (it.size < LIMIT) {
                    delay(100)
                    pagingData.postValue(emptyList())
                }
            } else {
                pagingData.postValue(emptyList())
                if (offset == 0) openVisitCurrentMonthData.postValue(Result.Fail(NO_DATA_FOUND))
            }
        }
    }

    private fun mapPrescriptionsWithDateFormat(
        visitDetails: List<VisitDetail>
    ): List<VisitDetail> {
        return visitDetails.map { visits ->
            visits.visitStartDate = DateTimeUtils.formatDbToDisplay(
                visits.visitStartDate,
                DateTimeUtils.USER_DOB_DB_FORMAT,
                DateTimeUtils.DD_MMM_AT_HH_MM_A_FORMAT
            )
            visits
        }
    }
}
