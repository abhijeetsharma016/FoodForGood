package com.example.adminfoodforgood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.location.LocationRequestCompat.Quality
import androidx.recyclerview.widget.RecyclerView
import com.example.adminfoodforgood.databinding.PendingOrdersItemBinding

class PendingOrderAdapter(
    private val customerNames: ArrayList<String>,
    private val quantities: ArrayList<String>,
    private val foodImages: ArrayList<Int>,
    private val context: Context
): RecyclerView.Adapter<PendingOrderAdapter.pendingOrderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): pendingOrderViewHolder {
        val binding = PendingOrdersItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return pendingOrderViewHolder(binding)
    }


    override fun onBindViewHolder(holder: pendingOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerNames.size

    inner class pendingOrderViewHolder(private val binding: PendingOrdersItemBinding):RecyclerView.ViewHolder(binding.root) {
        private var isAccected = false
        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerNames[position]
                pendingOrderQuentity.text = quantities[position]
                orderFoodItemImage.setImageResource(foodImages[position])
                orderAcceptButton.setOnClickListener {
                    if (!isAccected) {
                        orderAcceptButton.text = "Dispatch"
                        isAccected = true
                        showToast("Order Accepted")
                    }
                    else{
                        customerNames.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                        showToast("Order Dispatched")
                    }
                }
            }
        }
    }


    private fun showToast(s: String) {
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show()
    }

}