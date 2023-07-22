package com.example.friendly

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.ActivityChooserView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.friendly.Model.TokenManager
import com.example.friendly.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding : FragmentHomeBinding
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        tokenManager = TokenManager(requireActivity())
        loggedInOrNot()
        binding.emailVerifyBtn.setOnClickListener {
            userLoginOrSignup(it)
        }
        firebaseAuth = FirebaseAuth.getInstance()
        return binding.root
    }

    //After entering the email at home page, checking here if email is already registered
    private fun userLoginOrSignup(view: View){
        if(!TextUtils.isEmpty(binding.emailEt.text)) {
            val bundle = bundleOf("email" to binding.emailEt.text.toString())
            firebaseAuth.fetchSignInMethodsForEmail(binding.emailEt.text.toString())
                .addOnSuccessListener {
                    if (it.signInMethods?.isEmpty() == true) {
                        view.findNavController()
                            .navigate(R.id.action_homeFragment_to_registrationFragment, bundle)
                    } else {
                        view.findNavController()
                            .navigate(R.id.action_homeFragment_to_loginFragment, bundle)
                    }
                }
        }else{
            Toast.makeText(activity,"Please enter email!", Toast.LENGTH_SHORT).show()
        }

    }

    //Checking if a user is signed in to the device
    private fun loggedInOrNot(){
        val currentUser = tokenManager.getCurrentUser()
        if (currentUser!=null){
            findNavController().navigate(R.id.mainFragment)
        }
    }

}