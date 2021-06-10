package com.mongodb.rchatapp.ui.chatmembers

import androidx.recyclerview.widget.DiffUtil
import com.mongodb.rchatapp.ui.data.ChatsterListViewModel

class ChatMemberListDiffCallback(
    private val oldList: List<ChatsterListViewModel>,
    private val newList: List<ChatsterListViewModel>
) : DiffUtil.Callback() {

    val ITEM_SELECTION_UPDATED = Any()

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]._id === newList[newItemPosition]._id
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return oldList[oldPosition].isSelected == newList[newPosition].isSelected
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem.isSelected == newItem.isSelected) ITEM_SELECTION_UPDATED else null
    }

}