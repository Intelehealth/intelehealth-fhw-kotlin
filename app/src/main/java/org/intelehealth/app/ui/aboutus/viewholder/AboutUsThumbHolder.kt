package org.intelehealth.app.ui.aboutus.viewholder

import org.intelehealth.app.databinding.RowItemAboutUsSlideBinding
import org.intelehealth.common.ui.viewholder.BaseViewHolder

/**
 * Created by Vaghela Mithun R. on 17-03-2025 - 16:51.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * ViewHolder for displaying a single thumbnail image in the "About Us" section.
 *
 * This ViewHolder is responsible for binding a thumbnail image resource ID
 * to the corresponding view in the item layout (`RowItemAboutUsSlideBinding`).
 * It extends [BaseViewHolder].
 *
 * @param binding The view binding for the item layout (`RowItemAboutUsSlideBinding`).
 */
class AboutUsThumbHolder(
    private val binding: RowItemAboutUsSlideBinding
) : BaseViewHolder(binding.root) {

    /**
     * Binds the thumbnail image resource ID to the view.
     *
     * @param thumbResourceId The integer resource ID of the thumbnail image.
     */
    fun bind(thumbResourceId: Int) {
        binding.thumbId = thumbResourceId
    }
}
