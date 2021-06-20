package com.pf.spotstracker

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.VectorDrawable
import android.os.Build
import androidx.fragment.app.Fragment

import android.os.Bundle

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.maps.android.MarkerManager
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.pf.spotstracker.utils.LogDebug

class MapsFragment : Fragment(),
        GoogleMap.OnPoiClickListener,
        GoogleMap.InfoWindowAdapter,
ClusterManager.OnClusterClickListener<POIClusterItem>,
ClusterManager.OnClusterItemClickListener<POIClusterItem>,
ClusterManager.OnClusterItemInfoWindowClickListener<POIClusterItem>{

    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedClient: FusedLocationProviderClient
    private var meMarker: Marker? = null
    private var focusedOnMe = false
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var markersList: MutableList<Marker>
    private lateinit var clusterManager: ClusterManager<POIClusterItem>


    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

        mGoogleMap = googleMap
        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        clusterManager = ClusterManager<POIClusterItem>(MainActivity.getMainActivity(), mGoogleMap)
        markersList = mutableListOf()


        mGoogleMap.isMyLocationEnabled = true


        fusedClient.lastLocation.addOnSuccessListener {
            if(!focusedOnMe) {
                val latLng: LatLng = LatLng(it.latitude, it.longitude)

                var targetPos:CameraPosition = CameraPosition.builder()
                    .target(latLng)
                    .zoom(18.0f)
                    .build()
                var camUpdate:CameraUpdate = CameraUpdateFactory.newCameraPosition(targetPos)
                mGoogleMap.animateCamera(camUpdate, 4000, null)
                focusedOnMe = true
            }
        }

        //google map listeners
        mGoogleMap.setOnPoiClickListener(this)
        mGoogleMap.setOnMarkerClickListener(clusterManager)
        mGoogleMap.setInfoWindowAdapter(clusterManager.markerManager)
        mGoogleMap.setOnInfoWindowClickListener(clusterManager)

        mGoogleMap.setOnMapClickListener {
            it -> LogDebug("Clicked on " + it.latitude + " " + it.longitude)

           /* val q = Volley.newRequestQueue(MainActivity.getMainActivity())
            val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +it.latitude + ","+ it.longitude + "&type=restaurant&radius=2&sensor=false&key=" + BuildConfig.ApiKey

            val stringRequest = StringRequest(
                    Request.Method.GET, url,
                    Response.Listener<String> { response ->
                        // Display the first 500 characters of the response string.
                        LogDebug(response)

                    },
                    Response.ErrorListener { LogDebug("Request failed") })

// Add the request to the RequestQueue.
            q.add(stringRequest)*/

        }

        mGoogleMap.setOnMapLongClickListener {
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



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onPoiClick(p0: PointOfInterest?) {

        // Custom marker
        /*val height = 180
        val width = 200
        val b = MainActivity.getMainActivity()?.getDrawable(R.drawable.ic_map_pin)!!.toBitmap(width,height)
        val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)

        var m = mGoogleMap.addMarker(
                MarkerOptions().position(p0!!.latLng)
                        .title(p0!!.name)
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
        )
        m.showInfoWindow()
        //Add to list
        markersList.add(m)*/
        var poiClusterItem = POIClusterItem()
        poiClusterItem.latitude = p0!!.latLng.latitude
        poiClusterItem.longitude = p0!!.latLng.longitude
        clusterManager.addItem(poiClusterItem)

        clusterManager.cluster()

        //Move to marker
        var targetPos:CameraPosition = CameraPosition.builder()
                .target(p0!!.latLng)
                .zoom(18.0f)
                .build()
        var camUpdate:CameraUpdate = CameraUpdateFactory.newCameraPosition(targetPos)
        mGoogleMap.animateCamera(camUpdate, 1000, null)
    }

    override fun getInfoWindow(p0: Marker?): View {

        var v = layoutInflater.inflate(R.layout.marker_info, null)

        var t = v.findViewById<TextView>(R.id.marker_info_text)
        t!!.text = p0!!.title
        return v
    }

    override fun getInfoContents(p0: Marker?): View {
        TODO("Not yet implemented")
    }

    // Cluser callbacks
    override fun onClusterClick(p0: Cluster<POIClusterItem>?): Boolean {
        TODO("Not yet implemented")
        return false
    }

    override fun onClusterItemClick(p0: POIClusterItem?): Boolean {
        TODO("Not yet implemented")
        return false

    }

    override fun onClusterItemInfoWindowClick(p0: POIClusterItem?) {
        TODO("Not yet implemented")


    }


}


