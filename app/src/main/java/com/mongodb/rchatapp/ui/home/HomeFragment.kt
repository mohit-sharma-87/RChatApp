package com.mongodb.rchatapp.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mongodb.rchatapp.databinding.FragmentChatRoomListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

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

        homeViewModel.isLoggedIn.observe(viewLifecycleOwner) {
            if (!it)
                openLogin()
        }

        homeViewModel.chatList.observe(viewLifecycleOwner) {

        }

        binding.fabNewChat.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.goToChatMemberList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openLogin() {
        findNavController().navigate(HomeFragmentDirections.actionGoToHome())
    }

}