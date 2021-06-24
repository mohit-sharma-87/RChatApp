package com.mongodb.rchatapp.ui.chat

import androidx.recyclerview.widget.DiffUtil
import com.mongodb.rchatapp.ui.data.ChatMessage
import com.mongodb.rchatapp.ui.data.ChatsterListViewModel

class ChatMessageListDiffCallback(
    private val oldList: List<ChatMessage>,
    private val newList: List<ChatMessage>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]._id === newList[newItemPosition]._id
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return oldList[oldPosition].text == newList[newPosition].text
    }
}