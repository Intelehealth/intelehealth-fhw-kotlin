package org.intelehealth.data.offline.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Created by Vaghela Mithun R. on 02-04-2024 - 10:26.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
interface CoreDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(items: T)

    @Update
    suspend fun update(obj: T)
}