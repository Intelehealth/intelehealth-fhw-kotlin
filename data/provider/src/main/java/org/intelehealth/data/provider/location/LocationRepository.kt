package org.intelehealth.data.provider.location

import com.google.gson.Gson
import org.intelehealth.common.utility.PreferenceUtils
import org.intelehealth.data.network.model.SetupLocation
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 16-01-2025 - 14:12.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class LocationRepository @Inject constructor(
    private val dataSource: LocationDataSource, private val preferenceUtils: PreferenceUtils
) {
    fun getLocation() = dataSource.getLocation()

    fun saveLocation(location: SetupLocation) {
        val locationStr = Gson().toJson(location)
        preferenceUtils.location = locationStr
    }
}