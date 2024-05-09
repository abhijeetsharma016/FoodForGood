package com.example.foodforgood

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodforgood.databinding.FragmentNotificationBottomFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class notification_bottom_fragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotificationBottomFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBottomFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {

    }
}