package eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath.model

import com.google.android.gms.maps.model.LatLng

data class TempPolylineModel(
    val oldPositin: LatLng,
    val newPosition: LatLng
)
