package org.intelehealth.data.offline.entity

import androidx.room.Entity

/**
 * Created by Vaghela Mithun R. on 20-01-2025 - 19:24.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Entity
data class StatusCount(
    val pending: Int = 0, val completed: Int = 0, val received: Int = 0, val total: Int = 0
)