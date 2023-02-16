package com.bootcamp.locationloggerapp.m.ui.ui.profile.editprofile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bootcamp.locationloggerapp.R
import com.bootcamp.locationloggerapp.databinding.FragmentEditProfileBinding
import com.bootcamp.locationloggerapp.m.ui.utils.Constants
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditProfileViewModel by viewModels()
    private var userName: String = ""
    private var profilePhotoUrl: String = ""
    private var profilePhotoPath: String = ""
    private var backgroundPhotoUrl: String = ""
    private var backgroundPhotoPath: String = ""
    private var selectedPhotoProfile: Uri? = null
    private var userId: String = ""
    private val profilePhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                selectedPhotoProfile = it.data?.data!!
                binding.ivProfilePicture.setImageURI(selectedPhotoProfile)
            }
        }
    private var selectedCoverPhoto: Uri? = null
    private val coverPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                selectedCoverPhoto = it.data?.data!!
                binding.ivCoverPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.ivCoverPhoto.setImageURI(selectedCoverPhoto)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args()
        initButton()
        observers()

    }

    private fun observers() {
        val errorObserver = Observer<String> { errorMessage ->
            binding.progressBar.visibility = View.GONE

            Toast.makeText(requireActivity(), "error $errorMessage", Toast.LENGTH_SHORT).show()
        }

        val onUploadedPhotos = Observer<Boolean> {
            binding.progressBar.visibility = View.GONE
            findNavController().navigate(R.id.profileFragment)
        }
        viewModel.errorMessage.observe(requireActivity(), errorObserver)
        viewModel.onUpdated.observe(requireActivity(), onUploadedPhotos)
    }

    private fun initButton() {
        binding.btnSave.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val name = binding.tvUsername.text.toString()
            viewModel.deletePhotos(
                userId,
                name,
                profilePhotoUrl,
                profilePhotoPath,
                backgroundPhotoPath,
                backgroundPhotoUrl,
                selectedPhotoProfile,
                selectedCoverPhoto
            )
        }
        binding.ivProfilePicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            profilePhoto.launch(intent)
        }
        binding.ivCoverPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            coverPhoto.launch(intent)
        }
    }

    private fun args() {
        val args = this.arguments
        val userIdArgs = args?.get(Constants.userId)
        val userNameArgs = args?.get(Constants.userName)
        val profilePhotoUrlArgs = args?.get(Constants.profilePhotoUrl)
        val profilePhotoPathArgs = args?.get(Constants.profilePhotoPath)
        val backgroundPhotoUrlArgs = args?.get(Constants.backgroundPhotoUrl)
        val backgroundPhotoPathArgs = args?.get(Constants.backgroundPhotoPath)
        if (userNameArgs != null) {
            userId = userIdArgs as String
            userName = userNameArgs as String
            profilePhotoUrl = profilePhotoUrlArgs as String
            profilePhotoPath = profilePhotoPathArgs as String
            backgroundPhotoUrl = backgroundPhotoUrlArgs as String
            backgroundPhotoPath = backgroundPhotoPathArgs as String
            setInfo()
        }
    }

    private fun setInfo() {
        binding.tvUsername.setText(userName)
        Picasso.get()
            .load(profilePhotoUrl)
            .into(binding.ivProfilePicture)
        if (backgroundPhotoUrl != "") {
            binding.ivCoverPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
            Picasso.get()
                .load(backgroundPhotoUrl)
                .into(binding.ivCoverPhoto)
        }
    }
}