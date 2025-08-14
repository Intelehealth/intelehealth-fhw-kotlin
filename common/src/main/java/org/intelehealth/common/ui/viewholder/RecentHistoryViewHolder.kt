package org.intelehealth.common.ui.viewholder

import org.intelehealth.common.databinding.ListRowItemRecentHistoryBinding
import org.intelehealth.common.model.RecentItem

/**
 * Created by Vaghela Mithun R. on 01-07-2025 - 13:56.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class RecentHistoryViewHolder(
    private val binding: ListRowItemRecentHistoryBinding
) : BaseViewHolder(binding.root) {

    fun bind(item: RecentItem, hasCloseIcon: Boolean) {
        binding.hasCloseIcon = hasCloseIcon
        binding.recentItem = item
        binding.chipRecentHistory.tag = item
        binding.chipRecentHistory.setOnClickListener(this)
        binding.chipRecentHistory.setOnCloseIconClickListener(this)
        binding.executePendingBindings()
    }
}
