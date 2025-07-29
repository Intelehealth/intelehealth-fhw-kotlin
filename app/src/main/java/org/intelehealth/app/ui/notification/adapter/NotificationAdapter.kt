package org.intelehealth.app.ui.notification.adapter;

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.intelehealth.app.databinding.NotificationListItemBinding
import org.intelehealth.app.databinding.PrescriptionListItemBinding
import org.intelehealth.app.ui.prescription.viewholder.PrescriptionViewHolder
import org.intelehealth.common.model.ListItemHeaderSection
import org.intelehealth.common.ui.adapter.CommonHeaderSectionAdapter
import org.intelehealth.common.ui.adapter.FooterViewMoreAdapter
import org.intelehealth.data.offline.entity.LocalNotification
import org.intelehealth.data.offline.entity.VisitDetail
import java.util.LinkedList


class NotificationAdapter(
    context: Context,
    private var notification: LinkedList<ListItemHeaderSection>
) : FooterViewMoreAdapter<ListItemHeaderSection>(context, notification) {

    init {
        setHasStableIds(true)
    }

    /**
     * Creates new views (invoked by the layout manager).
     * Inflates the item layout and creates a new [NotificationViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType != HEADER && viewType != FOOTER) {
            val binding = NotificationListItemBinding.inflate(inflater, parent, false)
            NotificationViewHolder(binding)
        } else super.onCreateViewHolder(parent, viewType)
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager).
     * Binds the [VisitDetail] at the given [position] to the [NotificationViewHolder].
     * Sets a click listener on the view holder if one is provided.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) != HEADER && holder is NotificationViewHolder && getItem(position) is LocalNotification) {
            // Set click listener from the base adapter or fragment/activity
            viewHolderClickListener?.let { holder.setViewClickListener(it) }
            // Bind data to the ViewHolder
            holder.bind(getItem(position) as LocalNotification)
        } else super.onBindViewHolder(holder, position)
    }
}
