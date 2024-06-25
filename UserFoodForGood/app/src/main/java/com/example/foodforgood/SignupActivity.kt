package com.example.foodforgood

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodforgood.databinding.ActivitySignupBinding
import com.example.foodforgood.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient


    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        //Initializing firebase auth
        auth = Firebase.auth

        //Initializing firebase database
        database = Firebase.database.reference

        //Initializing google signin
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        binding.createAccountButton.setOnClickListener {
            email = binding.emailAddress.text.toString().trim()
            password = binding.password.text.toString().trim()
            userName = binding.userName.text.toString().trim()


            if (email.isBlank() || password.isBlank() || userName.isBlank()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password)
            }
        }

        binding.alreadyhaveaccountbutton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.googleSignIn.setOnClickListener{
            val SingIntent = googleSignInClient.signInIntent
            launcher.launch(SingIntent)
        }
    }


    //launcher for google singin
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result->
            if(result.resultCode == Activity.RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if(task.isSuccessful){
                    val account:GoogleSignInAccount? = task.result
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener{task->
                        if(task.isSuccessful){
                            Toast.makeText(this, "Signin Successful 😋", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        else{
                            Toast.makeText(this, "Signin failed ${task.exception.toString()}", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
                else{
                    Toast.makeText(this, "Signin failed ${task.exception.toString()}", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "SignIn Failed", Toast.LENGTH_SHORT).show()
            }
        }
            private fun createAccount(email: String, password: String) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
                        saveUserData()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to create account ", Toast.LENGTH_SHORT).show()
                        Log.d("Account", "createUserWithEmail:failure", task.exception)
                    }
                }
            }

            private fun saveUserData() {
                //retrieve data from input field
                userName = binding.userName.text.toString()
                password = binding.password.text.toString().trim()
                email = binding.emailAddress.text.toString().trim()

                val user = UserModel(userName, email, password)
                val userId = FirebaseAuth.getInstance().currentUser!!.uid

                //save data to firebasedatabase
                database.child("user").child(userId).setValue(user)
            }
        }