package com.andrei.cerbulescu.placestracker.fragments

import android.annotation.SuppressLint
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
import androidx.navigation.fragment.navArgs
import com.andrei.cerbulescu.placestracker.data.AppDatabase
import com.andrei.cerbulescu.placestracker.data.Place
import com.andrei.cerbulescu.placestracker.data.PlaceViewModel
import com.andrei.cerbulescu.placestracker.databinding.FragmentPreviewImageBinding
import java.io.ByteArrayOutputStream


class PreviewImage : Fragment() {
    val args: PreviewImageArgs by navArgs()
    private lateinit var binding: FragmentPreviewImageBinding
    private lateinit var mPlaceViewModel: PlaceViewModel

    @SuppressLint("WrongThread")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPreviewImageBinding.inflate(layoutInflater)
        var view = binding.root

        mPlaceViewModel = ViewModelProvider(this)[PlaceViewModel::class.java]

        var imagePreview = binding.imagePreview
        var bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, args.imageUri)
        } else {
            val source: ImageDecoder.Source = ImageDecoder.createSource(requireContext().contentResolver, args.imageUri)
            ImageDecoder.decodeBitmap(source)
        }
        imagePreview.setImageBitmap(bitmap)

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val place = Place(0, args.location.latitude.toLong(), args.location.longitude.toLong(), stream.toByteArray())
        // AppDatabase.getDatabase(requireContext()).placeDao()
        mPlaceViewModel.addPlace(place)
        return view
    }

}