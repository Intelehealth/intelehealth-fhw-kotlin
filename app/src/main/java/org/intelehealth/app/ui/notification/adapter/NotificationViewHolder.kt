package org.intelehealth.app.ui.notification.adapter

import org.intelehealth.app.databinding.NotificationListItemBinding
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import org.intelehealth.data.offline.entity.NotificationList

class NotificationViewHolder(private val binding: NotificationListItemBinding) : BaseViewHolder(binding.root) {
    override var allowInstantClick = true
    fun bind(localNotification: NotificationList) {
        binding.notification = localNotification
        binding.cardPatientItem.tag = localNotification
        binding.cardPatientItem.setOnClickListener(this)
        binding.executePendingBindings()
    }
}
