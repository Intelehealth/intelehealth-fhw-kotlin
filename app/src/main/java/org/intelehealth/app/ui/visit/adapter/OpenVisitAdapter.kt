package org.intelehealth.app.ui.visit.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.intelehealth.app.databinding.ListRowItemOpenVisitBinding
import org.intelehealth.app.ui.prescription.viewholder.PrescriptionViewHolder
import org.intelehealth.app.ui.visit.viewholder.OpenVisitViewHolder
import org.intelehealth.common.model.ListItemHeaderSection
import org.intelehealth.common.ui.adapter.FooterViewMoreAdapter
import org.intelehealth.data.offline.entity.VisitDetail
import java.util.LinkedList

class OpenVisitAdapter(
    context: Context,
    visits: LinkedList<ListItemHeaderSection>
) : FooterViewMoreAdapter<ListItemHeaderSection>(context, visits) {

    init {
        setHasStableIds(true)
    }

    /**
     * Creates new views (invoked by the layout manager).
     * Inflates the item layout and creates a new [PrescriptionViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType != HEADER && viewType != FOOTER) {
            val binding = ListRowItemOpenVisitBinding.inflate(inflater, parent, false)
            OpenVisitViewHolder(binding)
        } else super.onCreateViewHolder(parent, viewType)
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager).
     * Binds the [VisitDetail] at the given [position] to the [PrescriptionViewHolder].
     * Sets a click listener on the view holder if one is provided.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) != HEADER && holder is OpenVisitViewHolder && getItem(position) is VisitDetail) {
            // Set click listener from the base adapter or fragment/activity
            viewHolderClickListener?.let { holder.setViewClickListener(it) }
            // Bind data to the ViewHolder
            holder.bind(getItem(position) as VisitDetail)
        } else super.onBindViewHolder(holder, position)
    }

}
