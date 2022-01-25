package com.mongodb.rchatapp.ui.profile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mongodb.rchatapp.R
import com.mongodb.rchatapp.databinding.FragmentProfileBinding
import com.mongodb.rchatapp.ui.data.ProfileNavigation
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModel()
    private lateinit var _binding: FragmentProfileBinding
    private var imageUri: Uri? = null


    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        Glide
            .with(this)
            .load(uri)
            .circleCrop()
            .into(_binding.ivImage)

        imageUri = uri
    }

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

            imageUri?.let {
                val byteArray = requireContext().contentResolver?.openInputStream(it)?.buffered()
                    ?.use { it.readBytes() }

                byteArray?.let {
                    viewModel.updateImage(it)
                }
            }

            viewModel.updateDisplayName(_binding.inputUsername.text.toString())
        }

        viewModel.userName.observe(viewLifecycleOwner) {
            _binding.inputUsername.setText(it)
        }

        _binding.ivImage.setOnClickListener {
            getContent.launch("image/*")
        }

        viewModel.userImage.observe(viewLifecycleOwner) {
            Glide
                .with(requireContext())
                .load(it)
                .circleCrop()
                .placeholder(R.drawable.ic_baseline_add_a_photo_24)
                .error(R.drawable.ic_baseline_add_a_photo_24)
                .into(_binding.ivImage)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                viewModel.updateUserStatusToOffline()
                viewModel.onLogout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}