package com.example.adminfoodforgood.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.adminfoodforgood.databinding.PendingOrdersItemBinding

class PendingOrderAdapter(
    private val context: Context,
    private val customerNames: MutableList<String>,
    private val quantities: MutableList<String>,
    private val foodImages: MutableList<String>
) : RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding =
            PendingOrdersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
        holder.bind(customerNames[position], quantities[position], foodImages[position])
    }

    override fun getItemCount(): Int = customerNames.size

    inner class PendingOrderViewHolder(private val binding: PendingOrdersItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false

        fun bind(foodName: String, foodPrice: String, foodImage: String) {
            binding.apply {
                customerName.text = foodName
                pendingOrderQuentity.text = foodPrice

                // Log the image URL
                Log.d("Adapter", "Image URL: $foodImage")

                Glide.with(itemView.context)
                    .load(foodImage)
                    .override(300, 200) // Set the width and height to 300 and 200 respectively
                    .into(orderFoodItemImage)

                orderAcceptButton.setOnClickListener {
                    if (!isAccepted) {
                        orderAcceptButton.text = "Dispatch"
                        isAccepted = true
                        showToast("Order Accepted")
                    } else {
                        customerNames.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                        showToast("Order Dispatched")
                    }
                }
            }
        }
    }


    private fun showToast(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }
}