package eu.agribot.agribotapp.ui.fragment.autonomousmode


import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import eu.agribot.agribotapp.base.BaseViewModel

class AutonomousModeViewModel : BaseViewModel() {

    val agriBotLatLng = LatLng(51.11322, 17.01958)

    val markers: MutableLiveData<MarkerOptions> = MutableLiveData()

    fun addDefaultMarker() {
        markers.value = MarkerOptions().position(agriBotLatLng)
    }
}
