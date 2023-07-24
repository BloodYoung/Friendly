package com.example.friendly

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.friendly.Model.TokenManager
import com.example.friendly.databinding.FragmentMainBinding
import com.example.friendly.utils.Const.FIREBASE_DATABASE_ADDRESS
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding
    private lateinit var imageUri: Uri
    lateinit var tokenManager: TokenManager
    lateinit var currentUser: String
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var firebaseStorage: FirebaseStorage
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_DATABASE_ADDRESS)
        firebaseStorage = FirebaseStorage.getInstance()
        tokenManager = TokenManager(requireContext())
        currentUser = tokenManager.getCurrentUser()!!
        setData()
        setImages()
        binding.profilePictureImageView.setOnClickListener {
            pickImage()
        }

        return binding.root
    }

    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            if (data.data != null) {
                imageUri = data.data!!
            }
        }
        saveDataToFirebase()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun saveDataToFirebase() {
        firebaseStorage.getReference(currentUser).child("profilePicture").putFile(imageUri)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(requireContext(), "Image uploaded", Toast.LENGTH_SHORT).show()
                    setImages()
                }
            }
    }

    private fun setData() {
        var name = ""
        var genderAndAge = ""

        //Setting texts on UI
        Log.i("MYTAG", currentUser)
        firebaseDatabase.getReference("Users").child(currentUser!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        if (it.key.equals("firstName") || it.key.equals("lastName")) {
                            name = name + it.value + " "
                        }
                        if (it.key == "gender" || it.key == "age") {
                            if (genderAndAge.isNotEmpty()) {
                                genderAndAge += it.value
                            } else {
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

    private fun setImages() {
        var profilePictureUri: Uri?
        val fstore = FirebaseStorage.getInstance().getReference(currentUser)
        Log.i("MYTAGFSTORE", currentUser)
        fstore.child("profilePicture").downloadUrl.addOnCompleteListener {
            if (it.isSuccessful) {
                profilePictureUri = it.result
                Glide.with(this).load(profilePictureUri).into(binding.profilePictureImageView)
            }
        }
    }
}