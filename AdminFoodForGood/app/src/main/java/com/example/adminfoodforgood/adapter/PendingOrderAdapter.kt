package com.example.adminfoodforgood.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodforgood.databinding.PendingOrdersItemBinding

class PendingOrderAdapter(
    private val context: Context,
    private val customerNames: MutableList<String>,
    private val quantities: MutableList<String>,
    private val foodImages: MutableList<String>,
    private val itemClicked: OnItemClicked,
    ) : RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {

        interface OnItemClicked {
            fun onItemClickLister(position: Int)
            fun onItemAcceptClickListener(position: Int)
            fun onItemDispatchedClickListener(position: Int)
        }


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
                        itemClicked.onItemAcceptClickListener(position)
                    } else {
                        customerNames.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                        showToast("Order Dispatched")
                        itemClicked.onItemDispatchedClickListener(position)
                    }
                }
            }
            itemView.setOnClickListener {
                itemClicked.onItemClickLister(position)
            }
        }
    }


    private fun showToast(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }
}