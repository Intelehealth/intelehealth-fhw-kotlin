package org.intelehealth.feature.chat.ui.adapter.viewholder

import org.intelehealth.common.ui.viewholder.BaseViewHolder
import org.intelehealth.feature.chat.databinding.RowMsgItemReceiverBinding
import org.intelehealth.feature.chat.room.entity.ChatMessage


/**
 * Created by Vaghela Mithun R. on 15-08-2023 - 00:18.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class ReceiverViewHolder(val binding: RowMsgItemReceiverBinding) : BaseViewHolder(binding.root) {
    fun bind(chatMessage: ChatMessage) {
        binding.chatMessage = chatMessage
    }
}