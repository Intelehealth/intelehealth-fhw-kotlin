package org.intelehealth.common.model

/**
 * Created by Vaghela Mithun R. on 03-06-2025 - 23:30.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class ListItemFooter : ListItemHeaderSection {
    override fun isHeader(): Boolean = false
    override fun isFooter(): Boolean = true
}
