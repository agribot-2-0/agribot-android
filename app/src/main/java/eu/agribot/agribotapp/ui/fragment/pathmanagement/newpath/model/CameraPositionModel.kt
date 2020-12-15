package eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath.model

import com.google.android.gms.maps.model.LatLng

data class CameraPositionModel(
    val coordinates: LatLng,
    val isPointDeleted: Boolean
)
