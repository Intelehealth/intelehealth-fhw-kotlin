package org.intelehealth.data.provider.patient.search

import org.intelehealth.data.offline.dao.PatientAttributeDao
import org.intelehealth.data.offline.dao.PatientDao
import org.intelehealth.data.provider.utils.EncounterType
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 26-05-2025 - 17:23.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class SearchPatientRepository @Inject constructor(
    private val patientDao: PatientDao
) {
    fun searchPatient(searchQuery: String, offset: Int) = patientDao.searchPatient(
        presConceptId = EncounterType.VISIT_COMPLETE.value,
        visitCloseConceptId = EncounterType.PATIENT_EXIT_SURVEY.value,
        emergencyConceptId = EncounterType.EMERGENCY.value,
        searchQuery = searchQuery,
        offset = offset
    )
}
