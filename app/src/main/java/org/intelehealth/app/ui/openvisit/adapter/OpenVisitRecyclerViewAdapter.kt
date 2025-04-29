package org.intelehealth.app.ui.openvisit.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.intelehealth.app.databinding.OpenVisitListItemBinding
import org.intelehealth.app.ui.openvisit.viewholder.OpenVisitViewHolder
import org.intelehealth.common.ui.adapter.BaseRecyclerViewAdapter
import org.intelehealth.data.offline.entity.Patient

class OpenVisitRecyclerViewAdapter(
    context: Context,
    patientList: MutableList<Patient>
) : BaseRecyclerViewAdapter<Patient>(context, patientList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = OpenVisitListItemBinding.inflate(inflater, parent, false)
        return OpenVisitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        viewHolderClickListener?.let { (holder as OpenVisitViewHolder).setViewClickListener(it) }
        (holder as OpenVisitViewHolder).bind(getItem(position))
    }

}