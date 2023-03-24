package com.andrei.cerbulescu.placestracker

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.NavGraph
import androidx.navigation.Navigator
import androidx.navigation.fragment.NavHostFragment
import com.andrei.cerbulescu.placestracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        supportActionBar?.hide();
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            get_permissions()
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