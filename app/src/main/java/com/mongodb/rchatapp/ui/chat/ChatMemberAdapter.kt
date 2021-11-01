package com.mongodb.rchatapp.ui.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mongodb.rchatapp.databinding.ItemChatMemberBinding
import com.mongodb.rchatapp.ui.data.Member

class ChatMemberAdapter :
    RecyclerView.Adapter<ChatMemberAdapter.ViewHolder>() {

    lateinit var context: Context
    private var members: List<Member> = mutableListOf()


    override
    fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatMemberAdapter.ViewHolder {
        context = parent.context

        return ViewHolder(
            ItemChatMemberBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatMemberAdapter.ViewHolder, position: Int) {
        val members = members[position]
        holder.binding.chip.text = members.userName
    }

    override fun getItemCount(): Int {
        return members.size
    }


    fun setMemberList(members: List<Member>) {
        this.members = members
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemChatMemberBinding) :
        RecyclerView.ViewHolder(binding.root)

}