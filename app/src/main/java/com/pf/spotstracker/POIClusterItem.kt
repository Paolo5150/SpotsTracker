package com.pf.spotstracker

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

public class POIClusterItem : ClusterItem {

    var latitude = 0.0
    var longitude = 0.0

    override fun getPosition(): LatLng {
        return LatLng(latitude, longitude)
    }
}