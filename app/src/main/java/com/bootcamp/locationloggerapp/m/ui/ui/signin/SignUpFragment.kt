package com.bootcamp.locationloggerapp.m.ui.ui.signin

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bootcamp.locationloggerapp.R
import com.bootcamp.locationloggerapp.databinding.FragmentSignUpBinding
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignUpViewModel by viewModels()
    lateinit var selectedPhoto: Uri
    private val openGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                selectedPhoto = it.data?.data!!
                binding.ibSelectPhoto.setBackgroundColor(binding.root.context.getColor(R.color.white))
                binding.ibSelectPhoto.setImageURI(selectedPhoto)
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        FirebaseApp.initializeApp(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonAction()
    }

    private fun buttonAction() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        binding.ibSelectPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            openGallery.launch(intent)
        }

        binding.btnSignUp.setOnClickListener {
            val username = binding.tvRegisterUsername.text.toString()
            val email = binding.tvRegisterEmail.text.toString()
            val password = binding.tvRegisterPassword.text.toString()
            val confirmPassword = binding.tvRegisterConfirmPassword.text.toString()
            if (password != confirmPassword) {
                Toast.makeText(
                    requireContext(),
                    binding.root.resources.getString(R.string.password_no_match),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.registerUserAuth(username, email, password, selectedPhoto)
            }
        }

    }

    private fun observers() {
        val errorObserver = Observer<String> { errorMessage ->
            Toast.makeText(requireActivity(), "error $errorMessage", Toast.LENGTH_SHORT).show()
        }

        val onCreatedObserver = Observer<Boolean> {
            findNavController().navigate(R.id.loginFragment)
        }
        viewModel.errorMessage.observe(this, errorObserver)
        viewModel.onUserCreated.observe(this, onCreatedObserver)

    }
}