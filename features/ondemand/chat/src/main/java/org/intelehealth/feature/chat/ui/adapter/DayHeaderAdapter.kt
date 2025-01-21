package org.intelehealth.feature.chat.ui.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.intelehealth.common.ui.adapter.BaseRecyclerViewAdapter
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import org.intelehealth.feature.chat.databinding.ItemRowDateHeaderBinding
import org.intelehealth.feature.chat.model.DayHeader
import org.intelehealth.feature.chat.model.ItemHeader

/**
 * Created by Vaghela Mithun R. on 03-08-2023 - 18:45.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 */
abstract class DayHeaderAdapter(context: Context, list: MutableList<ItemHeader>) :
    BaseRecyclerViewAdapter<ItemHeader>(context, list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATE_HEADER) {
            val binding = ItemRowDateHeaderBinding.inflate(inflater, parent, false)
            DayHeaderHolder(binding)
        } else createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATE_HEADER && holder is DayHeaderHolder) {
            val header = getItem(position) as DayHeader
            holder.bind(header.displayFormat())
        }
    }

    companion object {
        const val DATE_HEADER = 1000
    }
}

internal class DayHeaderHolder(private val binding: ItemRowDateHeaderBinding) :
    BaseViewHolder(binding.root) {
    fun bind(date: String?) {
        binding.dateString = date
    }
}