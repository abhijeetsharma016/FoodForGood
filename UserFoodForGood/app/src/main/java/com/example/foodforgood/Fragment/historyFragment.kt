package com.example.foodforgood.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodforgood.adapter.BuyAgainAdapter
import com.example.foodforgood.databinding.FragmentHistoryBinding
import com.example.foodforgood.model.OrderDetails
import com.example.foodforgood.recentOrderItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.Serializable

class historyFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOrderItems: ArrayList<OrderDetails> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Retrieve and display data from firebase
        restiveBuyHistory()


        //button to open recent buy activity(when clicked on the recent buy item in history)
        binding.recentBuyItem.setOnClickListener {
            seeItemsRecentBuy()
        }

        // Inflate the layout for this fragment
        // setupRecyclerView()
        return binding.root
    }

    //fun to see items in recent buy
    private fun seeItemsRecentBuy() {
        listOfOrderItems.firstOrNull()?.let { recentItem ->
            val intent = Intent(requireContext(), recentOrderItems::class.java)
            intent.putExtra("recentItem", listOfOrderItems)
            startActivity(intent)
        }
    }

    //fun to retrieve data from firebase in buyHistory
    private fun restiveBuyHistory() {
        binding.recentBuyItem.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid ?: ""

        val buyItemReference: DatabaseReference =
            database.reference.child("user").child(userId).child("BuyHistory")
        val sortQuery = buyItemReference.orderByChild("currentTime")

        sortQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children) {
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItems.add(it)
                    }
                }
                listOfOrderItems.reverse()
                if (listOfOrderItems.isNotEmpty()) {
                    //display the most recent order details
                    setDataInRecentBuyItem()

                    //setup the recycler view with prev order details
                    setPrevBuyItemRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            //fun to set the recent orders details in recycler view
            private fun setDataInRecentBuyItem() {
                binding.recentBuyItem.visibility = View.VISIBLE
                val recentOrderItems = listOfOrderItems.firstOrNull()
                recentOrderItems?.let {
                    with(binding) {
                        buyAgainFoodName.text = it.foodNames?.firstOrNull() ?: ""
                        buyAgainFoodPrice.text = it.foodPrices?.firstOrNull() ?: ""
                        val image = it.foodImages?.firstOrNull() ?: ""
                        val uri = Uri.parse(image)

                        Glide.with(requireContext()).load(uri).into(buyAgainFoodImage)

                        listOfOrderItems.reverse()
                        if (listOfOrderItems.isNotEmpty()) {

                        }
                    }
                }
            }
        })
    }

    //fun to display the most recent order details
    private fun setPrevBuyItemRecyclerView() {
        val buyAgainFoodName = mutableListOf<String>()
        val buyAgainFoodPrice = mutableListOf<String>()
        val buyAgainFoodImage = mutableListOf<String>()

        for (i in 0 until listOfOrderItems.size - 1) {
            listOfOrderItems[i].foodNames?.firstOrNull()?.let {
                buyAgainFoodName.add(it)
                listOfOrderItems[i].foodPrices?.firstOrNull()?.let {
                    buyAgainFoodPrice.add(it)
                    listOfOrderItems[i].foodImages?.firstOrNull()?.let {
                        buyAgainFoodImage.add(it)
                    }

                    val rv = binding.buyAgainRecyclerView
                    rv.layoutManager = LinearLayoutManager(requireContext())
                    buyAgainAdapter =
                        BuyAgainAdapter(
                            buyAgainFoodName,
                            buyAgainFoodPrice,
                            buyAgainFoodImage,
                            requireContext()
                        )
                    rv.adapter = buyAgainAdapter
                }
            }
        }
    }
}