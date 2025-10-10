package org.intelehealth.common.ui.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.intelehealth.common.databinding.RowItemCommonHeaderSectionBinding
import org.intelehealth.common.model.CommonHeaderSection
import org.intelehealth.common.model.ListItemHeaderSection
import org.intelehealth.common.ui.adapter.ListItemHeaderSectionAdapter.Companion.HEADER
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import java.util.LinkedList

/**
 * Created by Vaghela Mithun R. on 03-06-2025 - 11:55.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

/** Adapter for displaying common header sections in a RecyclerView.
 * This adapter extends [ListItemHeaderSectionAdapter] to handle common header sections.
 *
 * @param context The context in which the adapter is created.
 * @param objectsList The list of items to be displayed, which should include instances of [CommonHeaderSection].
 */
open class CommonHeaderSectionAdapter<I : ListItemHeaderSection>(
    context: Context, objectsList: LinkedList<I>
) : ListItemHeaderSectionAdapter<I>(context, objectsList) {
    /**
     * Creates a new view holder for the common header section.
     * This method is called by the RecyclerView to create a new view holder for the header section.
     *
     * @param parent The parent view group into which the new view will be added.
     * @param viewType The type of view to create, which is expected to be HEADER for this adapter.
     * @return A new instance of [CommonHeaderSectionViewHolder] with the inflated binding.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == HEADER) {
            val binding = RowItemCommonHeaderSectionBinding.inflate(inflater, parent, false)
            CommonHeaderSectionViewHolder(binding)
        } else super.createViewHolder(parent, viewType)
    }

    /**
     * Binds the data to the view holder.
     * This method is called by the RecyclerView to bind data to the view holder at the specified position.
     *
     * @param holder The view holder to bind data to.
     * @param position The position of the item in the adapter.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CommonHeaderSectionViewHolder && getItemViewType(position) == HEADER && getItem(position).isHeader()) {
            val headerSection = getItem(position) as CommonHeaderSection
            holder.bind(headerSection)
        } else {
            super.bindViewHolder(holder, position)
        }
    }
}

/**
 * ViewHolder for the common header section.
 * This class binds the header section data to the view.
 *
 * @param binding The binding for the row item that contains the header section layout.
 */
class CommonHeaderSectionViewHolder(
    private val binding: RowItemCommonHeaderSectionBinding
) : BaseViewHolder(binding.root) {
    fun bind(headerSection: CommonHeaderSection) {
        binding.section = headerSection.section
    }
}
