package eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.DashboardNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.CoordinatesModel
import eu.agribot.agribotapp.domain.model.cdb.RobotLocalizationPose
import eu.agribot.agribotapp.domain.service.PathManagementService
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePathProvider

class NewPathViewModel(
    private val resourcesProvider: ResourcesProvider,
    private val filePathProvider: FilePathProvider,
    private val cloudDatabaseService: CloudDatabaseService,
    private val pathManagementService: PathManagementService
) : BaseViewModel() {
    val cameraPosition: MutableLiveData<LatLng> = MutableLiveData()
    private val currentPoint: MutableLiveData<LatLng> = MutableLiveData()
    val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val selectedPoints: MutableLiveData<LatLng> = MutableLiveData()
    private val selectedPointsList: MutableLiveData<List<CoordinatesModel>> =
        MutableLiveData(listOf())
    val postInputDialogEvent: MutableLiveEvent<Boolean> = MutableLiveEvent()
    private val fileNameSuffix = "_v"

    fun getMapTilePath(path: String): String {
        return filePathProvider.getMapTilePath(path)
    }

    fun getAgribotPosition() {
        cloudDatabaseService.getRobotLocalizationPose(
            onSuccess = this::setCurrentRobotPosition,
            onError = this::postErrorToastEvent
        )
    }

    private fun setCurrentRobotPosition(robotLocalizationPose: RobotLocalizationPose) {
        cameraPosition.value = LatLng(
            robotLocalizationPose.latitude,
            robotLocalizationPose.longitude
        )
    }

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

    fun onCoordinatesChanged(coordinates: LatLng) {
        currentPoint.value = coordinates
    }

    fun onGetPointerButtonClicked() {
        onPointSelected(currentPoint.value!!)
    }

    private fun onPointSelected(coordinates: LatLng) {
        selectedPoints.value = coordinates
        onGetCoordinate(coordinates = CoordinatesModel(coordinates))
    }

    fun onSavePathClicked() {
        postInputDialogEvent.postValue(true)
    }

    fun createNewLayer(layerName: String) {
        pathManagementService.createPathFile(
            selectedPointsList.value!!,
            layerName + fileNameSuffix,
            onSucess = this::postSucessToastEvent,
            onError = this::postErrorToastEvent
        )
    }

    private fun postSucessToastEvent() {
        toastMessageEvent.postValue(resourcesProvider.getString(R.string.create_layer_sucess))
        navigationEvent.postValue(DashboardNavigationEvent())
    }

    private fun postErrorToastEvent(error: String) {
        toastMessageEvent.postValue(resourcesProvider.getString(R.string.create_layer_error))
    }
}
