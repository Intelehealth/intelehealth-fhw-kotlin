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
