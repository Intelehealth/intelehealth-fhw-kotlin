package org.intelehealth.data.provider.prescription

import org.intelehealth.common.utility.DateTimeUtils
import org.intelehealth.data.offline.OfflineDatabase
import org.intelehealth.data.provider.utils.EncounterType
import javax.inject.Inject

class PrescriptionRepository @Inject constructor(private val database: OfflineDatabase) {
    fun getPrescriptionCount() = database.visitDao().getVisitStatusCount(
        EncounterType.VISIT_COMPLETE.value,
        EncounterType.PATIENT_EXIT_SURVEY.value
    )

    fun getReceivedPrescriptions(searchQuery: String, limit: Int, offset: Int) =
        database.visitDao().getReceivedPrescriptions(
            EncounterType.VISIT_COMPLETE.value,
            EncounterType.PATIENT_EXIT_SURVEY.value,
            EncounterType.EMERGENCY.value,
            searchQuery,
            limit,
            offset
        )

    fun getPendingPrescriptions(searchQuery: String, limit: Int, offset: Int) =
        database.visitDao().getReceivedPrescriptions(
            EncounterType.VISIT_COMPLETE.value,
            EncounterType.PATIENT_EXIT_SURVEY.value,
            EncounterType.EMERGENCY.value,
            searchQuery,
            limit,
            offset
        )
}