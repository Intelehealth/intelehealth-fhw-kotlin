package org.intelehealth.app.ui.prescription.adapter

import android.content.Context
import android.view.ViewGroup
import org.intelehealth.app.databinding.ListItemRowMedicationBinding
import org.intelehealth.common.ui.adapter.BaseRecyclerViewHolderAdapter
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import org.intelehealth.data.offline.model.Medication

/**
 * Created by Vaghela Mithun R. on 10-07-2025 - 20:43.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class MedicationAdapter(
    context: Context,
    medications: List<Medication>
) : BaseRecyclerViewHolderAdapter<Medication, MedicationViewHolder>(context, medications.toMutableList()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
        return MedicationViewHolder(
            ListItemRowMedicationBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MedicationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class MedicationViewHolder(
    private val binding: ListItemRowMedicationBinding
) : BaseViewHolder(binding.root) {
    fun bind(medication: Medication) {
        binding.medication = medication
        binding.executePendingBindings()
    }
}
