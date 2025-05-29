package org.intelehealth.app.ui.prescription.adapter;

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.intelehealth.app.databinding.PrescriptionListItemBinding
import org.intelehealth.app.ui.prescription.viewholder.PrescriptionViewHolder
import org.intelehealth.common.ui.adapter.BaseRecyclerViewAdapter
import org.intelehealth.data.offline.entity.Prescription

/**
 * Created by Tanvir Hasan on 24/04/25.
 * Email: mhasann@intelihealth.org
 *
 * RecyclerView adapter for displaying a list of [Prescription] items.
 *
 * Extends [BaseRecyclerViewAdapter] to handle common RecyclerView boilerplate.
 * Provides functionality to update the list of prescriptions and notify the adapter of changes.
 *
 * @param context The context, usually the calling Activity or Fragment.
 * @param prescriptions The initial list of prescriptions to display. This list can be updated.
 */
class PrescriptionRecyclerViewAdapter(
    context: Context,
    private var prescriptions: MutableList<Prescription>
) : BaseRecyclerViewAdapter<Prescription>(context, prescriptions) {

    /**
     * Creates new views (invoked by the layout manager).
     * Inflates the item layout and creates a new [PrescriptionViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = PrescriptionListItemBinding.inflate(inflater, parent, false)
        return PrescriptionViewHolder(binding)
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager).
     * Binds the [Prescription] at the given [position] to the [PrescriptionViewHolder].
     * Sets a click listener on the view holder if one is provided.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Set click listener from the base adapter or fragment/activity
        viewHolderClickListener?.let { (holder as PrescriptionViewHolder).setViewClickListener(it) }
        // Bind data to the ViewHolder
        (holder as PrescriptionViewHolder).bind(getItem(position))
    }

    /**
     * Updates the list of prescriptions with new items and notifies the adapter.
     *
     * Appends the [newList] to the existing list of prescriptions.
     * Notifies the adapter that items have been changed, attempting to provide a specific range.
     * @param newList The new list of prescriptions to add.
     */
    fun updateList(newList: MutableList<Prescription>) {
        val oldListSize = prescriptions.size // Correctly get size before adding
        prescriptions.addAll(newList)
        if (newList.isNotEmpty()) {
            notifyItemRangeInserted(oldListSize, newList.size)
        }
    }
}