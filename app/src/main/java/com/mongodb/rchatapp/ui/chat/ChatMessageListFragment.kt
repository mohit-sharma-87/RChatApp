package com.mongodb.rchatapp.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mongodb.rchatapp.databinding.FragmentChatMessageBinding
import com.mongodb.rchatapp.utils.clear
import com.mongodb.rchatapp.utils.gone
import com.mongodb.rchatapp.utils.hideKeyboard
import com.mongodb.rchatapp.utils.visible
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ChatMessageListFragment : Fragment() {

    private val args: ChatMessageListFragmentArgs by navArgs()
    lateinit var binding: FragmentChatMessageBinding
    private val viewModel: ChatMessageViewModel by viewModel {
        parametersOf(
            args.conversationId,
            args.currentUserName
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentChatMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvChatMessage.apply {
            layoutManager = LinearLayoutManager(activity).apply {
                stackFromEnd = true
            }
            adapter = ChatMessageAdapter()
        }

        binding.rvChatMembers.apply {
            adapter = ChatMemberAdapter()
        }

        viewModel.chatMessage.observe(viewLifecycleOwner) {
            val adapter = binding.rvChatMessage.adapter as ChatMessageAdapter
            adapter.setData(it)
        }

        binding.ivSend.setOnClickListener {
            viewModel.sendChatMessage(binding.tvChatMessage.text.toString())
        }

        viewModel.messagePostStatus.observe(viewLifecycleOwner) {
            if (it) {
                binding.tvChatMessage.clear()
                getView()?.hideKeyboard()
            } else
                Toast.makeText(requireContext(), "Couldn't send message", Toast.LENGTH_SHORT).show()
        }

        viewModel.loadingBar.observe(viewLifecycleOwner) {
            if (it)
                binding.loading.visible()
            else
                binding.loading.gone()
        }

        viewModel.conversation.observe(viewLifecycleOwner) {
            (binding.rvChatMembers.adapter as ChatMemberAdapter).setMemberList(it.members)
        }

    }


}