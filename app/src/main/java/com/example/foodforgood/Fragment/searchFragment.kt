package com.example.foodforgood.Fragment

import android.app.DownloadManager.Query
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.foodforgood.R
import com.example.foodforgood.adapter.menuAdapter
import com.example.foodforgood.databinding.FragmentHomeBinding
import com.example.foodforgood.databinding.FragmentSearchBinding
import com.example.foodforgood.databinding.MenuItemBinding


class searchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: menuAdapter
    private val originalMenuFoodName = listOf(
        "Burger",
        "sandwich",
        "momo",
        "item",
        "sandwich",
        "momo",
        "Burger",
        "sandwich",
        "momo",
        "item",
        "sandwich",
        "momo"
    )
    private val originalMenuItemPrice = listOf("$5", "$6", "$8", "$9", "$10", "$11","$5", "$6", "$8", "$9", "$10", "$11")
    private val originalMenuImage = listOf(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    private val filterMenuFoodName = MutableListOf<String>
    private val filterMenuItemPrice = MutableListOf<String>
    private val filterMenuImage = MutableListOf<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        adapter = menuAdapter(filterMenuFoodName, filterMenuItemPrice, filterMenuImage)
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter

        //Setup for search view
        setupSearchView()
        return binding.root
    }
    private fun setupSearchView(){
        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                filterMenuItems(query)
            }
        })
    }

    private fun filterMenuItems(query: String) {
        filterMenuFoodName.clear()
        filterMenuItemPrice.clear()
        filterMenuImage.clear()

        originalMenuFoodName.forEachIndexed { index, foodName ->
            if(foodName.contains(query, ignoreCase = true)){
                filterMenuFoodName.add(foodName)
                filterMenuItemPrice.add(originalMenuItemPrice[index])
                filterMenuImage.add(originalMenuImage[index])
            }
        }
    }

    companion object {


    }
}