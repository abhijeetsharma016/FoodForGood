package com.example.foodforgood

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodforgood.databinding.ActivityPayOutBinding
import com.example.foodforgood.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PayOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayOutBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var totalamount: String
    private lateinit var fooItemName: ArrayList<String>
    private lateinit var fooItemPrice: ArrayList<String>
    private lateinit var fooItemImage: ArrayList<String>
    private lateinit var fooItemDescription: ArrayList<String>
    private lateinit var fooItemIngredient: ArrayList<String>
    private lateinit var fooItemQuantity: ArrayList<Int>
    private lateinit var userId: String
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //database reference
        databaseReference = FirebaseDatabase.getInstance().getReference()

        //Initialize firebase and user details
        auth = FirebaseAuth.getInstance()

        //get user details from firebase
        val intent = intent
        fooItemName = intent.getStringArrayListExtra("fooItemName") as ArrayList<String>
        fooItemPrice = intent.getStringArrayListExtra("fooItemPrice") as ArrayList<String>
        fooItemImage = intent.getStringArrayListExtra("fooItemImage") as ArrayList<String>
        fooItemDescription =
            intent.getStringArrayListExtra("fooItemDescription") as ArrayList<String>
        fooItemIngredient = intent.getStringArrayListExtra("fooItemIngredient") as ArrayList<String>
        fooItemQuantity = intent.getIntegerArrayListExtra("fooItemQuantity") as ArrayList<Int>

        totalamount = calculateTotalAmount().toString() + "₹"
        binding.totalamount.isEnabled = false
        binding.totalamount.setText(totalamount)

        //set userdata
        setuserdata()

        binding.backButton.setOnClickListener {
            finish()
        }
        binding.placeMyOrder.setOnClickListener {
            name = binding.name.text.toString().trim()
            address = binding.address.text.toString().trim()
            phone = binding.phone.text.toString().trim()
            if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                placeOrder()
            }
        }
    }

    private fun placeOrder() {
        userId = auth.currentUser?.uid ?: ""
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("orderDetails").push().key
        val orderDetails = OrderDetails(
            userId,
            name,
            fooItemName,
            fooItemImage,
            fooItemPrice,
            fooItemQuantity,
            address,
            totalamount,
            phone,
            false,
            false,
            itemPushKey,
            time
        )
        val orderReference = databaseReference.child("orderDetails").child(itemPushKey!!)
        orderReference.setValue(orderDetails).addOnSuccessListener {
            val bottomSheetDialog = CongratsBottomSheet()
            bottomSheetDialog.show(supportFragmentManager, "Test")
            removeItemFromCart()

            addOrderToHistory(orderDetails)
        }
    }

    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("user").child(userId).child("BuyHistory")
            .child(orderDetails.itemPushKey!!).setValue(orderDetails).addOnSuccessListener {

        }.addOnFailureListener{
            Toast.makeText(this, "Failed to add order to history", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeItemFromCart() {
        val cartItemsReference = databaseReference.child("user").child(userId).child("cartItems")
        cartItemsReference.removeValue()
    }

    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for (i in 0 until fooItemName.size) {
            var price = fooItemPrice[i]
            val lastChar = price.last()
            val priceIntValue = if (lastChar == '$') {
                price.dropLast(1).toInt()
            } else {
                price.trim().toInt()
            }
            var quantity = fooItemQuantity[i]
            totalAmount += priceIntValue * quantity
        }
        return totalAmount
    }

    private fun setuserdata() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val userReference = databaseReference.child("user").child(userId).child("details")

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val names = snapshot.child("name").getValue(String::class.java) ?: ""
                        val addresses = snapshot.child("address").getValue(String::class.java) ?: ""
                        val phones = snapshot.child("phone").getValue(String::class.java) ?: ""

                        Log.d("FirebaseData", "Name: $names, Address: $addresses, Phone: $phones")

                        runOnUiThread {
                            binding.apply {
                                name.setText(names)
                                address.setText(addresses)
                                phone.setText(phones)
                            }
                        }
                    } else {
                        Log.d("FirebaseData", "No data exists")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("FirebaseData", "Error: ${error.message}")
                }
            })
        }
    }
}