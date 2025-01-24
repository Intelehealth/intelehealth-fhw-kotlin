package org.intelehealth.data.provider.home

import org.intelehealth.data.offline.OfflineDatabase
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 22-01-2025 - 16:33.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class HomeRepository @Inject constructor(private val database: OfflineDatabase) {
    fun getVisitStatusCount() = database.visitDao().getVisitStatusCount()

    fun getFollowUpStatusCount() = database.observationDao().getFollowUpStatusCount(
        "e8caffd6-5d22-41c4-8d6a-bc31a44d0c86",
        "629a9d0b-48eb-405e-953d-a5964c88dc30"
    )

    fun getAppointmentStatusCount() = database.appointmentDao().getAppointmentStatusCount()
}