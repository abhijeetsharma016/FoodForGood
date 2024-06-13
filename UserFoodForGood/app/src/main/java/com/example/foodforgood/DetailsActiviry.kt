package com.example.foodforgood

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodforgood.databinding.ActivityDetailsActiviryBinding

class DetailsActiviry : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsActiviryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsActiviryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val foodName = intent.getStringExtra("MenuItemName")
        val foodImage = intent.getIntExtra("MenuItemImage", 0)
        binding.DetailFoodName.text = foodName
        binding.DetailFoodImage.setImageResource(foodImage)

        binding.imageButton.setOnClickListener {
            finish()
        }
    }
}