package eu.agribot.agribotapp.domain.service

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.kml.KmlContainer
import com.google.maps.android.kml.KmlLineString
import com.google.maps.android.kml.KmlPlacemark

class KmlLayerService {

    fun getCoordinates(
        containers: Iterable<KmlContainer?>,
        onGetLatLng: (coordinates: ArrayList<LatLng>) -> Unit,
        onError: () -> Unit
    ) {
        containers.filterNotNull().forEach { container ->
            if (container.hasContainers()) {
                getCoordinates(container.containers, onGetLatLng, onError)
            } else {
                if (container.hasPlacemarks()) {
                    accessPlacemarks(container.placemarks, onGetLatLng, onError)
                }
            }
        }
    }

    private fun accessPlacemarks(
        placemarks: Iterable<KmlPlacemark?>,
        onGetLatLng: (coordinates: ArrayList<LatLng>) -> Unit,
        onError: () -> Unit
    ) {
        placemarks.filterNotNull().forEach { placemark ->
            try {
                val kmlLineString = placemark.geometry as KmlLineString
                onGetLatLng(kmlLineString.geometryObject)
            } catch (exception: Exception) {
                onError.invoke()
            }
        }
    }
}
