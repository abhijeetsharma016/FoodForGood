package com.example.adminfoodforgood

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodforgood.adapter.PendingOrderAdapter
import com.example.adminfoodforgood.databinding.ActivityPendingOrderBinding

class pendingOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPendingOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener{
            finish()
        }
        val OrderCustomerName = arrayListOf("John Doe", "Jane Smith", "Michael Johnson")
        val quantity = arrayListOf("8", "6", "5")
        val orderFoodImage = arrayListOf(R.drawable.menu1, R.drawable.menu2, R.drawable.menu3)


        val adapter = PendingOrderAdapter(OrderCustomerName, quantity, orderFoodImage, context = this)
        binding.pendingOrderRecyclerView.adapter = adapter
        binding.pendingOrderRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}