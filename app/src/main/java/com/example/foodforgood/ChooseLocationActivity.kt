package com.example.foodforgood

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodforgood.databinding.ActivityChooseLocationBinding

class ChooseLocationActivity : AppCompatActivity() {
    private val binding: ActivityChooseLocationBinding by lazy {
        ActivityChooseLocationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val locationList = arrayOf(
            "Andhra Pradesh: Amaravati",
            "Arunachal Pradesh: Itanagar",
            "Assam: Dispur",
            "Bihar: Patna",
            "Chhattisgarh: Raipur",
            "Goa: Panaji",
            "Gujarat: Gandhinagar",
            "Haryana: Chandigarh",
            "Himachal Pradesh: Shimla",
            "Jharkhand: Ranchi",
            "Karnataka: Bengaluru",
            "Kerala: Thiruvananthapuram",
            "Madhya Pradesh: Bhopal",
            "Maharashtra: Mumbai",
            "Manipur: Imphal",
            "Meghalaya: Shillong",
            "Mizoram: Aizawl",
            "Nagaland: Kohima",
            "Odisha: Bhubaneswar",
            "Punjab: Chandigarh",
            "Rajasthan: Jaipur",
            "Sikkim: Gangtok",
            "Tamil Nadu: Chennai",
            "Telangana: Hyderabad",
            "Tripura: Agartala",
            "Uttar Pradesh: Lucknow",
            "Uttarakhand: Dehradun (Winter), Gairsain (Summer)",
            "West Bengal: Kolkata",
            "Andaman and Nicobar Islands: Port Blair",
            "Chandigarh: Chandigarh",
            "Dadra & Nagar Haveli and Daman & Diu: Daman",
            "Delhi: New Delhi",
            "Jammu and Kashmir: Srinagar (Summer), Jammu (Winter)",
            "Lakshadweep: Kavaratti",
            "Puducherry: Pondicherry",
            "Ladakh: Leh"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)
    }
}