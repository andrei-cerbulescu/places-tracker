package com.andrei.cerbulescu.placestracker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val cameraButton : Button = view.findViewById(R.id.camera_button)
        cameraButton.setOnClickListener{
            val cameraFragment = CameraFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.navigator, cameraFragment)?.commit()
        }
        return view
    }
}