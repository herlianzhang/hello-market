package com.dpr.hello_market.ui.choose_location

import android.app.Application
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import com.dpr.hello_market.vo.Location
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import javax.inject.Inject

class ChooseLocationViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    lateinit var googleMap: GoogleMap
    lateinit var geoCoder: Geocoder
    var homeMarker: Marker? = null
    lateinit var myLocation: Location
}