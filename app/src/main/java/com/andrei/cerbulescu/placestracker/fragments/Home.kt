package com.andrei.cerbulescu.placestracker.fragments

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            get_permissions()
        }
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
        if (view == null) {
            return
        }
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
        if (::googleMap.isInitialized){
            googleMap.clear()
        }
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

    @RequiresApi(Build.VERSION_CODES.M)
    fun get_permissions(){
        var toBeRequested = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.POST_NOTIFICATIONS
        )
        for(e in toBeRequested){
            if(checkSelfPermission(requireContext(), e) != PermissionChecker.PERMISSION_GRANTED){
                findNavController().popBackStack(R.id.pendingPermissions, false, false)
                findNavController().navigate(R.id.pendingPermissions)
            }
        }
    }
}