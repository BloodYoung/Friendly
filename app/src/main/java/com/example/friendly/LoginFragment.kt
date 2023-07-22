package com.example.friendly

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.friendly.Model.TokenManager
import com.example.friendly.databinding.FragmentHomeBinding
import com.example.friendly.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var tokenManager : TokenManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        tokenManager = TokenManager(requireContext())
        val email = requireArguments().getString("email")
        binding.loginBtn.setOnClickListener {
            if(!TextUtils.isEmpty(binding.emailLoginFragmentEt.text) && !TextUtils.isEmpty(binding.passwordLoginFragmentEt.text)){
                userLogin(email!!)
            }else{
                Toast.makeText(requireActivity(),"Please enter required details!", Toast.LENGTH_SHORT).show()
            }
        }
        firebaseAuth = FirebaseAuth.getInstance()
        binding.emailLoginFragmentEt.setText(email)
        return binding.root
    }

    //Logging in user
    private fun userLogin(email:String){
        firebaseAuth.signInWithEmailAndPassword(email,binding.passwordLoginFragmentEt.text.toString()).addOnCompleteListener {
            if (it.isSuccessful){
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                Toast.makeText(activity,"Login Successful!" ,Toast.LENGTH_SHORT).show()
                firebaseAuth.currentUser?.let { it1 -> tokenManager.saveCurrentUser(it1.uid) }
            }else{
                Toast.makeText(activity,"Invalid credentials!" ,Toast.LENGTH_SHORT).show()
            }
        }
    }

}