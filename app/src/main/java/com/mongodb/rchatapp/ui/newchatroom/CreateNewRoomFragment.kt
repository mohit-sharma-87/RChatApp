package com.mongodb.rchatapp.ui.newchatroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mongodb.rchatapp.databinding.FragmentChatNewRoomBinding
import com.mongodb.rchatapp.ui.data.CreateNewChatNavigation
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A fragment representing a list of Items.
 */
class CreateNewRoomFragment : Fragment() {

    private val viewModel: ChatMemberViewModel by viewModel()
    private lateinit var binding: FragmentChatNewRoomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatNewRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMemberList.apply {
            adapter = ChatMemberListAdapter()
        }

        viewModel.loadingBar.observe(viewLifecycleOwner) {
            binding.loading.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.members.observe(viewLifecycleOwner) {
            val adapter = binding.rvMemberList.adapter as ChatMemberListAdapter
            adapter.updateMemberList(it)
        }

        binding.fabAddRoom.setOnClickListener {
            val adapter = binding.rvMemberList.adapter as ChatMemberListAdapter

            if (adapter.values.isNotEmpty()) {
                viewModel.createChatRoom(
                    roomName = binding.etRoomName.text.toString(),
                    selectedMembers = adapter.values.toList()
                )
            }
        }

        viewModel.navigation.observe(viewLifecycleOwner) {
            when (it) {
                CreateNewChatNavigation.GoToDashboard -> findNavController().navigateUp()
            }
        }
    }

}