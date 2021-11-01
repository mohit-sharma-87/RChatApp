package com.mongodb.rchatapp.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mongodb.rchatapp.databinding.ItemChatMessageBinding
import com.mongodb.rchatapp.ui.data.ChatMessage

class ChatMessageAdapter : RecyclerView.Adapter<ChatMessageAdapter.ViewHolder>() {

    private val messages = mutableListOf<ChatMessage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.message = messages[position]
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun setData(it: List<ChatMessage>) {
        val diffResult =
            DiffUtil.calculateDiff(
                ChatMessageListDiffCallback(
                    oldList = messages.toList(),
                    newList = it
                )
            )
        diffResult.dispatchUpdatesTo(this)

        messages.clear()
        messages.addAll(it)
    }

    inner class ViewHolder(val binding: ItemChatMessageBinding) :
        RecyclerView.ViewHolder(binding.root)
}
