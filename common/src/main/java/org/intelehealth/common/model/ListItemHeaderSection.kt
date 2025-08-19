package org.intelehealth.common.model

/**
 * Created by Vaghela Mithun R. on 29-03-2024 - 17:28.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
interface ListItemHeaderSection {
    fun isHeader(): Boolean
    fun isFooter(): Boolean {
        return false
    }
}
