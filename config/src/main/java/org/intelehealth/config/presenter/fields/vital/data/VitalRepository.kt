package org.intelehealth.config.presenter.fields.vital.data

import org.intelehealth.config.room.dao.VitalDao
import javax.inject.Inject

/**
 * Created by Lincon Pradhan R. on 24-06-2024 - 11:22.
 * Email : lincon@intelehealth.org
 * Mob   :
 **/

class VitalRepository @Inject constructor(private val vitalDao: VitalDao) {

    fun getAllEnabledLiveFields() = vitalDao.getAllEnabledLiveFields()

    suspend fun getAllEnabledFields() = vitalDao.getAllEnabledFields()
}
