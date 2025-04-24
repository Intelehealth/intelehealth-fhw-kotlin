package org.intelehealth.config.presenter.fields.patient.data

import org.intelehealth.config.room.dao.PatientRegFieldDao
import org.intelehealth.config.utility.PatientInfoGroup
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 10-04-2024 - 18:11.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
open class RegFieldRepository @Inject constructor(private val regFieldDao: PatientRegFieldDao) {
    suspend fun getGroupFields(group: PatientInfoGroup) = regFieldDao.getGroupFields(group.value)

    suspend fun getAllRecord() = regFieldDao.getAllRecord()

    fun getAllEnabledGroupField(group: PatientInfoGroup) = regFieldDao.getAllEnabledLiveGroupFields(group.value)

    fun getAllEnabledLiveFields() = regFieldDao.getAllEnabledLiveFields()

    fun getAllMandatoryLiveFields() = regFieldDao.getAllMandatoryLiveFields()

    fun getAllEditableLiveFields() = regFieldDao.getAllEditableLiveFields()

    fun getLiveRecord(name: String) = regFieldDao.getLiveRecord(name)
}
