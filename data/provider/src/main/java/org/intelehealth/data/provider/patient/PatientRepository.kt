package org.intelehealth.data.provider.patient

import org.intelehealth.data.offline.dao.PatientDao
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 20-12-2024 - 20:16.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class PatientRepository @Inject constructor(
    private val patientDataSource: PatientDataSource, private val patientDao: PatientDao
) {}