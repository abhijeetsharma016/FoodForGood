package com.example.foodforgood

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodforgood.databinding.ActivityLoginBinding
import com.example.foodforgood.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private var userName: String? = null
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignIn: GoogleSignInClient


    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        //Initialize Firebase Auth
        auth = Firebase.auth

        // Initialize Firebase Database
        database = Firebase.database.reference

        //Initialize Google Sign In
        googleSignIn = GoogleSignIn.getClient(this, googleSignInOptions)

        //Login With email and password
        binding.loginButton.setOnClickListener{

            //get Data from text fields
            email = binding.loginEmailAddress.text.toString().trim()
            password = binding.loginPassword.text.toString().trim()

            if(email.isBlank() || password.isBlank()){
                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show()
            }
            else{
                createUser()
            }
        }
        binding.donthaveaccountbutton.setOnClickListener{
            val intent = Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }

    }

    private fun createUser(){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{task->
            if(task.isSuccessful){
             val user =  auth.currentUser
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                updateUI(user)
            }else{
                Log.d("Account", "createUserWithEmail:failure", task.exception)
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{task->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
                        saveUserData()
                        val user =  auth.currentUser
                        updateUI(user)
                    }
                    else{
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                        Log.d("Account", "createUserWithEmail:failure", task.exception)
                    }
                }
            }
        }
    }

    private fun saveUserData() {
        //get Data from text fields
        email = binding.loginEmailAddress.text.toString().trim()
        password = binding.loginPassword.text.toString().trim()
        userName
        val user = UserModel(userName, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        //save user data to database
        database.child("users").child(userId).setValue(user)
    }

    private fun updateUI(user: FirebaseUser?) {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}