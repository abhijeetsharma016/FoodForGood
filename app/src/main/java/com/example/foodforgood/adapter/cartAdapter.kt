package com.example.foodforgood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodforgood.databinding.CartItemBinding

class cartAdapter(private val cartItems : MutableList<String>, private val cartItemPrice: MutableList<String>, private val CartImage: MutableList<Int>): RecyclerView.Adapter<cartAdapter.CartViewHolder>() {

    private val itemQuantity = IntArray(cartItems.size){1};
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cartItems.size


    inner class CartViewHolder(private val binding: CartItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantity[position]
                cartFoodName.text = cartItems[position]
                cartIntemPrice.text = cartItemPrice[position]
                cartImage.setImageResource(CartImage[position])
                cartItemQuentity.text = quantity.toString()

            }
        }

    }
}