package com.example.friendly

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.setMargins
import com.example.friendly.Model.TokenManager
import com.example.friendly.Model.User
import com.example.friendly.databinding.FragmentMainBinding
import com.example.friendly.utils.Const
import com.example.friendly.utils.Const.database
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        setData()
        return binding.root
    }

    private fun setData() {
        var name  = ""
        var genderAndAge = ""
        val tokenManager = TokenManager(requireContext())
        val currentUser = tokenManager.getCurrentUser()
        database.child(currentUser!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        if(it.key.equals("firstName")||it.key.equals("lastName")){
                           name = name + it.value + " "
                        }
                        if(it.key=="gender"|| it.key == "age"){
                            if (genderAndAge.isNotEmpty()){
                                genderAndAge += it.value
                            }else{
                                genderAndAge = genderAndAge + it.value + ", "
                            }
                        }
                    }
                    binding.profileNameTv.text = name
                    binding.genderAndAgeTv.text = genderAndAge
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), error.details, Toast.LENGTH_SHORT).show()
                }

            })
    }

}