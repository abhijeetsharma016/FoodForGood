package com.example.adminfoodforgood

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodforgood.adapter.menuItemAdapter
import com.example.adminfoodforgood.databinding.ActivityAllItemBinding
import com.example.adminfoodforgood.model.AllMenu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList
import kotlin.math.log

class allItemActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var menuItem: ArrayList<AllMenu> = ArrayList()
    private val binding: ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        databaseReference = FirebaseDatabase.getInstance().reference
        retrieveMenuItem()


        binding.backButton.setOnClickListener {
            finish()
        }


    }

    private fun retrieveMenuItem() {
        database = FirebaseDatabase.getInstance()
        val foodRef = database.reference.child("menu")

        //fetch data from database
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear existing data before populating
                menuItem.clear()

                //loop for through each item in database
                for (foodSnapshot in snapshot.children) {
                    val menuItems = foodSnapshot.getValue(AllMenu::class.java)
                    menuItems?.let { menuItem.add(it) }
                }

                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Database Error:", "Error: ${error.message}")
            }
        })

    }

    private fun setAdapter() {
        val adapter =
            menuItemAdapter(this@allItemActivity, menuItem, databaseReference){position ->
                deleteMenuItem(position)
            }
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.menuRecyclerView.adapter = adapter

    }

    private fun deleteMenuItem(position: Int) {
        val menuItemDelete = menuItem[position]
        val menuItemKey = menuItemDelete.key
        val foodRef = database.reference.child("menu").child(menuItemKey!!)

        foodRef.removeValue().addOnCompleteListener {task->
            if(task.isSuccessful){
                menuItem.removeAt(position)
                binding.menuRecyclerView.adapter?.notifyItemRemoved(position)
            }
            else{
                Toast.makeText(this,"Failed to delete", Toast.LENGTH_SHORT).show()
            }
        }
    }
}