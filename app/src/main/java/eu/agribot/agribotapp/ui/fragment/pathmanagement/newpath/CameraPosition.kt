package eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath

import com.google.android.gms.maps.model.LatLng

data class CameraPosition(
    val coordinates: LatLng,
    val isPointDeleted: Boolean
)