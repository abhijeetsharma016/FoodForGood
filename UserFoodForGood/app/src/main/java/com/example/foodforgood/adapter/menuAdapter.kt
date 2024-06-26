package com.example.foodforgood.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodforgood.DetailsActivity
import com.example.foodforgood.databinding.MenuItemBinding
import com.example.foodforgood.model.menuItem

class menuAdapter(
    private val menuItems: List<menuItem>,
    private val requireContext: Context
) : RecyclerView.Adapter<menuAdapter.MenuViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuItems.size

    inner class MenuViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailActivity(position)
                }
            }
        }

        private fun openDetailActivity(position: Int) {
            val menuItem = menuItems[position]
            Log.d("MenuAdapter", "foodIngredient: ${menuItem.foodIngredient}")
            //a intent to open detail activity and pass data
            val intent = Intent(requireContext, DetailsActivity::class.java).apply {
                putExtra("MenuItemName", menuItem.foodName)
                putExtra("MenuItemImage", menuItem.foodImage)
                putExtra("MenuItemDescription", menuItem.foodDescription)
                putExtra("MenuItemIngredient", menuItem.foodIngredient)
                putExtra("MenuItemPrice", menuItem.foodPrice)
            }

            //start the detail activity
            requireContext.startActivity(intent)
        }


        //set data into recyclerview
        fun bind(position: Int) {
            binding.apply {
                val menuItems = menuItems[position]
                menuFoodName.text = menuItems.foodName
                menuPrice.text = menuItems.foodPrice
                val uri = Uri.parse(menuItems.foodImage)
                //load image using glide
                Glide.with(requireContext).load(uri).into(menuImage)


            }
        }

    }

}

