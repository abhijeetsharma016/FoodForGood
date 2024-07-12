package com.example.adminfoodforgood

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.adminfoodforgood.databinding.ActivityMainBinding
import com.example.adminfoodforgood.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completedOrderReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Initialize Firebase database
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        // Get a reference to the "CompletedOrder" node
        completedOrderReference = database.reference.child("CompletedOrder")


        binding.addMenu.setOnClickListener {
            val intent = Intent(this, addItemActivity::class.java)
            startActivity(intent)
        }
        binding.allItemMenu.setOnClickListener {
            val intent = Intent(this, allItemActivity::class.java)
            startActivity(intent)
        }

        binding.outForDeliveryButton.setOnClickListener {
            val intent = Intent(this, outForDeliveryActivity::class.java)
            startActivity(intent)
        }

        binding.profile.setOnClickListener {
            val intent = Intent(this, adminProfileActivity::class.java)
            startActivity(intent)
        }
        binding.createNewUser.setOnClickListener {
            val intent = Intent(this, createUserActivity::class.java)
            startActivity(intent)
        }

        binding.pendingOrderTextView.setOnClickListener {
            val intent = Intent(this, pendingOrderActivity::class.java)
            startActivity(intent)
        }

        pendingOrders()

        completedOrders()

        wholeTimeEarning()
    }

    private fun wholeTimeEarning() {
        var listOfTotalPay = mutableListOf<Int>()


        completedOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (orderSnapshot in snapshot.children) {
                    var completeOrder = orderSnapshot.getValue(OrderDetails::class.java)

                    completeOrder?.totalPrice?.replace("₹", "")?.toIntOrNull()
                        ?.let { i -> listOfTotalPay.add(i) }
                }
            binding.wholeTimeEarning.text = listOfTotalPay.sum().toString() +"₹"
        }

                override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    })
}

private fun pendingOrders() {
    database = FirebaseDatabase.getInstance()
    var pendingOrderRef = database.reference.child("orderDetails")
    var pendingOrderItemCount = 0
    pendingOrderRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            pendingOrderItemCount = snapshot.childrenCount.toInt()
            binding.pendingOrders.text = pendingOrderItemCount.toString()
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    })

}

private fun completedOrders() {
    var completedOrderRef = database.reference.child("CompletedOrder")
    var completedOrderItemCount = 0
    completedOrderRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            completedOrderItemCount = snapshot.childrenCount.toInt()
            binding.completedOrders.text = completedOrderItemCount.toString()
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    })
}


}