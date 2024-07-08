package com.example.adminfoodforgood

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.adminfoodforgood.databinding.ActivityOrderDetailsBinding
import com.example.adminfoodforgood.model.OrderDetails

class OrderDetailsActivity : AppCompatActivity() {
    private val binding: ActivityOrderDetailsBinding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }

    private var userName: String? = null
    private var Address: String?= null
    private var phoneNo: String?= null
    private var totalPrice: String? = null
    private var foodNames: MutableList<String> = mutableListOf()
    private var foodPrices: MutableList<String> = mutableListOf()
    private var foodQuantities: MutableList<Int> = mutableListOf()
    private var foodImages: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        getDataFromIntent()

    }

    private fun getDataFromIntent() {
        val receiveOrderDetail = intent.getParcelableExtra<OrderDetails>("orderDetail")
        if(receiveOrderDetail != null){
            userName = receiveOrderDetail.userName
            foodNames = receiveOrderDetail.foodNames!!
            foodPrices = receiveOrderDetail.foodPrices!!
            foodQuantities = receiveOrderDetail.foodQuantities!!
            foodImages = receiveOrderDetail.foodImages!!
            Address = receiveOrderDetail.address
            phoneNo = receiveOrderDetail.phoneNumber
            totalPrice = receiveOrderDetail.totalPrice
        }
    }
}