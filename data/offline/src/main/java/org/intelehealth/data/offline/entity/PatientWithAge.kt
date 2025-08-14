package org.intelehealth.data.offline.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Created by Vaghela Mithun R. on 04-07-2025 - 19:21.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Entity
class PatientWithAge(
    @ColumnInfo("age")
    var age: Int? = 0,
) : Patient()
