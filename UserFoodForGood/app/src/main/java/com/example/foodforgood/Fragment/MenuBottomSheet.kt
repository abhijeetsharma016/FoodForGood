package com.example.foodforgood.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodforgood.adapter.menuAdapter
import com.example.foodforgood.databinding.FragmentMenuBottomSheetBinding
import com.example.foodforgood.model.menuItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class menuBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentMenuBottomSheetBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<menuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)

        binding.menuButtonBack.setOnClickListener {
            dismiss()
        }

        retrieveMenuItems()

        return binding.root
    }

    private fun retrieveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (menuSnapshot in snapshot.children) {
                        val menuItem = menuSnapshot.getValue(menuItem::class.java)
                        menuItem?.let {
                            menuItems.add(it)
                        }
                    }
                    Log.d("Items", "Data retrieved successfully")
                    //Once data is received set adapter, set adapter
                    setAdapter()
                }
            }


            override fun onCancelled(error: DatabaseError) {
                // handle error
            }
        })
    }
    private fun setAdapter() {
        if (menuItems.isNotEmpty()) {
            val adapter = menuAdapter(menuItems, requireContext())
            binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.menuRecyclerView.adapter = adapter
            Log.d("Items", "Data retrieved successfully")
        }
        else{
            Log.d("Items", "No data found")
        }
    }
    companion object{

    }
}