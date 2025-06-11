package org.intelehealth.data.provider.visit

import org.intelehealth.data.offline.dao.VisitDao
import org.intelehealth.data.offline.entity.PatientAttributeTypeMaster
import org.intelehealth.data.provider.utils.EncounterType
import org.intelehealth.data.provider.utils.ObsConcept
import org.intelehealth.data.provider.utils.VisitAttributeType
import javax.inject.Inject

class VisitDetailRepository @Inject constructor(private val visitDao: VisitDao) {
    fun getVisitDetail(
        visitUuid: String
    ) = visitDao.getVisitDetailsByUuid(
        visitId = visitUuid,
        visitCompleteEnType = EncounterType.VISIT_COMPLETE.value,
        exitSurveyEnType = EncounterType.PATIENT_EXIT_SURVEY.value,
        emergencyEnType = EncounterType.EMERGENCY.value,
        adultInitialEnType = EncounterType.ADULT_INITIAL.value,
        followUpConceptId = ObsConcept.FOLLOW_UP.value,
        specialityAttrType = VisitAttributeType.DR_SPECIALITY.value,
        patientAttrName = PatientAttributeTypeMaster.TELEPHONE
    )
}
