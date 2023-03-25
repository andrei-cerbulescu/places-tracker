package com.andrei.cerbulescu.placestracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.andrei.cerbulescu.placestracker.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navigator: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        navigator = binding.navHostFragment.getFragment<NavHostFragment>().navController
        supportActionBar?.hide();
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            get_permissions()
        }

        binding.HomeButton.setOnClickListener{
            navigator.popBackStack(R.id.home, true, false)
            navigator.navigate(R.id.home)
        }

        binding.CameraButton.setOnClickListener{
            navigator.navigate(R.id.camera)
        }

        binding.SearchButton.setOnClickListener{
            navigator.navigate(R.id.search)
        }


        navigator.addOnDestinationChangedListener{ navController: NavController, navDestination: NavDestination, bundle: Bundle? ->
            var buttons = mutableListOf(binding.CameraButton, binding.HomeButton, binding.SearchButton)
            for(button in buttons){
                button.setBackgroundColor(Color.DKGRAY)
            }

            if(navDestination.id == R.id.home) {
                binding.HomeButton.setBackgroundColor(Color.LTGRAY)
            }

            if(navDestination.id == R.id.camera){
                binding.CameraButton.setBackgroundColor(Color.LTGRAY)
            }

            if(navDestination.id == R.id.search){
                binding.SearchButton.setBackgroundColor(Color.LTGRAY)
            }

        }

        setContentView(view)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun get_permissions(){
        var toBeRequested = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.POST_NOTIFICATIONS
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

    override fun onPause() {
        super.onPause()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                "places-tracker-notification",
                "PlacesTracker Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.camera)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(this, "places-tracker-notification")
            .setContentText("Why don't you make a memory?")
            .setContentTitle("Heey")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(1, notification)
    }

}