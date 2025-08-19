package org.intelehealth.common.ui.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.intelehealth.common.databinding.RowItemFooterPagingProgressBinding
import org.intelehealth.common.model.ListItemHeaderSection
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import java.util.LinkedList

/**
 * Created by Vaghela Mithun R. on 03-06-2025 - 23:03.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
open class FooterViewMoreAdapter<I : ListItemHeaderSection>(
    context: Context, objectsList: LinkedList<I>
) : CommonHeaderSectionAdapter<I>(context, objectsList) {

    var isLoading: Boolean = false
        set(value) {
            field = value
            notifyItemChanged(itemCount - 1)
        }

    companion object {
        const val FOOTER = 2000
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isFooter()) FOOTER
        else super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == FOOTER) {
            val binding = RowItemFooterPagingProgressBinding.inflate(inflater, parent, false)
            FooterViewMoreViewHolder(binding)
        } else super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == FOOTER && holder is FooterViewMoreViewHolder && getItem(position).isFooter()) {
            viewHolderClickListener?.let { holder.setViewClickListener(it) }
            holder.bind(isLoading)
        } else super.onBindViewHolder(holder, position)
    }
}

class FooterViewMoreViewHolder(
    private val binding: RowItemFooterPagingProgressBinding
) : BaseViewHolder(binding.root) {

    fun bind(isLoading: Boolean) {
        binding.isLoading = isLoading
        binding.btnViewMore.setOnClickListener(this)
    }
}
