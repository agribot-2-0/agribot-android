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
    val currentCoordinate: MutableLiveData<LatLng> = MutableLiveData()
    val cameraPosition: MutableLiveData<CameraPosition> = MutableLiveData()
    private val currentPoint: MutableLiveData<CoordinatesModel> = MutableLiveData()
    val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val selectedPointsList: MutableLiveData<List<CoordinatesModel>> =
        MutableLiveData(listOf())
    val postInputDialogEvent: MutableLiveEvent<Boolean> = MutableLiveEvent()
    private val lastPointPosition = 0

    fun setCurrentCoordinate(coordinates: LatLng) {
        currentCoordinate.value = coordinates
    }

    fun getMapTilePath(path: String): String {
        return filePathProvider.getMapTilePath(path)
    }

    fun deleteLastPoint() {
        if (selectedPointsList.value!!.size > 1) {
            selectedPointsList.value =
                selectedPointsList.value?.minus(
                    CoordinatesModel(
                        selectedPointsList.value!![lastPointPosition].latitude,
                        selectedPointsList.value!![lastPointPosition].longtitude
                    )
                )
            cameraPosition.value =
                CameraPosition(
                    LatLng(
                        selectedPointsList.value!![lastPointPosition].latitude,
                        selectedPointsList.value!![lastPointPosition].longtitude
                    ), true
                )
        }
    }

    fun getAgribotPosition() {
        cloudDatabaseService.getRobotLocalizationPose(
            onSuccess = this::setCurrentRobotPosition,
            onError = this::postErrorToastEvent
        )
    }

    private fun setCurrentRobotPosition(robotLocalizationPose: RobotLocalizationPose) {
        cameraPosition.value = CameraPosition(
            LatLng(
                robotLocalizationPose.latitude,
                robotLocalizationPose.longitude
            ), false
        )
    }

    private fun onGetCoordinate(coordinates: CoordinatesModel) {
        val isCoordinatesAlreadyAdded = selectedPointsList.value!!.any { foundCoordinates ->
            foundCoordinates == coordinates
        }
        if (!isCoordinatesAlreadyAdded) {
            selectedPointsList.value = listOf(
                CoordinatesModel(
                    coordinates.latitude,
                    coordinates.longtitude
                ), *selectedPointsList.value!!.toTypedArray()
            )
        }
    }

    fun onCoordinatesChanged(coordinates: LatLng) {
        currentPoint.value = CoordinatesModel(coordinates.latitude, coordinates.longitude)
    }

    fun onGetPointerButtonClicked() {
        onPointSelected(LatLng(currentPoint.value!!.latitude, currentPoint.value!!.longtitude))
    }

    private fun onPointSelected(coordinates: LatLng) {
        onGetCoordinate(
            coordinates = CoordinatesModel(
                latitude = coordinates.latitude,
                longtitude = coordinates.longitude
            )
        )
    }

    fun onSavePathClicked() {
        postInputDialogEvent.postValue(true)
    }

    fun createNewLayer(layerName: String) {
        pathManagementService.createPathFile(
            selectedPointsList.value!!,
            layerName,
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
