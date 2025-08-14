package org.intelehealth.common.model

/**
 * Created by Vaghela Mithun R. on 03-06-2025 - 12:02.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
data class CommonHeaderSection(val section: Int) : ListItemHeaderSection {
    override fun isHeader(): Boolean = true
}



