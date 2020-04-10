package eu.agribot.agribotapp.ui.fragment.pathmanagement

import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.domain.service.PathManagementService

class PathManagementViewModel(
    private val pathManagementService: PathManagementService
) : BaseViewModel(){
    companion object { var mapFragment : SupportMapFragment?=null }
    val agriBotLatLng = LatLng(51.11322, 17.01958)
    val zoom = 15F


}
