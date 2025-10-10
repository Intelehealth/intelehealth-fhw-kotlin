package org.intelehealth.common.ui.binding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.github.ajalt.timberkt.Timber
import org.intelehealth.common.extensions.setupHorizontalLinearView
import org.intelehealth.common.extensions.setupLinearView
import org.intelehealth.common.model.ListBindItem

/**
 * Created by Vaghela Mithun R. on 10-07-2025 - 15:26.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@BindingAdapter(value = ["itemsList", "viewProvider"], requireAll = false)
fun bindRecyclerView(recyclerView: RecyclerView, itemsList: List<ListBindItem>?, layoutId: Int) {
    Timber.d { "Recycler Binding item size => ${itemsList?.size}" }
    Timber.d { "Recycler Binding layoutId => $layoutId" }
    object : RecyclerView.Adapter<ItemViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, parent, false)
            return ItemViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.binding.setVariable(BR.recentItem, itemsList?.get(position))
            holder.binding.executePendingBindings()
        }

        override fun getItemCount(): Int = itemsList?.size ?: 0
    }.apply {
        recyclerView.setupLinearView(adapter = this, hasItemDecoration = true)
    }

}

class ItemViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
