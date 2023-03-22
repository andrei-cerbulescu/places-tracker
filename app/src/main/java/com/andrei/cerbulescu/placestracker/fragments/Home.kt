package com.andrei.cerbulescu.placestracker.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.andrei.cerbulescu.placestracker.R
import com.andrei.cerbulescu.placestracker.data.Place
import com.andrei.cerbulescu.placestracker.data.PlaceDao
import com.andrei.cerbulescu.placestracker.data.PlaceViewModel
import com.andrei.cerbulescu.placestracker.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class Home : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mPlaceViewModel: PlaceViewModel

    private lateinit var googleMap: GoogleMap
    private lateinit var locationManager: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val view = binding.root

        mPlaceViewModel = ViewModelProvider(this)[PlaceViewModel::class.java]

        binding.cameraButton.setOnClickListener{
            findNavController().navigate(R.id.action_home_to_camera)
        }

        locationManager = LocationServices.getFusedLocationProviderClient(requireActivity());

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        binding.mapView.getMapAsync(this)

        mPlaceViewModel.readAllData.observe(viewLifecycleOwner, Observer{
            for(place in it){
                googleMap.addMarker(MarkerOptions().position(LatLng(place.latitude.toDouble(),
                    place.longitude.toDouble()
                )))
            }
            }
        )

        return view
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        p0.let { map ->
            googleMap = map
            locationManager.lastLocation.addOnCompleteListener{
                var lat: Double = 0.0
                var long: Double = 0.0

                if(it.result != null){
                    lat = it.result.latitude
                    long = it.result.longitude
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, long),
                    20F
                ))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        googleMap.clear()
    }
}