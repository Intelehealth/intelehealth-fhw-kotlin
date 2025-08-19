package org.intelehealth.app.ui.patient.viewholder

import org.intelehealth.app.databinding.ListRowItemPatientBinding
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import org.intelehealth.data.offline.entity.VisitDetail

/**
 * Created by Vaghela Mithun on 27-06-25
 * Email : mithun@intelehealth.org
 *
 * PatientViewHolder: Binds patient data to a list item in the RecyclerView.
 * Displays patient information and handles clicks on the list item.
 *
 **/
class PatientViewHolder(private val binding: ListRowItemPatientBinding) : BaseViewHolder(binding.root) {
    override var allowInstantClick = true
    fun bind(visitDetail: VisitDetail) {
        binding.patient = visitDetail
        binding.cardPatientItem.tag = visitDetail
        binding.cardPatientItem.setOnClickListener(this)
        binding.executePendingBindings()
    }
}
