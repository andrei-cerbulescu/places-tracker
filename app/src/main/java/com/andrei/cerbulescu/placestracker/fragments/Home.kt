package com.andrei.cerbulescu.placestracker.fragments

import android.annotation.SuppressLint
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class Home : Fragment(), OnMapReadyCallback, OnMarkerClickListener {
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

        locationManager = LocationServices.getFusedLocationProviderClient(requireActivity());

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        binding.mapView.getMapAsync(this)

        return view
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        p0.let { map ->
            googleMap = map
            googleMap.setOnMarkerClickListener(this)

            mPlaceViewModel.readAllData.observe(viewLifecycleOwner, Observer{
                googleMap.clear()
                for(place in it){
                    googleMap.addMarker(MarkerOptions().position(LatLng(
                        place.latitude,
                        place.longitude
                    )))
                }
            }
            )

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

    override fun onMarkerClick(p0: Marker): Boolean {
        mPlaceViewModel
            .findFirstByDistance(p0.position.latitude, p0.position.longitude)
            .observe(viewLifecycleOwner, Observer{
                val navigateAction = HomeDirections.actionHomeToPreviewLocation(it)
                findNavController().navigate(navigateAction)
        })

        return true
    }
}