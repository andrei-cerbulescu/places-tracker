package com.andrei.cerbulescu.placestracker.fragments

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.andrei.cerbulescu.placestracker.R
import com.andrei.cerbulescu.placestracker.data.PlaceViewModel
import com.andrei.cerbulescu.placestracker.databinding.FragmentHomeBinding

class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mPlaceViewModel: PlaceViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val view = binding.root

        mPlaceViewModel = ViewModelProvider(this)[PlaceViewModel::class.java]
        mPlaceViewModel.readAllData.observe(viewLifecycleOwner, Observer{ places ->
            binding.counterText.text = places.size.toString()
        })

        binding.cameraButton.setOnClickListener{
            findNavController().navigate(R.id.action_home_to_camera)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}