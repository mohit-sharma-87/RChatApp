package com.mongodb.rchatapp.ui.home

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mongodb.rchatapp.databinding.FragmentChatRoomListBinding
import com.mongodb.rchatapp.ui.data.HomeNavigation
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"
    private val homeViewModel: HomeViewModel by viewModel()
    private var _binding: FragmentChatRoomListBinding? = null

    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycle.addObserver(homeViewModel)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatRoomListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvChatList.apply {
            adapter = ChatListAdapter().addClickListener {
                homeViewModel.onRoomClick(it)
            }
        }

        homeViewModel.chatList.observe(viewLifecycleOwner) {

            if (it.isEmpty()) {
                showEmptyScreen()
            } else
                hideEmptyScreen()

            val adapter = binding.rvChatList.adapter as ChatListAdapter
            adapter.updateList(it)
        }

        homeViewModel.navigation.observe(viewLifecycleOwner) {
            when (it) {
                is HomeNavigation.GoToSelectedRoom -> {
                    findNavController().navigate(
                        HomeFragmentDirections.goToChatMessage(
                            it.conversationId,
                            it.roomName
                        )
                    )
                }
                is HomeNavigation.GoToProfile -> {
                    findNavController().navigate(HomeFragmentDirections.goToProfile())
                }
                is HomeNavigation.GoToLogin -> {
                    findNavController().navigate(HomeFragmentDirections.goToLogin())
                }
                else -> {
                    Log.e(TAG, "onViewCreated: navigation $it")
                }
            }
        }

        binding.fabNewChat.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.goToChatMemberList())
        }

        homeViewModel.loadingBar.observe(viewLifecycleOwner) {
            binding.loading.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideEmptyScreen() {
        binding.tvEmptyScreen.visibility = View.GONE
    }

    private fun showEmptyScreen() {
        binding.tvEmptyScreen.visibility = View.VISIBLE
    }
}