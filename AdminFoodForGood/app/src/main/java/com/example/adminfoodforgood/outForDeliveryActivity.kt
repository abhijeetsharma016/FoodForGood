package com.example.adminfoodforgood

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodforgood.adapter.DeliveryAdapter
import com.example.adminfoodforgood.databinding.ActivityOutForDeliveryBinding
import com.example.adminfoodforgood.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class outForDeliveryActivity : AppCompatActivity() {
    private val binding: ActivityOutForDeliveryBinding by lazy { ActivityOutForDeliveryBinding.inflate(layoutInflater) }
    private lateinit var database: FirebaseDatabase
    private var listOfCompleteOrders: ArrayList<OrderDetails> = arrayListOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.backButton.setOnClickListener{
            finish()
        }
        //retrieve and display complete orders
        retrieveCompleteOrdersDetails()


    }

    private fun retrieveCompleteOrdersDetails() {
        //initialize database
        database = FirebaseDatabase.getInstance()
        val completeOrdersRef = database.getReference("CompletedOrder").orderByChild("currentTime")
        completeOrdersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear the list before adding new items
                listOfCompleteOrders.clear()


                for (orderSnapShot in snapshot.children) {
                    val orderDetails = orderSnapShot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        listOfCompleteOrders.add(it)
                    }
                }
                //reverse the list to display the most recent order first
                listOfCompleteOrders.reverse()

                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setDataIntoRecyclerView() {
        //initialize list to hold customer name and payment status
        val customerName = mutableListOf<String>()
        val moneyStatus = mutableListOf<Boolean>()

        for(order in listOfCompleteOrders){
            order.userName?.let {
                customerName.add(it)
            }
            moneyStatus.add(order.paymentReceived)

            val adapter = DeliveryAdapter(customerName, moneyStatus)
            binding.deliveryRecyclerView.adapter = adapter
            binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this)
        }
    }
}