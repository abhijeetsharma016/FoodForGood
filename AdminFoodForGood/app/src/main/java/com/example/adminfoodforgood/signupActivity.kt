package com.example.adminfoodforgood

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminfoodforgood.databinding.ActivitySignupBinding
import com.example.adminfoodforgood.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class signupActivity : AppCompatActivity() {
    private lateinit var userName: String
    private lateinit var nameOfRestaurant: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //initialization Firebase Auth
        auth = Firebase.auth
        //initialize Firebase Database
        database = Firebase.database.reference



        binding.createAccount.setOnClickListener {

            //get text from edit text
            userName = binding.username.text.toString().trim()
            nameOfRestaurant = binding.restorentName.text.toString().trim()
            email = binding.emailOrPhone.text.toString().trim()
            password = binding.password.text.toString().trim()

            if(userName.isBlank() || nameOfRestaurant.isBlank() || email.isBlank() || password.isBlank()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
            }
            else{
                createAccount(email, password)
            }
        }
        binding.alreadyHaveAnAccountButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        val locationList = arrayOf(
            "Jaipur",
            "Agra",
            "Delhi",
            "Mumbai",
            "Pune",
            "Chennai",
            "Kolkata",
            "Hyderabad",
            "Ahmedabad",
            "Surat"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.listOfLocations
        autoCompleteTextView.setAdapter(adapter)
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password). addOnCompleteListener(){task->
            if(task.isSuccessful){
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this, "Failed to create account", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Failure", task.exception)
            }
        }
    }
//Save data into data base
    private fun saveUserData() {
        userName = binding.username.text.toString().trim()
        nameOfRestaurant = binding.restorentName.text.toString().trim()
        email = binding.emailOrPhone.text.toString().trim()
        password = binding.password.text.toString().trim()

        val user = UserModel(userName, nameOfRestaurant, email, password)
    val userId = FirebaseAuth.getInstance().currentUser!!.uid
    //save data in firebase
    database.child("user").child(userId).setValue(user)

    }
}