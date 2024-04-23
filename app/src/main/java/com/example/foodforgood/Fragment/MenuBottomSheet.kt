package com.example.foodforgood.Fragment

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodforgood.R

import com.example.foodforgood.adapter.menuAdapter
import com.example.foodforgood.databinding.FragmentMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class menuBottomSheet :  BottomSheetDialogFragment(){
    private lateinit var binding: FragmentMenuBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)

        binding.menuButtonBack.setOnClickListener{
            dismiss()
        }

        val menuFoodName = listOf("Burger", "sandwich", "momo", "item", "sandwich", "momo","Burger", "sandwich", "momo", "item", "sandwich", "momo")
        val menuItemPrice = listOf("$5", "$6", "$8", "$9", "$10", "$11","$5", "$6", "$8", "$9", "$10", "$11")
        val menuImage = listOf(
            R.drawable.menu1,
            R.drawable.menu2,
            R.drawable.menu3,
            R.drawable.menu4,
            R.drawable.menu5,
            R.drawable.menu2,
            R.drawable.menu1,
            R.drawable.menu2,
            R.drawable.menu3,
            R.drawable.menu4,
            R.drawable.menu5,
            R.drawable.menu2
        )

        val adapter = menuAdapter(ArrayList(menuFoodName), ArrayList(menuItemPrice), ArrayList(menuImage))
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter
        return binding.root
    }

    companion object {

    }
}