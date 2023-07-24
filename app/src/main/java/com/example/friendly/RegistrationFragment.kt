package com.example.friendly

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.friendly.Model.TokenManager
import com.example.friendly.Model.User
import com.example.friendly.databinding.FragmentRegistrationBinding
import com.example.friendly.utils.Const
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistrationFragment : Fragment() {
    lateinit var binding: FragmentRegistrationBinding
    lateinit var tokenManager: TokenManager
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        firebaseDatabase = FirebaseDatabase.getInstance(Const.FIREBASE_DATABASE_ADDRESS)
        firebaseAuth = FirebaseAuth.getInstance()
        tokenManager = TokenManager(requireContext())
        val email = requireArguments().getString("email")
        if (email!!.isNotEmpty()){
            binding.emailRegistrationEt.isClickable = false
            binding.emailRegistrationEt.isFocusable = false

        }
        binding.emailRegistrationEt.setText(email)

        binding.signUpSubmitBtn.setOnClickListener {
            signUp()
        }
        return binding.root
    }

    //Signing up the user
    private fun signUp() {
        binding.apply {
            if (isDataEmpty(firstNameEt) && isDataEmpty(firstNameEt) && isDataEmpty(
                    emailRegistrationEt
                )
                && isDataEmpty(ageEt) && isDataEmpty(passwordEt) && isDataEmpty(confirmPasswordEt)  && isDataEmpty(genderEt)
            ) {
                if (passwordEt.text.toString() == confirmPasswordEt.text.toString()) {
                    firebaseAuth.createUserWithEmailAndPassword(
                        emailRegistrationEt.text.toString(),
                        passwordEt.text.toString()
                    ).addOnCompleteListener { it ->
                        if (it.isSuccessful) {
                            saveData()
                            findNavController().navigate(R.id.action_registrationFragment_to_mainFragment)
                            firebaseAuth.currentUser?.let { it1 -> tokenManager.saveCurrentUser(it1.uid) }
                        }
                    }
                } else {
                    Toast.makeText(activity, "Passwords don't match!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, "Enter all the fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Checking if any registration field is empty
    private fun isDataEmpty(editText: EditText): Boolean {
        return !TextUtils.isEmpty(editText.text)
    }

    //Saving data to database
    private fun saveData() {
        binding.apply {
            val user = User(
                firstNameEt.text.toString(),
                lastNameEt.text.toString(),
                emailRegistrationEt.text.toString(),
                genderEt.text.toString(),
                ageEt.text.toString().toInt()
            )
            firebaseDatabase.getReference("Users").child(firebaseAuth.currentUser!!.uid).setValue(user).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(requireContext(),"Registration Successful!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}