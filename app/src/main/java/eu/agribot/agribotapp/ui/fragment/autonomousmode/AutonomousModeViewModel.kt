package eu.agribot.agribotapp.ui.fragment.autonomousmode

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.domain.service.AutonomusModeService
import eu.agribot.agribotapp.domain.service.DiagnosticDataService

class AutonomousModeViewModel(
    private val autonomusModeService: AutonomusModeService,
    diagnosticDataService: DiagnosticDataService
) :
    BaseViewModel() {

    private val agriBotLatLng = LatLng(51.11322, 17.01958)
    val showToastEvent: LiveEvent<String> = MutableLiveEvent()

    val markers: MutableLiveData<MarkerOptions> = MutableLiveData()

    fun addDefaultMarker() {
        markers.value = MarkerOptions().position(agriBotLatLng)
    }

    fun sendInitAutoMode() {
        autonomusModeService.runAutonomusMode(this::postValueAgribotDisconnected)
    }

    private fun postValueAgribotDisconnected() {
        showToastEvent.postValue("Agribot Disconnected")
    }
}
