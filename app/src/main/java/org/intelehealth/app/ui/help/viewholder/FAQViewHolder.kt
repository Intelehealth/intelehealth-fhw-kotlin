package org.intelehealth.app.ui.help.viewholder

import android.animation.ObjectAnimator
import android.widget.ImageView
import org.intelehealth.app.databinding.RowItemHelpFaqBinding
import org.intelehealth.app.model.help.FAQItem
import org.intelehealth.common.ui.viewholder.BaseViewHolder

/**
 * Created by Vaghela Mithun R. on 18-02-2025 - 15:46.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * [RecyclerView.ViewHolder] for displaying a [FAQItem] in a [RecyclerView].
 *
 * This ViewHolder is responsible for binding a [FAQItem] to the
 * corresponding layout (`RowItemHelpFaqBinding`) and displaying
 * its data. It extends [BaseViewHolder] to provide common functionality for
 * ViewHolders.
 *
 * @property binding The [RowItemHelpFaqBinding] instance for this ViewHolder.
 */
class FAQViewHolder(private val binding: RowItemHelpFaqBinding) : BaseViewHolder(binding.root) {

    override var allowInstantClick = true

    /**
     * Binds a [FAQItem] to the ViewHolder's layout.
     *
     * This method updates the layout with the data from the provided
     * [FAQItem] and executes any pending data binding updates.
     *
     * @param faqItem The [FAQItem] to bind to the layout.
     */
    fun bind(faqItem: FAQItem) {
        binding.faqItem = faqItem
        binding.btnExpandableQuestion.setOnClickListener(this)
        binding.btnExpandableQuestion.isSelected = faqItem.isExpanded
        binding.executePendingBindings()
    }
}
