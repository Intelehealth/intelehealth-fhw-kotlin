package org.intelehealth.app.ui.location.viewmodel

import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.data.network.model.SetupLocation
import org.intelehealth.data.provider.location.LocationRepository
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 18-01-2025 - 11:29.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltViewModel
class LocationViewModel @Inject constructor(private val locationRepository: LocationRepository) : BaseViewModel() {

    var selectedLocation: SetupLocation? = null

    fun getLocation() = locationRepository.getLocation().asLiveData()

    fun saveLocation() = selectedLocation?.let { locationRepository.saveLocation(it) }
}