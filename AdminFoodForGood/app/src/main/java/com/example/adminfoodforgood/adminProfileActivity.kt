package com.example.adminfoodforgood

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adminfoodforgood.databinding.ActivityAdminProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class adminProfileActivity : AppCompatActivity() {
    private val binding: ActivityAdminProfileBinding by lazy { ActivityAdminProfileBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminReference = database.reference.child("user")

        binding.saveInfoButton.setOnClickListener{
            setUserData()
        }


        binding.backButton.setOnClickListener{
            finish()
        }
        binding.name.isEnabled = false
        binding.address.isEnabled = false
        binding.phone.isEnabled = false
        binding.email.isEnabled = false
        binding.password.isEnabled = false
        binding.saveInfoButton.isEnabled = false

        var isEnable = false
        binding.editButton.setOnClickListener {
            isEnable = !isEnable
            binding.name.isEnabled = isEnable
            binding.address.isEnabled = isEnable
            binding.phone.isEnabled = isEnable
            binding.email.isEnabled = isEnable
            binding.password.isEnabled = isEnable
            binding.saveInfoButton.isEnabled = true

            if (isEnable) {
                binding.name.requestFocus()
            }
        }

        retrieveUserData()
    }

        private fun setUserData() {
            val userUid = auth.currentUser?.uid
            if (userUid!= null) {
                val userRef = adminReference.child(userUid)
                val ownerName = binding.name.text.toString()
                val ownerAddress = binding.address.text.toString()
                val ownerPhone = binding.phone.text.toString()
                val ownerEmail = binding.email.text.toString()
                val ownerPassword = binding.password.text.toString()

                val userData = hashMapOf<String, Any>(
                    "name" to ownerName,
                    "address" to ownerAddress,
                    "phone" to ownerPhone,
                    "email" to ownerEmail,
                    "password" to ownerPassword
                )

                userRef.updateChildren(userData).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "User data updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to update user data", Toast.LENGTH_SHORT).show()
                        Log.d("UserData", "Error updating user data: ${task.exception?.message}")
                    }
                }
            }
        }


    private fun retrieveUserData() {
        val userUid = auth.currentUser?.uid
        Log.d("UserUid", userUid?: "UserUid is null")
        if(userUid != null){
                 val userRef = adminReference.child(userUid)
                userRef.addListenerForSingleValueEvent(object : ValueEventListener {


                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()) {
                            Log.d("FirebaseData", snapshot.value.toString())
                            var ownerName = snapshot.child("name").getValue(String::class.java)
                            var ownerAddress = snapshot.child("address").getValue(String::class.java)
                            var ownerPhone = snapshot.child("phone").getValue(String::class.java)
                            var ownerEmail = snapshot.child("email").getValue(String::class.java)
                            var ownerPassword = snapshot.child("password").getValue(String::class.java)
                            setDataToTextView(ownerName, ownerAddress, ownerPhone, ownerEmail, ownerPassword)
                        } else {
                            Log.d("FirebaseData", "No data exists")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }

    }

    private fun setDataToTextView(
        ownerName: String?,
        ownerAddress: String?,
        ownerPhone: String?,
        ownerEmail: String?,
        ownerPassword: String?
    ) {
        binding.name.setText(ownerName)
        binding.address.setText(ownerAddress)
        binding.phone.setText(ownerPhone)
        binding.email.setText(ownerEmail)
        binding.password.setText(ownerPassword)
    }
}