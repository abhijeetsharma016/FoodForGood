package com.example.adminfoodforgood

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodforgood.adapter.addItemAdapter
import com.example.adminfoodforgood.databinding.ActivityAllItemBinding
import java.util.ArrayList

class allItemActivity : AppCompatActivity() {
    private val binding: ActivityAllItemBinding by lazy { ActivityAllItemBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val menuFoodName = listOf("Burger", "Sandwich", "Momo", "Burger", "Sandwich", "Momo")
        val menuItemPrice = listOf("$5", "$6", "$8", "$9", "$10", "$10")
        val menuImage = listOf(R.drawable.menu1, R.drawable.menu2, R.drawable.menu4, R.drawable.menu3, R.drawable.menu5, R.drawable.menu3)

        binding.backButton.setOnClickListener{
            finish()
        }
        val adapter = addItemAdapter(ArrayList(menuFoodName), ArrayList(menuItemPrice), ArrayList(menuImage))

        binding.menuRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.menuRecyclerView.adapter = adapter
    }
}