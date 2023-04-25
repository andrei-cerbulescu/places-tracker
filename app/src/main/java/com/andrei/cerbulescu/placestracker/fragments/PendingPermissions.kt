package com.andrei.cerbulescu.placestracker.fragments

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.PermissionChecker
import androidx.navigation.fragment.findNavController
import com.andrei.cerbulescu.placestracker.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PendingPermissions.newInstance] factory method to
 * create an instance of this fragment.
 */
class PendingPermissions : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            get_permissions()
        }
        else{
            findNavController().popBackStack(R.id.home, false, false)
            findNavController().navigate(R.id.home)
        }
        return inflater.inflate(R.layout.fragment_pending_permissions, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun get_permissions() {
        var toBeRequested = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.POST_NOTIFICATIONS
        )
        var needsConfirmation = mutableListOf<String>()

        for (e in toBeRequested) {
            if (PermissionChecker.checkSelfPermission(
                    requireContext(),
                    e
                ) != PermissionChecker.PERMISSION_GRANTED
            ) {
                needsConfirmation.add(e)
            }
        }

        if (needsConfirmation.size > 0) {
            requestPermissions(needsConfirmation.toTypedArray(), 101)
        }else{
            findNavController().popBackStack(R.id.home, false, false)
            findNavController().navigate(R.id.home)
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
                if(it!= PackageManager.PERMISSION_GRANTED){
                    get_permissions()
                    return
                }
            }
        }
        findNavController().popBackStack(R.id.home, false, false)
        findNavController().navigate(R.id.home)
    }

}
