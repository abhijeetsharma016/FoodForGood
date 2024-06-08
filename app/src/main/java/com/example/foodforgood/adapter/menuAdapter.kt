package com.example.foodforgood.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.android.car.ui.toolbar.MenuItem.OnClickListener
import com.example.foodforgood.DetailsActiviry
import com.example.foodforgood.databinding.MenuItemBinding

class menuAdapter(private val menuItemName: MutableList<String>, private val menuItemPrice : MutableList<String>, private val MenuImage:MutableList<Int>, private val requireContext: Context): RecyclerView.Adapter<menuAdapter.MenuViewHolder>() {

    private val itemClickListener:OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int = menuItemName.size

    inner class MenuViewHolder(private val binding: MenuItemBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(position)
                }
                //Set OnClick Listener to open details

                val intent = Intent(requireContext, DetailsActiviry::class.java)
                intent.putExtra("MenuItemName", menuItemName.get(position))
                intent.putExtra("MenuItemImage", MenuImage.get(position))
                requireContext.startActivity(intent)
            }
        }
        fun bind(position: Int) {
            binding.apply {
                menuFoodName.text = menuItemName[position]
                menuPrice.text = menuItemPrice[position]
                menuImage.setImageResource(MenuImage[position])

            }
        }

    }
}

private fun OnClickListener?.onItemClick(position: Int) {
    TODO("Not yet implemented")
}
