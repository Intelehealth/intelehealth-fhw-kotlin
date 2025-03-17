package org.intelehealth.app.ui.aboutus.viewholder

import org.intelehealth.app.databinding.RowItemAboutUsSlideBinding
import org.intelehealth.common.ui.viewholder.BaseViewHolder

/**
 * Created by Vaghela Mithun R. on 17-03-2025 - 16:51.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class AboutUsThumbHolder(
    private val binding: RowItemAboutUsSlideBinding
) : BaseViewHolder(binding.root) {

    fun bind(thumbResourceId: Int) {
        binding.thumbId = thumbResourceId
    }
}
