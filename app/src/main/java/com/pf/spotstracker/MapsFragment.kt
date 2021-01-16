package com.pf.spotstracker



import android.annotation.SuppressLint
import androidx.fragment.app.Fragment

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.pf.spotstracker.utils.LogDebug

class MapsFragment : Fragment() {

    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedClient: FusedLocationProviderClient
    private var meMarker: Marker? = null
    private var focusedOnMe = false

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

        googleMap.isMyLocationEnabled = true
        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        fusedClient!!.lastLocation.addOnSuccessListener {
            if(!focusedOnMe) {
                val latLng: LatLng = LatLng(it.latitude, it.longitude)

                var targetPos:CameraPosition = CameraPosition.builder()
                    .target(latLng)
                    .zoom(18.0f)
                    .build()
                var camUpdate:CameraUpdate = CameraUpdateFactory.newCameraPosition(targetPos)
                googleMap.animateCamera(camUpdate, 4000, null)
                focusedOnMe = true
            }
        }

        googleMap.setOnMapClickListener {
            it -> LogDebug("Clicked on " + it.latitude + " " + it.longitude)
        }

        googleMap.setOnMapLongClickListener {
                it -> LogDebug("Long clicked on " + it.latitude + " " + it.longitude)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedClient.removeLocationUpdates(locationCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.menu_map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}