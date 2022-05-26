package com.javid.zohotask.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.javid.zohotask.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.mcViewPhase1.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPhase1Fragment())
        }
        binding.mcViewPhase2.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPhase2Fragment())
        }
    }

}