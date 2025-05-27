package org.intelehealth.app.ui.help.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.intelehealth.app.databinding.RowItemHelpFaqBinding
import org.intelehealth.app.model.help.FAQItem
import org.intelehealth.app.ui.help.viewholder.FAQViewHolder
import org.intelehealth.common.ui.adapter.BaseRecyclerViewAdapter

/**
 * Created by Vaghela Mithun R. on 18-02-2025 - 15:42.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * [RecyclerView.Adapter] for displaying a list of [FAQItem] objects.
 *
 * This adapter is responsible for creating and binding [FAQViewHolder]
 * instances to display FAQ items in a [RecyclerView]. It extends
 * [BaseRecyclerViewAdapter] to provide common functionality for RecyclerView adapters.
 *
 * @property context The [Context] used for inflating layouts.
 * @property faqItems The [MutableList] of [FAQItem] objects to display.
 */
class FAQAdapter(
    context: Context,
    faqItems: MutableList<FAQItem>
) : BaseRecyclerViewAdapter<FAQItem>(context, faqItems) {

    /**
     * Creates a new [FAQViewHolder] instance.
     *
     * This method inflates the layout for a single FAQ item and
     * returns a new [FAQViewHolder] to hold it.
     *
     * @param parent The [ViewGroup] into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new [FAQViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RowItemHelpFaqBinding.inflate(inflater, parent, false)
        return FAQViewHolder(binding)
    }

    /**
     * Binds a [FAQItem] to a [FAQViewHolder].
     *
     * This method is called to update the contents of a [FAQViewHolder]
     * to reflect the item at the given position.
     *
     * @param holder The [RecyclerView.ViewHolder] which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        viewHolderClickListener?.let { (holder as FAQViewHolder).setViewClickListener(it) }
        (holder as FAQViewHolder).bind(getItem(position))
    }
}
