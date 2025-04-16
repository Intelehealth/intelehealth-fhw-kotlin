package org.intelehealth.config.presenter.feature.data

import org.intelehealth.config.room.dao.ActiveFeatureStatusDao
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 10-04-2024 - 18:11.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class ActiveFeatureStatusRepository @Inject constructor(
    private val activeFeatureStatusDao: ActiveFeatureStatusDao
) {
    fun getActiveFeatureStatus() = activeFeatureStatusDao.getActiveFeatureStatusLiveRecord()

    suspend fun getAllRecord() = activeFeatureStatusDao.getAllRecord()

    suspend fun getRecord() = activeFeatureStatusDao.getRecord()
}
