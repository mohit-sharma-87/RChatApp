package com.mongodb.rchatapp.ui.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.mongodb.rchatapp.databinding.FragmentProfileBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.view.*
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.mongodb.rchatapp.R
import com.mongodb.rchatapp.ui.data.ProfileNavigation


class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModel()
    private lateinit var _binding: FragmentProfileBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigation.observe(viewLifecycleOwner) {
            when (it) {
                ProfileNavigation.GoToHome -> {
                    findNavController().navigateUp()
                }
            }
        }

        _binding.inputUsername.doAfterTextChanged {
            _binding.btUpdate.isEnabled = true
        }

        _binding.btUpdate.setOnClickListener {
            viewModel.updateDisplayName(_binding.inputUsername.text.toString())
        }

        viewModel.userName.observe(viewLifecycleOwner) {
            _binding.inputUsername.setText(it)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                viewModel.onLogout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}