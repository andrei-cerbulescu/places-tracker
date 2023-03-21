package com.andrei.cerbulescu.placestracker.fragments

import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.andrei.cerbulescu.placestracker.databinding.FragmentCameraBinding
import com.andrei.cerbulescu.placestracker.utils.ANIMATION_FAST_MILLIS
import com.andrei.cerbulescu.placestracker.utils.ANIMATION_SLOW_MILLIS
import com.google.common.util.concurrent.ListenableFuture
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class Camera : Fragment() {
    private lateinit var binding: FragmentCameraBinding
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private lateinit var previewView : PreviewView
    private lateinit var imageCapture : ImageCapture

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(layoutInflater)
        var view = binding.root
        previewView = binding.previewView
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(requireContext()))

        binding.shutterButton.setOnClickListener{
            val name = SimpleDateFormat(System.currentTimeMillis().toString(), Locale.US)
                .format(System.currentTimeMillis())
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    val appName = requireContext().resources.getString(com.andrei.cerbulescu.placestracker.R.string.app_name)
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/${appName}")
                }
            }
            val outputFileOptions = ImageCapture.OutputFileOptions
                .Builder(requireContext().contentResolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues)
                .build()
            imageCapture.takePicture(outputFileOptions, Executors.newSingleThreadExecutor(), object: ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        // Display flash animation to indicate that photo was captured
                        binding.root.postDelayed({
                            binding.root.foreground = ColorDrawable(Color.WHITE)
                            binding.root.postDelayed(
                                { binding.root.foreground = null }, ANIMATION_FAST_MILLIS)
                        }, ANIMATION_SLOW_MILLIS)
                    }
                }

                override fun onError(exception: ImageCaptureException) {

                }

            })
        }

        return view
    }

    fun bindPreview(cameraProvider : ProcessCameraProvider){
        var preview : Preview = Preview.Builder()
            .build()

        var cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(previewView.surfaceProvider)

        imageCapture = ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY).build()

        var camera = cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview, imageCapture)

    }


}