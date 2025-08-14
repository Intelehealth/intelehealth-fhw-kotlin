package org.intelehealth.app.ui.visit.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.data.provider.visit.VisitDetailRepository
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 10-06-2025 - 16:02.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltViewModel
class VisitDetailViewModel @Inject constructor(
    private val visitDetailRepository: VisitDetailRepository
) : BaseViewModel() {
    fun fetchVisitDetails(visitId: String) = visitDetailRepository.getVisitDetail(visitId)
}
