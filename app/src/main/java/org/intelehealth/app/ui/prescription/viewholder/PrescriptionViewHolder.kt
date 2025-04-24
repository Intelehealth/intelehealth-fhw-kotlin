package org.intelehealth.app.ui.prescription.viewholder

import android.animation.ObjectAnimator
import android.widget.ImageView
import org.intelehealth.app.databinding.PrescriptionListItemBinding
import org.intelehealth.app.databinding.RowItemHelpFaqBinding
import org.intelehealth.app.model.help.FAQItem
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import org.intelehealth.data.offline.entity.Patient

/**
 * Created by Tanvir Hasan on 24-04-25
 * Email : mhasan@intelehealth.org
 **/


class PrescriptionViewHolder(private val binding: PrescriptionListItemBinding) : BaseViewHolder(binding.root) {

    override var allowInstantClick = true
    fun bind(patient: Patient) {
        binding.patient = patient
        binding.cardItem.setOnClickListener(this)
        binding.executePendingBindings()
    }
}
