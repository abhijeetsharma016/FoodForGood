package com.example.foodforgood

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.foodforgood.databinding.ActivityDetailsActiviryBinding
import com.example.foodforgood.model.cartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsActiviryBinding
    private var foodName: String? = null
    private var foodImage: String? = null
    private var foodDescription: String? = null
    private var foodIngredient: String? = null
    private var foodPrice: String? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsActiviryBinding.inflate(layoutInflater)
        setContentView(binding.root)
//Initialize auth
        auth = FirebaseAuth.getInstance()

        foodName = intent.getStringExtra("MenuItemName")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodIngredient = intent.getStringExtra("MenuItemIngredient")
        Log.d("DetailsActivity", "foodIngredient: $foodIngredient")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodImage = intent.getStringExtra("MenuItemImage")


        with(binding) {
            detailFoodName.text = foodName
            description.text = foodDescription
            ingredient.text = foodIngredient
            Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(detailFoodImage)
        }


        binding.imageButton.setOnClickListener {
            finish()
        }

        binding.addItemButton.setOnClickListener{
            addItemsToCart()
        }
    }

    private fun addItemsToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid ?: ""
        //objects for cart items
        val cartItem = cartItems(foodName.toString(), foodPrice.toString(),foodDescription.toString(), foodImage.toString(), 1)

        //save data of cart to firebase
        database.child("user").child(userId).child("cartItems").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this, "Item added to cart Successfully 😁", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener{
            Toast.makeText(this, "Failed to add item to cart 😔", Toast.LENGTH_SHORT).show()
        }
    }
}