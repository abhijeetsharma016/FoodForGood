package com.example.foodforgood.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodforgood.databinding.CartItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class cartAdapter(

    private val context: Context,
    private val CartItems: MutableList<String>,
    private val CartItemPrices: MutableList<String>,
    private var CartImages: MutableList<String>,
    private var cartDescriptions: MutableList<String>,
    private var cartQuantity: MutableList<Int>,
    private val cartIngredients: MutableList<String>
) : RecyclerView.Adapter<cartAdapter.CartViewHolder>() {



    //Instance Firebase
    private val auth = FirebaseAuth.getInstance()
    init {
        //Initialize Firebase
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid?:""
        val cartItemNumber = CartItems.size

        itemQuantity = IntArray(cartItemNumber) { 1 }
        cartItemsReference = database.reference.child("user").child(userId).child("cartItems")

    }
    companion object{
        private var itemQuantity: IntArray = intArrayOf()
        private lateinit var cartItemsReference: DatabaseReference
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = CartItems.size

    //getUpdated quantity
    fun getItemQuantity(): MutableList<Int> {
        val itemQuantity = mutableListOf<Int>()
        itemQuantity.addAll(cartQuantity)
        return itemQuantity
    }


    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantity[position]
                cartFoodName.text = CartItems[position]
                cartItemPrice.text = CartItemPrices[position]

                //load image using glide
                val uriString = CartImages[position].toString()
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(cartImage)

                //TODO if you want to log glide do this
                /*Glide.with(context).load(uri).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d("Glide", "Failed to load image")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d("Glide", "Success to load image")
                        return false
                    }
                }).into(cartImage)*/


                cartItemQuentity.text = quantity.toString()

                minusButton.setOnClickListener {
                    decreaseQuantity(position)
                }

                plusButton.setOnClickListener {
                    increaseQuantity(position)
                }

                deleteButton.setOnClickListener {
                    var itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteItem(itemPosition)
                    }
                }
            }
        }


        private fun decreaseQuantity(position: Int) {
            if (itemQuantity[position] > 1) {
                itemQuantity[position]--
                cartQuantity[position] = itemQuantity[position]
                binding.cartItemQuentity.text = itemQuantity[position].toString()
            }
        }
        private fun increaseQuantity(position: Int) {
            if (itemQuantity[position] < 10) {
                itemQuantity[position]++
                cartQuantity[position] = itemQuantity[position]
                binding.cartItemQuentity.text = itemQuantity[position].toString()
            }
        }

        private fun deleteItem(position: Int) {
            val positionRetrieve = position
            getUniqueKeyAtPosition(positionRetrieve){uniqueKey ->
                if(uniqueKey != null){
                    removeItem(position, uniqueKey)
                }
            }
        }

        private fun removeItem(position: Int, uniqueKey: String) {
            if(uniqueKey!= null){
                cartItemsReference.child(uniqueKey).removeValue().addOnSuccessListener {
                    if (position < CartItems.size) {
                        CartItems.removeAt(position)
                    }
                    if (position < CartImages.size) {
                        CartImages.removeAt(position)
                    }
                    if (position < cartDescriptions.size) {
                        cartDescriptions.removeAt(position)
                    }
                    if (position < cartQuantity.size) {
                        cartQuantity.removeAt(position)
                    }
                    if (position < cartIngredients.size) {
                        cartIngredients.removeAt(position)
                    }
                    if (position < CartItemPrices.size) {
                        CartItemPrices.removeAt(position)
                    }
                    Toast.makeText(context, "item deleted successfully", Toast.LENGTH_SHORT).show()
                    //update item quantities
                    itemQuantity = itemQuantity.filterIndexed {index, i -> index!= position}.toIntArray()
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, CartItems.size)
                    notifyDataSetChanged()
                }.addOnFailureListener{
                    Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun getUniqueKeyAtPosition(positionRetrieve: Int, onComplete:(String?) -> Unit){
            cartItemsReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey: String? = null

                    //loop for snapshot children
                    snapshot.children.forEachIndexed { index, dataSnapshot ->
                        if(index == positionRetrieve){
                            uniqueKey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    onComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        }
    }

}