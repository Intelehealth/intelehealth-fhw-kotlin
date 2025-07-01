package org.intelehealth.app.ui.patient.viewmodel

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
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.common.model.ListItemFooter
import org.intelehealth.common.model.ListItemHeaderSection
import org.intelehealth.common.state.Result
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.common.utility.CommonConstants
import org.intelehealth.common.utility.CommonConstants.LIMIT
import org.intelehealth.common.utility.NO_DATA_FOUND
import org.intelehealth.data.offline.entity.RecentHistory
import org.intelehealth.data.provider.patient.search.SearchPatientRepository
import java.util.LinkedList
import javax.inject.Inject

@HiltViewModel
class FindPatientViewModel @Inject constructor(
    private val repository: SearchPatientRepository,
    networkHelper: NetworkHelper
) : BaseViewModel(networkHelper = networkHelper) {

    private var patientData =
        MutableLiveData<Result<List<ListItemHeaderSection>>>()
    val patientLiveData: LiveData<Result<List<ListItemHeaderSection>>> = patientData

    private var patientPageData = MutableLiveData<List<ListItemHeaderSection>>()
    val patientPageLiveData: LiveData<List<ListItemHeaderSection>> = patientPageData

    private var offset = 0

    fun searchPatient(
        searchQuery: String
    ) = viewModelScope.launch {
        offset = 0
        val flow = repository.searchPatient(searchQuery, offset)
        flow.onStart {
            patientData.postValue(Result.Loading("Please wait..."))
        }.catch { e ->
            Timber.e(e) // Log the error
            patientData.postValue(Result.Error(e.message ?: "Unknown error occurred"))
        }.flowOn(dispatcher).collectLatest {
            if (it.isNotEmpty()) {
                val patients = LinkedList<ListItemHeaderSection>()
                patients.addAll(it)
                if (it.size >= LIMIT) patients.addLast(ListItemFooter())
                patientData.postValue(Result.Success(patients, ""))
                offset = it.size
            } else patientData.postValue(Result.Fail(NO_DATA_FOUND))
        }
    }

    fun loadPage(searchQuery: String) = viewModelScope.launch {
        val flow = repository.searchPatient(searchQuery, offset)
        flow.collectLatest {
            if (it.isNotEmpty()) {
                offset += it.size
                patientPageData.postValue(it.toMutableList())
                if (it.size < LIMIT && searchQuery.isEmpty()) {
                    delay(100)
                    patientPageData.postValue(emptyList())
                }
            } else patientPageData.postValue(emptyList())
        }
    }

    fun addRecentSearchHistory(value: String) = viewModelScope.launch {
        repository.addRecentSearchHistory(value)
    }

    fun getRecentSearchHistory() = repository.getRecentSearchHistory()

    fun updateRecentSearchHistory(recentHistory: RecentHistory) = viewModelScope.launch {
        repository.updateRecentSearchHistory(recentHistory)
    }

    fun clearRecentSearchHistory() = viewModelScope.launch{
        repository.deleteRecentSearchHistory()
    }
}
