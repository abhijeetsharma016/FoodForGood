package com.example.adminfoodforgood

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodforgood.adapter.PendingOrderAdapter
import com.example.adminfoodforgood.databinding.ActivityPendingOrderBinding
import com.example.adminfoodforgood.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class pendingOrderActivity : AppCompatActivity(), PendingOrderAdapter.OnItemClicked {
    private lateinit var binding: ActivityPendingOrderBinding
    private var listOfName: MutableList<String> = mutableListOf()
    private var listOfQuantity: MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrder:MutableList<String> = mutableListOf()
    private var listOfOrderItems: ArrayList<OrderDetails> = arrayListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
//Initialize of database
        database = FirebaseDatabase.getInstance()
        //Initialize of databaseReference
        databaseOrderDetails = database.getReference("orderDetails")
        getOrderDetails()

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun getOrderDetails() {
        //retrieve order details from firebase database
        databaseOrderDetails.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(orderSnapShot in snapshot.children){
                    val orderDetails = orderSnapShot.getValue(OrderDetails::class.java)
                    orderDetails?.let{
                        listOfOrderItems.add(it)

                    }
                }
                addDataToListForRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    private fun addDataToListForRecyclerView() {
        for (i in 0 until listOfOrderItems.size) {
            listOfOrderItems[i].foodNames?.firstOrNull()?.let {
                listOfName.add(it)
                listOfOrderItems[i].foodQuantities?.firstOrNull()?.let {
                    listOfQuantity.add(it.toString())
                    listOfOrderItems[i].foodImages?.firstOrNull()?.let {
                        listOfImageFirstFoodOrder.add(it)
                    }

                    binding.pendingOrderRecyclerView.layoutManager = LinearLayoutManager(this)
                    val adapter = PendingOrderAdapter(this, listOfName, listOfQuantity, listOfImageFirstFoodOrder, this)
                    binding.pendingOrderRecyclerView.adapter = adapter
                }
            }
        }
    }

    override fun onItemClickLister(position: Int) {
        val intent = Intent(this, OrderDetailsActivity::class.java)
        val userOrderDetails = listOfOrderItems[position]
        intent.putExtra("userOrderDetails", userOrderDetails)
        startActivity(intent)
    }
}
