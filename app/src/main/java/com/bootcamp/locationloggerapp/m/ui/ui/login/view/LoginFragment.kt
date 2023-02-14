package com.bootcamp.locationloggerapp.m.ui.ui.login.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bootcamp.locationloggerapp.R
import com.bootcamp.locationloggerapp.databinding.FragmentLoginBinding
import com.bootcamp.locationloggerapp.m.ui.ui.HomeActivity
import com.bootcamp.locationloggerapp.m.ui.ui.login.viewmodel.LoginViewModel
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    val viewModel: LoginViewModel by viewModels()

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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonAction()
    }

    private fun buttonAction(){
        binding.btnLoginSignin.setOnClickListener{
            val email =  binding.tvLoginEmail.text.toString()
            val password = binding.tvLoginPassword.text.toString()
            viewModel.signIn(email,password)

        }
        binding.tvLoginRegister.setOnClickListener{
            findNavController().navigate(R.id.signUpFragment)
        }
    }
    private fun observers(){
        val errorObserver = Observer<String> { errorMessage ->
            Toast.makeText(requireActivity(), "error $errorMessage", Toast.LENGTH_SHORT).show()
        }

        val onLogin = Observer<Boolean> {
            val intent = Intent(requireContext(), HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        viewModel.errorMessage.observe(this, errorObserver)
        viewModel.onLogin.observe(this, onLogin)

    }

}