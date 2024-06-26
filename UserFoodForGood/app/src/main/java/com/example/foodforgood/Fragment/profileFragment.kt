package com.example.foodforgood.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.foodforgood.R
import com.example.foodforgood.databinding.FragmentProfileBinding
import com.example.foodforgood.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class profileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        setUserData()
        binding.saveInfoButton.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val phone = binding.phone.text.toString()
            val address = binding.address.text.toString()

            updateUserData(name, email, phone, address)
        }
        return binding.root

    }

    private fun updateUserData(name: String, email: String, phone: String, address: String) {
        val userid = auth.currentUser?.uid

        if (userid != null) {
            val userRef = firebaseDatabase.getReference("user").child(userid)

            val userData =hashMapOf("name" to name,
                "email" to email,
                "phone" to phone,
                "address" to address
            )
            userRef.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(), "Data Updated", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener{
                    Toast.makeText(requireContext(), "Failed to Update Data", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setUserData() {
        //set user data
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userRef = firebaseDatabase.getReference("user").child(userId)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userProfile = snapshot.getValue(UserModel::class.java)
                        if (userProfile != null) {
                            binding.name.setText(userProfile.name)
                            binding.email.setText(userProfile.email)
                            binding.phone.setText(userProfile.phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}