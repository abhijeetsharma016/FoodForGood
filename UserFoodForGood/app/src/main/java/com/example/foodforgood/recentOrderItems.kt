package com.example.foodforgood

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodforgood.adapter.RecentBuyAdapter
import com.example.foodforgood.databinding.ActivityRecentOrderItemsBinding
import com.example.foodforgood.model.OrderDetails

class recentOrderItems : AppCompatActivity() {
    private val binding: ActivityRecentOrderItemsBinding by lazy {
        ActivityRecentOrderItemsBinding.inflate(layoutInflater)
    }
    private lateinit var allFoodName: ArrayList<String>
    private lateinit var allFoodImages: ArrayList<String>
    private lateinit var allFoodPrice: ArrayList<String>
    private lateinit var allFoodQuantity: ArrayList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener{
            finish()
        }

        val recentOrderItems = intent.getSerializableExtra("recentItem") as ArrayList<OrderDetails>
        recentOrderItems?.let { orderDetails ->
            if (orderDetails.isNotEmpty()) {
                val recentOrderItem = orderDetails[orderDetails.size -1]
                allFoodName = recentOrderItem.foodNames as ArrayList<String>
                allFoodImages = recentOrderItem.foodImages as ArrayList<String>
                allFoodPrice = recentOrderItem.foodPrices as ArrayList<String>
                allFoodQuantity = recentOrderItem.foodQuantities as ArrayList<Int>
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        val rvAdapter = binding.recyclerViewRecentBuy
        rvAdapter.layoutManager = LinearLayoutManager(this)
        val adapter = RecentBuyAdapter(this, allFoodName, allFoodImages, allFoodPrice, allFoodQuantity)
        rvAdapter.adapter = adapter
    }
}