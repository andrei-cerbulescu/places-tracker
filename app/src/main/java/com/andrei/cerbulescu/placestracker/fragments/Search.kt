package com.andrei.cerbulescu.placestracker.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.andrei.cerbulescu.placestracker.R
import com.andrei.cerbulescu.placestracker.data.PlaceViewModel
import com.andrei.cerbulescu.placestracker.databinding.FragmentSearchBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng

class Search : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentSearchBinding

    private lateinit var googleMap: GoogleMap
    private lateinit var mPlaceViewModel: PlaceViewModel

    private lateinit var locationManager: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)

        mPlaceViewModel = ViewModelProvider(this)[PlaceViewModel::class.java]
        locationManager = LocationServices.getFusedLocationProviderClient(requireActivity());

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        binding.mapView.getMapAsync(this)

        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        locationManager.lastLocation.addOnCompleteListener{
            var lat: Double = 0.0
            var long: Double = 0.0

            if(it.result != null){
                lat = it.result.latitude
                long = it.result.longitude
            }
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(lat, long),
                15F
            ))
        }

        googleMap.setOnMapClickListener{
            mPlaceViewModel.findByDistance(it.latitude, it.longitude, 0.01)
                .observe(viewLifecycleOwner, Observer{ places ->
                    places
            })
        }
    }
}