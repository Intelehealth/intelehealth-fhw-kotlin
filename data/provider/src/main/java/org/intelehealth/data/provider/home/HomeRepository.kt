package org.intelehealth.data.provider.home

import org.intelehealth.data.offline.OfflineDatabase
import org.intelehealth.data.provider.utils.EncounterType
import org.intelehealth.data.provider.utils.ObsConcept
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 22-01-2025 - 16:33.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class HomeRepository @Inject constructor(private val database: OfflineDatabase) {
    fun getVisitStatusCount() = database.visitDao().getVisitStatusCount(
        EncounterType.VISIT_COMPLETE.value,
        EncounterType.PATIENT_EXIT_SURVEY.value
    )

    fun getFollowUpStatusCount() = database.observationDao().getFollowUpStatusCount(
        ObsConcept.FOLLOW_UP.value,
        EncounterType.PATIENT_EXIT_SURVEY.value
    )

    fun getAppointmentStatusCount() = database.appointmentDao().getAppointmentStatusCount()
}
