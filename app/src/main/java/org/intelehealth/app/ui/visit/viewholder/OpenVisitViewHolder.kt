package org.intelehealth.app.ui.visit.viewholder

import org.intelehealth.app.databinding.ListRowItemOpenVisitBinding
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import org.intelehealth.data.offline.entity.VisitDetail

class OpenVisitViewHolder(
    private val binding: ListRowItemOpenVisitBinding
) : BaseViewHolder(binding.root) {
    override var allowInstantClick = true
    fun bind(visitDetail: VisitDetail) {
        binding.visit = visitDetail
        binding.cardItem.tag = visitDetail
        binding.cardItem.setOnClickListener(this)
        binding.executePendingBindings()
    }
}
