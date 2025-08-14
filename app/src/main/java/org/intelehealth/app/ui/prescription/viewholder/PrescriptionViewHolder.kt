package org.intelehealth.app.ui.prescription.viewholder

import org.intelehealth.app.databinding.PrescriptionListItemBinding
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import org.intelehealth.data.offline.entity.VisitDetail

/**
 * Created by Tanvir Hasan on 24-04-25
 * Email : mhasan@intelehealth.org
 *
 * PrescriptionViewHolder: Binds patient data to a list item in the RecyclerView.
 * Displays patient information and handles clicks on the list item.
 *
 **/
class PrescriptionViewHolder(private val binding: PrescriptionListItemBinding) : BaseViewHolder(binding.root) {
    override var allowInstantClick = true
    fun bind(visitDetail: VisitDetail) {
        binding.prescription = visitDetail
        binding.cardPatientItem.tag = visitDetail
        binding.cardPatientItem.setOnClickListener(this)
        binding.btnSharePrescription.tag = visitDetail
        binding.btnSharePrescription.setOnClickListener(this)
        binding.executePendingBindings()
    }
}
