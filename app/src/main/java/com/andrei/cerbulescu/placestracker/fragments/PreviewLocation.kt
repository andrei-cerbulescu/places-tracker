package com.andrei.cerbulescu.placestracker.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andrei.cerbulescu.placestracker.R
import com.andrei.cerbulescu.placestracker.data.PlaceViewModel
import com.andrei.cerbulescu.placestracker.databinding.FragmentPreviewLocationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.ByteArrayOutputStream
import java.util.*


class PreviewLocation : Fragment(), OnMapReadyCallback {

    val args: PreviewLocationArgs by navArgs()
    private lateinit var binding: FragmentPreviewLocationBinding
    private lateinit var mPlaceViewModel: PlaceViewModel
    private lateinit var googleMap: GoogleMap
    private lateinit var image: Bitmap
    private var uri: Uri? = null

    @SuppressLint("WrongThread")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPreviewLocationBinding.inflate(layoutInflater)
        var view = binding.root

        mPlaceViewModel = ViewModelProvider(this)[PlaceViewModel::class.java]

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        binding.mapView.getMapAsync(this)

        binding.deleteButton.setOnClickListener {

            mPlaceViewModel.deletePlace(args.place)

            findNavController().navigate(R.id.home)
        }

        binding.shareButton.setOnClickListener {
            val shareIntent = Intent().apply {
                val bytes = ByteArrayOutputStream()
                image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                val path: String = Images.Media.insertImage(requireContext().contentResolver, image, "untitled", null)
                uri = Uri.parse(path)

                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "image/jepg"
            }
            startActivity(Intent.createChooser(shareIntent, null))
        }

        return view
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        image = BitmapFactory.decodeByteArray(args.place.image, 0, args.place.image.size)
        binding.imagePreview.setImageBitmap(image)
        googleMap.addMarker(MarkerOptions().position(LatLng(args.place.latitude, args.place.longitude)))
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(LatLng(args.place.latitude, args.place.longitude),
            15F
        ))

    }

    override fun onResume() {
        super.onResume()

        uri?.let {
            requireContext().contentResolver.delete(it, null, null)
            uri = null
        }
    }
}