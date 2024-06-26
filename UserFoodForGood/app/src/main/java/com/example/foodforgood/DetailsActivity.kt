package com.example.foodforgood

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.foodforgood.databinding.ActivityDetailsActiviryBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsActiviryBinding
    private var foodName: String? = null
    private var foodImage: String? = null
    private var foodDescription: String? = null
    private var foodIngredient: String? = null
    private var foodPrice: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsActiviryBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }
}