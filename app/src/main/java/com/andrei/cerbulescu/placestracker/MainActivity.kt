package com.andrei.cerbulescu.placestracker

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            get_permissions()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun get_permissions(){
        var toBeRequested = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        var needsConfirmation = mutableListOf<String>()

        for(e in toBeRequested){
            if(checkSelfPermission(e) != PackageManager.PERMISSION_GRANTED){
                needsConfirmation.add(e)
            }
        }

        if(needsConfirmation.size > 0){
            requestPermissions(needsConfirmation.toTypedArray(), 101)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            grantResults.forEach {
                if(it!=PackageManager.PERMISSION_GRANTED){
                    get_permissions()
                }
            }
        }
    }
}