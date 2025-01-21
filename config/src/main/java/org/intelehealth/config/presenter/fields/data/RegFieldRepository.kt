package org.intelehealth.config.presenter.fields.data

import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.config.room.dao.PatientRegFieldDao
import org.intelehealth.config.utility.FieldGroup
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 10-04-2024 - 18:11.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
open class RegFieldRepository @Inject constructor(private val regFieldDao: PatientRegFieldDao) {
    suspend fun getGroupFields(group: FieldGroup) = regFieldDao.getGroupFields(group.value)

    suspend fun getAllRecord() = regFieldDao.getAllRecord()

    fun getAllEnabledGroupField(group: FieldGroup) = regFieldDao.getAllEnabledLiveGroupFields(group.value)

    fun getAllEnabledLiveFields() = regFieldDao.getAllEnabledLiveFields()

    fun getAllMandatoryLiveFields() = regFieldDao.getAllMandatoryLiveFields()

    fun getAllEditableLiveFields() = regFieldDao.getAllEditableLiveFields()

    fun getLiveRecord(name: String) = regFieldDao.getLiveRecord(name)
}