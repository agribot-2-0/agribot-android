package eu.agribot.agribotapp.ui.fragment.pathmanagement.mainpathscreen

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.NewPathNavigationEvent
import eu.agribot.agribotapp.domain.service.PathManagementService

class PathManagementViewModel(
    private val pathManagementService: PathManagementService
) : BaseViewModel() {

    val agriBotLatLng = LatLng(51.11322, 17.01958)

    val markers: MutableLiveData<MarkerOptions> = MutableLiveData()

    fun addDefaultMarker() {
        markers.value = MarkerOptions().position(agriBotLatLng)
    }

    fun postNewPathNavigationEvent() {
        navigationEvent.postValue(NewPathNavigationEvent())
    }
}
