package org.intelehealth.app.ui.prescription.adapter;

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.intelehealth.app.databinding.PrescriptionListItemBinding
import org.intelehealth.app.ui.prescription.viewholder.PrescriptionViewHolder
import org.intelehealth.common.model.ListItemHeaderSection
import org.intelehealth.common.ui.adapter.CommonHeaderSectionAdapter
import org.intelehealth.common.ui.adapter.FooterViewMoreAdapter
import org.intelehealth.data.offline.entity.Prescription
import java.util.LinkedList

/**
 * Created by Tanvir Hasan on 24/04/25.
 * Email: mhasann@intelihealth.org
 *
 * RecyclerView adapter for displaying a list of [Prescription : ListItemHeaderSection] items.
 *
 * Extends [CommonHeaderSectionAdapter] to handle common RecyclerView boilerplate.
 * Provides functionality to update the list of prescriptions and notify the adapter of changes.
 *
 * @param context The context, usually the calling Activity or Fragment.
 * @param prescriptions The initial list of prescriptions to display. This list can be updated.
 */
class PrescriptionAdapter(
    context: Context,
    private var prescriptions: LinkedList<ListItemHeaderSection>
) : FooterViewMoreAdapter<ListItemHeaderSection>(context, prescriptions) {

    init {
        setHasStableIds(true)
    }

    /**
     * Creates new views (invoked by the layout manager).
     * Inflates the item layout and creates a new [PrescriptionViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType != HEADER && viewType != FOOTER) {
            val binding = PrescriptionListItemBinding.inflate(inflater, parent, false)
            PrescriptionViewHolder(binding)
        } else super.onCreateViewHolder(parent, viewType)
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager).
     * Binds the [Prescription] at the given [position] to the [PrescriptionViewHolder].
     * Sets a click listener on the view holder if one is provided.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) != HEADER && holder is PrescriptionViewHolder && getItem(position) is Prescription) {
            // Set click listener from the base adapter or fragment/activity
            viewHolderClickListener?.let { holder.setViewClickListener(it) }
            // Bind data to the ViewHolder
            holder.bind(getItem(position) as Prescription)
        } else super.onBindViewHolder(holder, position)
    }
}
