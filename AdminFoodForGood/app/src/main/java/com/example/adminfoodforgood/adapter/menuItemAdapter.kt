package com.example.adminfoodforgood.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodforgood.databinding.ItemItemBinding
import com.example.adminfoodforgood.model.AllMenu
import com.google.firebase.database.DatabaseReference

class menuItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference,
    private val onDeleteClickListener: (position: Int) -> Unit

) : RecyclerView.Adapter<menuItemAdapter.addItemViewHolder>() {

    private val itemQuantity = IntArray(menuList.size) { 1 }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): addItemViewHolder {
        val binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return addItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: addItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuList.size

    inner class addItemViewHolder(private val binding: ItemItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantity[position]
                val menuItem = menuList[position]
                val uriString = menuItem.foodImage
                val uri = Uri.parse(uriString)

                foodNameTextView.text = menuItem.foodName
                priceTextView.text = menuItem.foodPrice

                // Log the URI to check if it's being parsed correctly
                Log.d("menuItemAdapter", "Image URI: $uriString")

                Glide.with(context)
                    .load(uri)
                    .override(200, 200) // resize to 200x200
                    .into(foodImageView) // glide is used to load the image from firebase in speed

                quantityTextView.text = quantity.toString()
                minusButton.setOnClickListener {
                    decreaseQuantity(position)
                }
                plusButton.setOnClickListener {
                    increaseQuantity(position)
                }
                deleteButton.setOnClickListener {
                    onDeleteClickListener(position)
                }
            }
        }

        private fun decreaseQuantity(position: Int) {
            if (itemQuantity[position] > 1) {
                itemQuantity[position]--
                binding.quantityTextView.text = itemQuantity[position].toString()
            }
        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantity[position] < 10) {
                itemQuantity[position]++
                binding.quantityTextView.text = itemQuantity[position].toString()
            }
        }

        private fun deleteQuantity(position: Int) {
            menuList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menuList.size)
        }
    }
}
