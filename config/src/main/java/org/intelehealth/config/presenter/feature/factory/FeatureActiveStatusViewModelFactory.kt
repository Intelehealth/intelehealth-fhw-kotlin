package org.intelehealth.config.presenter.feature.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.intelehealth.config.presenter.feature.data.ActiveFeatureStatusRepository
import org.intelehealth.config.presenter.feature.viewmodel.ActiveFeatureStatusViewModel

/**
 * Created by Vaghela Mithun R. on 15-04-2024 - 15:54.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class FeatureActiveStatusViewModelFactory(private val repository: ActiveFeatureStatusRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ActiveFeatureStatusViewModel(repository) as T
    }
}
