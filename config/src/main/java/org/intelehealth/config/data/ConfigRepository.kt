package org.intelehealth.config.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.intelehealth.common.state.Result
import org.intelehealth.common.utility.NO_DATA_FOUND
import org.intelehealth.config.network.response.ConfigResponse
import org.intelehealth.config.room.ConfigDatabase
import org.intelehealth.config.room.entity.PatientRegistrationFields
import org.intelehealth.config.utility.FieldGroup
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 15-03-2024 - 16:21.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

class ConfigRepository @Inject constructor(
    private val configDb: ConfigDatabase,
    private val dataSource: ConfigDataSource,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) {
//    constructor(context: Context) : this(
//        configDb = ConfigDatabase.getInstance(context),
//        ConfigDataSource(
//            WebClientProvider.getApiClient(), NetworkHelper(context)
//        ), scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
//    )

    fun fetchAndUpdateConfig(onCompleted: (Result<*>) -> Unit) {
        scope.launch {
            dataSource.getConfig().collect { result ->
                if (result.isSuccess()) {
                    result.data?.let {
                        withContext(Dispatchers.IO) {
                            saveAllConfig(it, this) { onCompleted(result) }
                        }
                    } ?: onCompleted(Result.Fail<Any>(NO_DATA_FOUND))
                } else onCompleted(result)
            }
        }
    }

    fun suspendFetchAndUpdateConfig() = dataSource.getConfig()

    fun saveAllConfig(config: ConfigResponse, coroutineScope: CoroutineScope = scope, onCompleted: () -> Unit) {
        coroutineScope.launch {
            configDb.clearAllTables()
            configDb.specializationDao().save(config.specialization)
            configDb.languageDao().save(config.language)
            groupingPatientRegFields(config.patientRegFields.personal, FieldGroup.PERSONAL)
            groupingPatientRegFields(config.patientRegFields.address, FieldGroup.ADDRESS)
            groupingPatientRegFields(config.patientRegFields.other, FieldGroup.OTHER)
            configDb.patientVitalDao().save(config.patientVitals)
            configDb.patientDiagnosticsDao().save(config.diagnostics)
            configDb.activeSectionDao().save(config.patientVisitSection)
            configDb.activeSectionDao().save(config.homeScreen)
            config.patientVisitSummery.apply {
                chatSection = if (config.webrtcSection) config.webrtcStatus.chat else false
                videoSection = if (config.webrtcSection) config.webrtcStatus.video else false
                vitalSection = config.patientVitalSection
                activeStatusPatientAddress = config.activeStatusPatientAddress
                activeStatusPatientOther = config.activeStatusPatientOther
                activeStatusAbha = config.activeStatusAbha
                activeStatusPatientFamilyMemberRegistration = config.activeStatusFamilyRegistration
                activeStatusPatientHouseholdSurvey = config.activeStatusHouseholdSurvey
                activeStatusRosterQuestionnaireSection = config.rosterQuestionnaireSection
                activeStatusDiagnosticsSection = config.patientDiagnosticsSection
                activeStatusPatientDraftSurvey = config.patientDraftSurvey
            }.also { configDb.featureActiveStatusDao().add(it) }
            onCompleted.invoke()
        }
    }

    private suspend fun groupingPatientRegFields(
        fields: List<PatientRegistrationFields>, group: FieldGroup
    ) {
        fields.map {
            it.groupId = group.value
            return@map it
        }.let { configDb.patientRegFieldDao().save(it) }
    }
}
