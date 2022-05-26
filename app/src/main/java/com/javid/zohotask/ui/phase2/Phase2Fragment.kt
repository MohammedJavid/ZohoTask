package com.javid.zohotask.ui.phase2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.javid.zohotask.R
import com.javid.zohotask.databinding.FragmentPhase2Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Phase2Fragment : Fragment() {

    private lateinit var binding: FragmentPhase2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhase2Binding.inflate(inflater, container, false)
        return binding.root
    }

}