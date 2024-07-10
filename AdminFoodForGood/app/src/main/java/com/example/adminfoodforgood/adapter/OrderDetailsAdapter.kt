package com.example.adminfoodforgood.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodforgood.databinding.OrderDetailsItemsBinding

class OrderDetailsAdapter(
    private var context: Context,
    private var foodName: ArrayList<String>,
    private var foodPrice: ArrayList<String>,
    private var foodQuantity: ArrayList<Int>,
    private var foodImage: ArrayList<String>
): RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val binding = OrderDetailsItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = foodName.size

    inner class OrderDetailsViewHolder(private var binding: OrderDetailsItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.foodName.text = foodName[position]
            binding.foodPrice.text = foodPrice[position]
            binding.foodQuantity.text = foodQuantity[position].toString()
            val uriString = foodImage[position]
            val uri = Uri.parse(uriString)
            Glide.with(context)
                .load(uri)
                .override(200, 200) // set custom width and height
                .into(binding.orderFoodItemImage);
        }

    }


}