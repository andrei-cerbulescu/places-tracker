package com.andrei.cerbulescu.placestracker.fragments

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andrei.cerbulescu.placestracker.R
import com.andrei.cerbulescu.placestracker.data.AppDatabase
import com.andrei.cerbulescu.placestracker.data.Place
import com.andrei.cerbulescu.placestracker.data.PlaceViewModel
import com.andrei.cerbulescu.placestracker.databinding.FragmentPreviewImageBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files


class PreviewImage : Fragment(), OnMapReadyCallback {
    val args: PreviewImageArgs by navArgs()
    private lateinit var binding: FragmentPreviewImageBinding
    private lateinit var mPlaceViewModel: PlaceViewModel

    private lateinit var googleMap: GoogleMap

    @SuppressLint("WrongThread")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPreviewImageBinding.inflate(layoutInflater)
        var view = binding.root
        mPlaceViewModel = ViewModelProvider(this)[PlaceViewModel::class.java]

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        binding.mapView.getMapAsync(this)

        var imagePreview = binding.imagePreview
        var bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, args.imageUri)
        } else {
            val source: ImageDecoder.Source = ImageDecoder.createSource(requireContext().contentResolver, args.imageUri)
            ImageDecoder.decodeBitmap(source)
        }
        imagePreview.setImageBitmap(bitmap)

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)

       binding.saveButton.setOnClickListener {
           val place = Place(0, args.location.latitude, args.location.longitude, stream.toByteArray())
           mPlaceViewModel.addPlace(place)
           findNavController().popBackStack(R.id.home, false, false)
           findNavController().navigate(R.id.home)
       }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()

        requireContext().contentResolver.delete(args.imageUri, null, null)
    }

    override fun onMapReady(p0: GoogleMap) {
        p0?.let {
            googleMap = it
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(args.location.latitude, args.location.longitude),
                15F
            ))
            googleMap.addMarker(MarkerOptions().position(LatLng(args.location.latitude, args.location.longitude)))
        }
    }

}