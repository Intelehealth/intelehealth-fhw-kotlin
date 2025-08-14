package org.intelehealth.config.presenter.section.viewmodel

import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.config.presenter.section.data.ActiveSectionRepository

/**
 * Created by Lincon Pradhan
 * Email : lincon@intelehealth.org
 **/
class ActiveSectionViewModel(private val repository: ActiveSectionRepository) : BaseViewModel() {
    fun fetchActiveSection() = repository.getAllActiveSection()
}