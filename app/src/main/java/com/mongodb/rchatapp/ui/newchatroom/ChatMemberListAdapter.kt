package com.mongodb.rchatapp.ui.newchatroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.mongodb.rchatapp.R
import com.mongodb.rchatapp.databinding.ItemMemberBinding
import com.mongodb.rchatapp.ui.data.ChatsterListViewModel
import java.util.ArrayList

class ChatMemberListAdapter : RecyclerView.Adapter<ChatMemberListAdapter.ViewHolder>() {

    var values: MutableList<ChatsterListViewModel> = ArrayList()

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

        holder.binding.icMemberName.text = if (item.displayName.isNullOrEmpty()) {
            item.userName
        } else {
            item.displayName
        }
        updateSelectedState(holder.binding.ivCheck, item.isSelected)

        Glide
            .with(holder.binding.icMemberIcon)
            .load(item.avatarImage?.picture)
            .placeholder(R.drawable.ic_baseline_group_chat)
            .circleCrop()
            .into(holder.binding.icMemberIcon)

        holder.binding.root.apply {
            setOnClickListener {
                val updatedItem = item.copy(isSelected = !item.isSelected)
                val updatedList = values.toMutableList().apply {
                        this[position] = updatedItem
                    }
                updateMemberList(updatedList)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            updateSelectedState(holder.binding.ivCheck, values[position].isSelected)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val binding: ItemMemberBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateMemberList(items: List<ChatsterListViewModel>) {

        val diffResult =
            DiffUtil.calculateDiff(ChatMemberListDiffCallback(oldList = values, newList = items))
        diffResult.dispatchUpdatesTo(this)

        values.clear()
        values.addAll(items)
    }

    private fun updateSelectedState(view: View, isSelected: Boolean) {
        view.visibility = if (isSelected) View.VISIBLE else View.GONE
    }
}