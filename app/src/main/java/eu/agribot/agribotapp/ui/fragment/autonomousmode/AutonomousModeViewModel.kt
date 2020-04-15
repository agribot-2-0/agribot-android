package eu.agribot.agribotapp.ui.fragment.autonomousmode

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import eu.agribot.agribotapp.base.BaseViewModel

class AutonomousModeViewModel : BaseViewModel() {

    val agriBotLatLng = LatLng(51.11322, 17.01958)

    fun setMap(mMap: GoogleMap){
        mMap.addMarker(MarkerOptions().position(agriBotLatLng).title("welcome"))
    }

}
