package com.example.foodforgood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.foodforgood.databinding.FragmentHomeBinding
import com.example.foodforgood.databinding.PopularItemBinding

class popularAdapter(private var items:List<String>,private val price:List<String>, private val image: List<Int>): RecyclerView.Adapter<popularAdapter.PopularViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false ))
    }


    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val item = items[position]
        val images = image[position]
        val price = price[position]
        holder.bind(item, price, images)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class PopularViewHolder(private val binding: PopularItemBinding):RecyclerView.ViewHolder(binding.root) {
        private val imageView = binding.imageView6
        fun bind(item: String,price: String, images: Int) {
            binding.foodNamePopular.text = item
            binding.pricePopular.text = price
            imageView.setImageResource(images)
        }

    }
}