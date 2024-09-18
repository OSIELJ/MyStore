package com.example.mystore.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mystore.R
import com.example.mystore.activities.ShoppingActivity
import com.example.mystore.databinding.FragmentLoginBinding
import com.example.mystore.util.Resource
import com.example.mystore.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDontYouHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.apply {
            btnLoginLogin.setOnClickListener {
                val email = edEmailLogin.text.toString().trim()
                val password = edPasswordLogin.text.toString()
                viewModel.login(email, password)
            }
        }

       lifecycleScope.launchWhenStarted {
           viewModel.login.collect{
               when(it){
                   is Resource.Loading -> {

                       Toast.makeText(context, "Carregando...", Toast.LENGTH_SHORT).show()
                   }
                   is Resource.Success -> {
                       Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                           startActivity(intent)
                       }

                   }
                   is Resource.Error -> {
                       Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                   }
               }
           }
       }
    }
}