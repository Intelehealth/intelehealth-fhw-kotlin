package org.intelehealth.common.ui.adapter

import android.content.Context
import android.view.ViewGroup
import org.intelehealth.common.databinding.ListRowItemRecentHistoryBinding
import org.intelehealth.common.model.RecentItem
import org.intelehealth.common.ui.viewholder.RecentHistoryViewHolder

/**
 * Created by Vaghela Mithun R. on 01-07-2025 - 13:54.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class RecentHistoryAdapter(
    context: Context,
    items: MutableList<RecentItem>,
    private val hasCloseIcon: Boolean = false
) : BaseRecyclerViewHolderAdapter<RecentItem, RecentHistoryViewHolder>(context, items) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentHistoryViewHolder {
        val binding = ListRowItemRecentHistoryBinding.inflate(inflater, parent, false)
        return RecentHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentHistoryViewHolder, position: Int) {
        viewHolderClickListener?.let { holder.setViewClickListener(it) }
        holder.bind(items[position], hasCloseIcon)
    }
}
