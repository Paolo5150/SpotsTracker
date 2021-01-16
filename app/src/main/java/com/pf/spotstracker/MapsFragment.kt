package com.pf.spotstracker



import android.annotation.SuppressLint
import androidx.fragment.app.Fragment

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.pf.spotstracker.utils.LogDebug

class MapsFragment : Fragment() {

    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedClient: FusedLocationProviderClient
    private var meMarker: Marker? = null

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

      /*  val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/

        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val locationRequest = LocationRequest.create()?.apply {
            interval = 2000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult?) {
                result ?: return
                if(result.locations.isNotEmpty())
                {
                    val location =result.lastLocation
                    val latLng: LatLng = LatLng(location.latitude, location.longitude)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                    if(meMarker != null) {
                        meMarker!!.position = latLng

                    }
                    else
                    {
                        meMarker = googleMap.addMarker(MarkerOptions().position(latLng).title("I'm here"))
                    }
                    googleMap.setMinZoomPreference(13.0f)
                    googleMap.setMaxZoomPreference(22.0f)
                }
            }
        }

        fusedClient!!.requestLocationUpdates(locationRequest, locationCallback, null)

        googleMap.setOnMapClickListener {
            it -> LogDebug("Clicked on " + it.latitude + " " + it.longitude)
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