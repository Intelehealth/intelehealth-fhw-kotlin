package org.intelehealth.data.offline.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import org.intelehealth.data.offline.entity.VisitAttribute

@Dao
interface VisitAttributeDao : CoreDao<VisitAttribute> {

    @Query("SELECT * FROM tbl_visit_attribute WHERE visit_uuid = :visitUuid")
    fun getVisitAttributesByVisitUuid(visitUuid: String): LiveData<List<VisitAttribute>>

}