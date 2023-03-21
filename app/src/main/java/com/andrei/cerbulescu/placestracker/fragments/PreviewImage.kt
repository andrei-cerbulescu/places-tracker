package com.andrei.cerbulescu.placestracker.fragments

import android.annotation.SuppressLint
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.andrei.cerbulescu.placestracker.databinding.FragmentPreviewImageBinding


class PreviewImage : Fragment() {
    val args: PreviewImageArgs by navArgs()
    private lateinit var binding: FragmentPreviewImageBinding

    @SuppressLint("WrongThread")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPreviewImageBinding.inflate(layoutInflater)
        var view = binding.root

        var imagePreview = binding.imagePreview
        var bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, args.imageUri)
        } else {
            val source: ImageDecoder.Source = ImageDecoder.createSource(requireContext().contentResolver, args.imageUri)
            ImageDecoder.decodeBitmap(source)
        }
        imagePreview.setImageBitmap(bitmap)

        // it.addMarker(MarkerOptions().position(LatLng(args.location.latitude, args.location.longitude)).title("Photo Location"))
        //AppDatabase.getDatabase(requireContext()).placeDao().getAll()
        return view
    }

}