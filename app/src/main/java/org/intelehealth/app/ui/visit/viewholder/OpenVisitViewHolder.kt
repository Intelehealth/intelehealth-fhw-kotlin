package org.intelehealth.app.ui.visit.viewholder

import org.intelehealth.app.databinding.ItemOpenVisitBinding
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import org.intelehealth.data.offline.entity.Patient

class OpenVisitViewHolder(
    private val binding: ItemOpenVisitBinding
) : BaseViewHolder(binding.root) {
    override var allowInstantClick = true
    fun bind(patient: Patient) {
        binding.patient = patient
        binding.cardItem.setOnClickListener(this)
        binding.executePendingBindings()
    }
}