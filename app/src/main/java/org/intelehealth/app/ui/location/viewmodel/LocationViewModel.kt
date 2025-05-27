package org.intelehealth.app.ui.location.viewmodel

import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.data.network.model.SetupLocation
import org.intelehealth.data.provider.location.LocationRepository
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 18-01-2025 - 11:29.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * ViewModel for managing location data and interactions.
 *
 * This ViewModel handles the retrieval and saving of user location information.
 * It interacts with the [LocationRepository] to access and modify location data.
 *
 * @property locationRepository The repository used to access location data.
 * @property networkHelper Helper class for network-related operations.
 */
@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel(networkHelper = networkHelper) {

    /**
     * The currently selected location by the user.
     *
     * This property holds the [SetupLocation] object representing the user's
     * chosen location. It can be set by the UI and is used when saving the
     * location.
     */
    var selectedLocation: SetupLocation? = null

    /**
     * Retrieves the user's current location.
     *
     * @return A LiveData object containing the user's location as a
     * [SetupLocation] object, or null if no location is set.
     */
    fun getLocation() = locationRepository.getLocation().asLiveData()

    /**
     * Saves the currently selected location.
     *
     * This method saves the location stored in [selectedLocation] using the
     * [LocationRepository].
     *
     * @return The result of the save operation, which could be a LiveData or
     * other appropriate type depending on your repository implementation.
     * Returns null if no location is selected.
     */
    fun saveLocation() = selectedLocation?.let { locationRepository.saveLocation(it) }
}
