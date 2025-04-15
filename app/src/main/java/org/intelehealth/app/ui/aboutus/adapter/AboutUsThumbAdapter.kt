package org.intelehealth.app.ui.aboutus.adapter

import android.content.Context
import android.view.ViewGroup
import org.intelehealth.app.databinding.RowItemAboutUsSlideBinding
import org.intelehealth.app.ui.aboutus.viewholder.AboutUsThumbHolder
import org.intelehealth.common.ui.adapter.BaseRecyclerViewHolderAdapter

/**
 * Created by Vaghela Mithun R. on 17-03-2025 - 16:51.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Adapter for displaying a list of thumbnail images in the "About Us" section.
 *
 * This adapter extends [BaseRecyclerViewHolderAdapter] to manage a list of
 * thumbnail image resources (represented by their integer resource IDs) and
 * display them in a RecyclerView.
 *
 * @param context The context in which the adapter is being used.
 * @param thumbList A mutable list of integer resource IDs representing the
 *   thumbnail images to be displayed.
 */
class AboutUsThumbAdapter(
    context: Context, thumbList: MutableList<Int>
) : BaseRecyclerViewHolderAdapter<Int, AboutUsThumbHolder>(context, thumbList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutUsThumbHolder {
        val binding = RowItemAboutUsSlideBinding.inflate(inflater, parent, false)
        return AboutUsThumbHolder(binding)
    }

    override fun onBindViewHolder(holder: AboutUsThumbHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
