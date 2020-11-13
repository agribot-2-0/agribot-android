package eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.CoordinatesModel
import eu.agribot.agribotapp.domain.service.PathManagementService
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePathProvider

class NewPathViewModel(
    private val resourcesProvider: ResourcesProvider,
    private val filePathProvider: FilePathProvider,
    private val pathManagementService: PathManagementService
) : BaseViewModel() {

    val selectedPoints: MutableLiveData<LatLng> = MutableLiveData()
    val selectedPointsList: MutableLiveData<List<CoordinatesModel>> = MutableLiveData(listOf())

    private fun onGetCoordinate(coordinates: CoordinatesModel) {
        val isCoordinatesAlreadyAdded = selectedPointsList.value!!.any { foundCoordinates ->
            foundCoordinates.coordinates == coordinates.coordinates
        }
        if (!isCoordinatesAlreadyAdded) {
            selectedPointsList.value = listOf(
                CoordinatesModel(
                    coordinates.coordinates
                ), *selectedPointsList.value!!.toTypedArray()
            )
        }
    }


    fun onPointSelected(coordinates: LatLng) {
        selectedPoints.value = coordinates
        onGetCoordinate(coordinates = CoordinatesModel(coordinates))
    }


    fun onSavePathClicked() {
        pathManagementService.createPathFile(selectedPointsList.value!!, "fajnaScieszgaXDDD")
    }


}
