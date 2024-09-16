package com.example.mystore.fragments.loginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mystore.R
import com.example.mystore.data.User
import com.example.mystore.databinding.FragmentRegisterBinding
import com.example.mystore.util.RegisterValidation
import com.example.mystore.util.Resource
import com.example.mystore.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment: Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnRegisterRegister.setOnClickListener {
                val user = User(
                    edFirstName.text.toString().trim(),
                    edLastName.text.toString().trim(),
                    edEmailRegister.text.toString().trim()
                )

                val password = edPasswordRegister.text.toString()
                viewModel.createAccountWithEmailAndPassword(user, password)

            }
        }


        lifecycleScope.launch {
            viewModel.register.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {

                        Toast.makeText(context, "Carregando...", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {

                        Toast.makeText(context, "Registro bem-sucedido!", Toast.LENGTH_SHORT).show()
                        Log.d("test", resource.data.toString())

                    }
                    is Resource.Error -> {

                        Toast.makeText(context, "Erro: ${resource.message}", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, resource.message.toString())
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect{ validation ->
                if (validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.edEmailRegister.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }

                if (validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.edPasswordRegister.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                }
            }
        }

    }
    }
}