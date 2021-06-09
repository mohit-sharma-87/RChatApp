package com.mongodb.rchatapp.ui.chatmembers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mongodb.rchatapp.databinding.FragmentMemberListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A fragment representing a list of Items.
 */
class ChatMemberListFragment : Fragment() {

    private val viewModel: ChatMemberViewModel by viewModel()
    private lateinit var binding: FragmentMemberListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMemberListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMemberList.apply {
            adapter = ChatMemberViewAdapter()
        }

        viewModel.loadingBar.observe(viewLifecycleOwner) {
            binding.loading.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.members.observe(viewLifecycleOwner) {
            val adapter = binding.rvMemberList.adapter as ChatMemberViewAdapter
            adapter.updateMemberList(it)
        }
    }

}