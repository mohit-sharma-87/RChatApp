package com.mongodb.rchatapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mongodb.rchatapp.databinding.ItemChatListBinding
import com.mongodb.rchatapp.ui.data.Conversation
import java.util.*

class ChatListAdapter : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    var values: MutableList<Conversation> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = values[position]
    }

    override fun getItemCount(): Int {
        return values.size
    }

    fun updateList(items: List<Conversation>) {
        values.clear()
        values.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemChatListBinding) : RecyclerView.ViewHolder(binding.root)
}

