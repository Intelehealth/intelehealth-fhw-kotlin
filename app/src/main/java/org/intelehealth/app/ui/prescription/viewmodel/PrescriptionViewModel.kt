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
import org.intelehealth.resource.R
import org.intelehealth.common.model.CommonHeaderSection
import org.intelehealth.common.model.ListItemFooter
import org.intelehealth.common.model.ListItemHeaderSection
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.common.utility.CommonConstants
import org.intelehealth.common.utility.DateTimeUtils
import org.intelehealth.data.offline.entity.Prescription
import org.intelehealth.data.provider.prescription.PrescriptionRepository
import javax.inject.Inject
import org.intelehealth.common.state.Result
import org.intelehealth.common.utility.CommonConstants.LIMIT
import org.intelehealth.common.utility.NO_DATA_FOUND
import java.util.LinkedList

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
) : BaseViewModel() {

    private var prescriptionCurrentMonthData =
        MutableLiveData<Result<List<ListItemHeaderSection>>>()
    var prescriptionCurrentMonthLiveData: LiveData<Result<List<ListItemHeaderSection>>> = prescriptionCurrentMonthData

    private var pagingData =
        MutableLiveData<List<ListItemHeaderSection>>()
    var pagingLiveData: LiveData<List<ListItemHeaderSection>> = pagingData

    private var offset = 0

    private var otherSectionAdded = false
    private var addMoreSectionAdded = false

    /**
     * Retrieves a Flow of prescription counts from the repository.
     *
     * This typically includes counts like received, pending, etc.
     */
    fun prescriptionCount() = prescriptionRepository.getPrescriptionCount()


    /**
     * Fetches received prescriptions (recent and older) from the repository.
     *
     * Resets pagination offsets if it's an initial load.
     * Retrieves recent and older received prescriptions based on the current search query and offsets.
     * Updates LiveData with the fetched and date-formatted prescriptions.
     * Increments pagination offsets for subsequent fetches.
     *
     * @param loadingType Indicates if this is an initial load or for pagination.
     */
//    fun fetchReceivedPrescription(loadingType: LoadingType) {
//        if (loadingType == LoadingType.INITIAL) {
//            offset = 0
//            receivedOlderOffset = 0
//        }
//        viewModelScope.launch {
//            // Assuming executeLocalQuery handles the combined fetching logic from the repository
//            // and returns a Flow<Pair<List<Prescription>, List<Prescription>>>
//            executeLocalQuery(
//                queryCallOne = {
//                    prescriptionRepository.getRecentReceivedPrescriptions(
//                        searchQuery = searchQueryReceived,
//                        limit = LIMIT,
//                        offset = offset
//                    )
//                },
//                queryCallTwo = {
//                    prescriptionRepository.getOlderReceivedPrescriptions(
//                        searchQuery = searchQueryReceived,
//                        limit = LIMIT,
//                        offset = receivedOlderOffset
//                    )
//                }
//            ).collectLatest {
//                handleResponse(loadingType, it) {
//                    val data = it as Pair<*, *>
//                    _receivedRecentPrescription.value =
//                        (data.first as List<Prescription>).map { prescription ->
//                            prescription.visitStartDate = DateTimeUtils.formatOneToAnother(
//                                prescription.visitStartDate,
//                                DateTimeUtils.USER_DOB_DB_FORMAT,
//                                DateTimeUtils.DD_MMM_AT_HH_MM_A_FORMAT
//                            )
//                            prescription
//                        }.toMutableList()
//                    _receivedOlderPrescription.value =
//                        (data.second as List<Prescription>).map { prescription ->
//                            prescription.visitStartDate = DateTimeUtils.formatOneToAnother(
//                                prescription.visitStartDate,
//                                DateTimeUtils.USER_DOB_DB_FORMAT,
//                                DateTimeUtils.DD_MMM_AT_HH_MM_A_FORMAT
//                            )
//                            prescription
//                        }.toMutableList()
//
//                    offset += _receivedRecentPrescription.value?.size ?: 0
//                    receivedOlderOffset += _receivedOlderPrescription.value?.size ?: 0
//
//                }
//            }
//        }
//    }


    /**
     * Fetches pending prescriptions (recent and older) from the repository.
     *
     * Resets pagination offsets for initial loads. Uses the provided 'query' for searching.
     * Updates LiveData with fetched, date-formatted prescriptions and increments pagination offsets.
     *
     * @param loadingType Indicates if it's an initial load or for pagination.
     * @param query The search string to filter prescriptions. Defaults to empty.
     */
//    fun fetchPendingPrescription(loadingType: LoadingType, query: String = "") {
//        if (loadingType == LoadingType.INITIAL) {
//            pendingRecentOffset = 0
//            pendingOlderOffset = 0
//        }
//        viewModelScope.launch {
//            executeLocalQuery(
//                queryCallOne = {
//                    prescriptionRepository.getRecentPendingPrescriptions(
//                        searchQuery = searchQueryPending, // Use the passed 'query' parameter
//                        limit = LIMIT,
//                        offset = pendingRecentOffset
//                    )
//                },
//                queryCallTwo = {
//                    prescriptionRepository.getOlderPendingPrescriptions(
//                        searchQuery = searchQueryPending, // Use the passed 'query' parameter
//                        limit = LIMIT,
//                        offset = pendingOlderOffset
//                    )
//                }
//            ).collectLatest {
//                handleResponse(loadingType, it) {
//                    val data = it as Pair<*, *>
//                    _pendingRecentPrescription.value =
//                        (data.first as List<Prescription>).map { prescription ->
//                            prescription.visitStartDate = DateTimeUtils.formatOneToAnother(
//                                prescription.visitStartDate,
//                                DateTimeUtils.USER_DOB_DB_FORMAT,
//                                DateTimeUtils.DD_MMM_AT_HH_MM_A_FORMAT
//                            )
//                            prescription
//                        }.toMutableList()
//                    _pendingOlderPrescription.value =
//                        (data.second as List<Prescription>).map { prescription ->
//                            prescription.visitStartDate = DateTimeUtils.formatOneToAnother(
//                                prescription.visitStartDate,
//                                DateTimeUtils.USER_DOB_DB_FORMAT,
//                                DateTimeUtils.DD_MMM_AT_HH_MM_A_FORMAT
//                            )
//                            prescription
//                        }.toMutableList()
//
//                    pendingRecentOffset += _pendingRecentPrescription.value?.size ?: 0
//                    pendingOlderOffset += _pendingOlderPrescription.value?.size ?: 0
//
//                }
//            }
//        }
//    }

    fun getCurrentMonthPrescriptions(tab: Prescription.TabType, searchQuery: String = "") {
        viewModelScope.launch {
            resetReceivedPrescriptionData()
            Timber.d { "Other => $otherSectionAdded" }
            Timber.d { "Add more => $addMoreSectionAdded" }

            val flow = if (tab == Prescription.TabType.RECEIVED)
                prescriptionRepository.getCurrentMonthReceivedPrescriptions(searchQuery)
            else prescriptionRepository.getCurrentMonthPendingPrescriptions(searchQuery)

            mapFlowToResultLiveData(flow, searchQuery, tab)
        }
    }

    private fun mapFlowToResultLiveData(
        flow: Flow<List<Prescription>>,
        searchQuery: String,
        tab: Prescription.TabType,
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


    fun fetchPrescriptionWithPagination(tab: Prescription.TabType, searchQuery: String = "") =
        viewModelScope.launch {
            val flow = if (tab == Prescription.TabType.RECEIVED)
                prescriptionRepository.getReceivedPrescriptionsWithPaging(offset, searchQuery)
            else prescriptionRepository.getPendingPrescriptionsWithPaging(offset, searchQuery)

            handlePagingFlow(flow, searchQuery)
        }

    private fun handlePagingFlow(flow: Flow<List<Prescription>>, searchQuery: String) = viewModelScope.launch {
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
        prescriptions: List<Prescription>
    ): List<Prescription> {
        return prescriptions.map { prescription ->
            prescription.visitStartDate = DateTimeUtils.formatOneToAnother(
                prescription.visitStartDate,
                DateTimeUtils.USER_DOB_DB_FORMAT,
                DateTimeUtils.DD_MMM_AT_HH_MM_A_FORMAT
            )
            prescription
        }
    }

    private fun generatePrescriptionListWithHeaderSection(
        prescriptions: List<Prescription>
    ): List<ListItemHeaderSection> {
        val listWithSection = LinkedList<ListItemHeaderSection>()
        val today = prescriptions.filter { it.section == CommonConstants.TODAY }
        val yesterday = prescriptions.filter { it.section == CommonConstants.YESTERDAY }
        val thisWeek = prescriptions.filter { it.section == CommonConstants.THIS_WEEK }
        val thisMonth = prescriptions.filter { it.section == CommonConstants.THIS_MONTH }
        val other = prescriptions.filter { it.section == CommonConstants.OTHER }

        if (today.isEmpty() && yesterday.isEmpty() && thisWeek.isEmpty() && thisMonth.isEmpty() && other.isEmpty()) {
            return emptyList()
        }

        if (today.isNotEmpty()) {
            listWithSection.add(CommonHeaderSection(R.string.lbl_today_visits))
            listWithSection.addAll(today)
        }

        if (yesterday.isNotEmpty()) {
            listWithSection.add(CommonHeaderSection(R.string.lbl_yesterday_visits))
            listWithSection.addAll(yesterday)
        }

        if (thisWeek.isNotEmpty()) {
            listWithSection.add(CommonHeaderSection(R.string.lbl_this_week_visits))
            listWithSection.addAll(thisWeek)
        }

        if (thisMonth.isNotEmpty()) {
            listWithSection.add(CommonHeaderSection(R.string.lbl_this_month_visits))
            listWithSection.addAll(thisMonth)
        }

        if (other.isNotEmpty()) {
            listWithSection.add(CommonHeaderSection(R.string.lbl_this_other_visits))
            listWithSection.addAll(other)
            if (!otherSectionAdded) {
                otherSectionAdded = true
            }
        }

        if (!addMoreSectionAdded) {
            addMoreSectionAdded = true
            listWithSection.add(ListItemFooter())
        }

        return listWithSection
    }

    private fun resetReceivedPrescriptionData() {
        offset = 0
        otherSectionAdded = false
        addMoreSectionAdded = false
    }
}
