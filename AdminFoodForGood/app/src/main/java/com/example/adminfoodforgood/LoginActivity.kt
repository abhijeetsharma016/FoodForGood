package com.example.adminfoodforgood

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminfoodforgood.databinding.ActivityLoginBinding
import com.example.adminfoodforgood.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {
    private var userName: String? = null
    private var nameOfRestaurant: String? = null
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database:DatabaseReference


    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //initializing firebase auth
        auth = Firebase.auth
        //initializing firebase database
        database = Firebase.database.reference


        binding.loginButton.setOnClickListener{
            //getting data from edit texts
            email = binding.loginEmail.text.toString().trim()
            password = binding.loginPassword.text.toString().trim()

            if(email.isBlank() || password.isBlank()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
            else{
                createUserAccount(email, password)
            }
        }
        binding.dontHaveAnAccountButton.setOnClickListener{
            val intent = Intent(this, signupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun createUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                updateUI(user)
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(this, "Created user account and logged in successfully", Toast.LENGTH_LONG).show()
                        saveUserData()
                        updateUI(user)
                    } else {
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        Log.d("Account", "createUserWithEmail:failure", task.exception)
                    }
                }
            }
        }
    }

    private fun saveUserData() {
        email = binding.loginEmail.text.toString().trim()
        password = binding.loginPassword.text.toString().trim()
        val user = UserModel(userName, nameOfRestaurant, email =email, password = password)
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            database.child("users").child(it).setValue(user)
        }
    }

    fun updateUI(user: FirebaseUser?) {
        if(user != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}