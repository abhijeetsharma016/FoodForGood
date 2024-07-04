package com.example.foodforgood.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodforgood.databinding.RecentBuyItemBinding

class RecentBuyAdapter(
    private var context: Context,
    private var foodNameList: ArrayList<String>,
    private var foodImgList: ArrayList<String>,
    private var foodPriceList: ArrayList<String>,
    private var foodQuantity: ArrayList<Int>
): RecyclerView.Adapter<RecentBuyAdapter.RecentBuyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentBuyViewHolder {
        val binding = RecentBuyItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return RecentBuyViewHolder(binding)
    }

    override fun getItemCount(): Int = foodNameList.size

    override fun onBindViewHolder(holder: RecentBuyViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class RecentBuyViewHolder(private val binding: RecentBuyItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                foodName.text = foodNameList[position]
                foodPrice.text = foodPriceList[position]
                foodQuentity.text = foodQuantity[position].toString()

                val uriString = foodImgList[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(foodImage)
            }
        }

    }
}