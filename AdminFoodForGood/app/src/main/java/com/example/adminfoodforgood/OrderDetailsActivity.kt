package com.example.adminfoodforgood

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodforgood.adapter.OrderDetailsAdapter
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
    private var foodNames: ArrayList<String> = arrayListOf()
    private var foodPrices: ArrayList<String> = arrayListOf()
    private var foodQuantities: ArrayList<Int> = arrayListOf()
    private var foodImages: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        getDataFromIntent()

    }

    private fun getDataFromIntent() {
        val receiveOrderDetail = intent.getSerializableExtra("userOrderDetails") as OrderDetails
        receiveOrderDetail?.let {orderDetails ->
                userName = receiveOrderDetail.userName
                foodNames = receiveOrderDetail.foodNames as ArrayList<String>
                foodPrices = receiveOrderDetail.foodPrices as ArrayList<String>
                foodQuantities = receiveOrderDetail.foodQuantities as ArrayList<Int>
                foodImages = receiveOrderDetail.foodImages as ArrayList<String>
                Address = receiveOrderDetail.address
                phoneNo = receiveOrderDetail.phoneNumber
                totalPrice = receiveOrderDetail.totalPrice

                setUserDetails()
                setAdapter()
            }
        }

    private fun setUserDetails() {
        binding.name.text = userName
        binding.address.text = Address
        binding.phone.text = phoneNo
        binding.totalPay.text = totalPrice
    }

    private fun setAdapter() {
        binding.orderDetailsRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = OrderDetailsAdapter(this, foodNames, foodPrices, foodQuantities, foodImages)
        binding.orderDetailsRecyclerView.adapter = adapter
    }
}