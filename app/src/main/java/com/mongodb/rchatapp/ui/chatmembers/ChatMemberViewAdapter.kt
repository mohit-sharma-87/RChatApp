package com.mongodb.rchatapp.ui.chatmembers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mongodb.rchatapp.databinding.ItemMemberBinding
import com.mongodb.rchatapp.ui.data.model.Chatster

class ChatMemberViewAdapter : RecyclerView.Adapter<ChatMemberViewAdapter.ViewHolder>() {

    private var values: List<Chatster> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMemberBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.name.text = if (item.displayName.isNullOrEmpty()) {
            item.userName
        } else {
            item.displayName
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: ItemMemberBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.icMemberName
    }

    fun updateMemberList(items: List<Chatster>) {
        values = items
        notifyDataSetChanged()
        //TODO : Implement diff call back
    }

}