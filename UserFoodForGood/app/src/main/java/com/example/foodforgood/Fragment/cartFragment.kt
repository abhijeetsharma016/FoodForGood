package com.example.foodforgood.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodforgood.PayOutActivity
import com.example.foodforgood.adapter.cartAdapter
import com.example.foodforgood.databinding.FragmentCartBinding
import com.example.foodforgood.model.cartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class cartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var foodName: MutableList<String>
    private lateinit var foodImageUri: MutableList<String>
    private lateinit var foodDescription: MutableList<String>
    private lateinit var foodIngredient: MutableList<String>
    private lateinit var foodPrice: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var auth: FirebaseAuth
    private lateinit var cartAdapter: cartAdapter
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        restiveCardItems()


        binding.cartProceedButton.setOnClickListener {
            if (foodName.isEmpty()) {
                Toast.makeText(requireContext(), "Cart is empty", Toast.LENGTH_SHORT).show()
            } else {
                getOrderedDetails()
            }
        }
        return binding.root
    }

    private fun orderNow(
        foodName: MutableList<String>,
        foodPrice: MutableList<String>,
        foodImage: MutableList<String>,
        foodDescription: MutableList<String>,
        foodIngredient: MutableList<String>,
        foodQuantity: MutableList<Int>
    ) {
        if (isAdded && context != null) {
            val intent = Intent(requireContext(), PayOutActivity::class.java)
            intent.putExtra("fooItemName", foodName as ArrayList<String>)
            intent.putExtra("fooItemPrice", foodPrice as ArrayList<String>)
            intent.putExtra("fooItemImage", foodImage as ArrayList<String>)
            intent.putExtra("fooItemDescription", foodDescription as ArrayList<String>)
            intent.putExtra("fooItemIngredient", foodIngredient as ArrayList<String>)
            intent.putExtra("fooItemQuantity", foodQuantity as ArrayList<Int>)
            startActivity(intent)
        }
    }

    private fun getOrderedDetails() {
        val orderIdReference: DatabaseReference =
            database.reference.child("user").child(userId).child("cartItems")
        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodImage = mutableListOf<String>()
        val foodDescription = mutableListOf<String>()
        val foodIngredient = mutableListOf<String>()

        //get items quantity
        val foodQuantity = cartAdapter.getItemQuantity()
        orderIdReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    //get the cart items object from child node
                    val orderItems = foodSnapshot.getValue(cartItems::class.java)
                    //add the data to the list
                    orderItems?.foodName?.let { foodName.add(it) }
                    orderItems?.foodPrice?.let { foodPrice.add(it) }
                    orderItems?.foodImage?.let { foodImage.add(it) }
                    orderItems?.foodDescription?.let { foodDescription.add(it) }
                    orderItems?.foodIngredient?.let { foodIngredient.add(it) }
                }
                orderNow(
                    foodName,
                    foodPrice,
                    foodImage,
                    foodDescription,
                    foodIngredient,
                    foodQuantity
                )

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Order making failed pls try again later",
                    Toast.LENGTH_SHORT
                )
            }
        })
    }

    private fun restiveCardItems() {
        //database reference to the database
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""
        val foodRef: DatabaseReference =
            database.reference.child("user").child(userId).child("cartItems")

        //cart Items
        foodName = mutableListOf()
        foodPrice = mutableListOf()
        foodDescription = mutableListOf()
        foodIngredient = mutableListOf()
        foodImageUri = mutableListOf()
        quantity = mutableListOf()

        // fetch data from the database
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {

                    //get the cart items object from child node
                    val cartItems = foodSnapshot.getValue(cartItems::class.java)

                    //add the data to the list
                    cartItems?.foodName?.let { foodName.add(it) }
                    cartItems?.foodPrice?.let { foodPrice.add(it) }
                    cartItems?.foodDescription?.let { foodDescription.add(it) }
                    cartItems?.foodImage?.let { foodImageUri.add(it) }
                    cartItems?.foodQuantity?.let { quantity.add(it) }
                    cartItems?.foodIngredient?.let { foodIngredient.add(it) }
                }
                setAdapter()
            }

            private fun setAdapter() {
                cartAdapter = cartAdapter(
                    requireContext(),
                    foodName,
                    foodPrice,
                    foodImageUri,
                    foodDescription,
                    quantity,
                    foodIngredient,
                )
                binding.cartRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.cartRecyclerView.adapter = cartAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "data not fetched", Toast.LENGTH_SHORT).show()
            }

        })
    }

    companion object {

    }
}