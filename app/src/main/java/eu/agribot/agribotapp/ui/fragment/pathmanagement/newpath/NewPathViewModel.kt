package eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.kml.KmlContainer
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.DashboardNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.CoordinatesModel
import eu.agribot.agribotapp.domain.model.cdb.RobotLocalizationPose
import eu.agribot.agribotapp.domain.service.KmlLayerService
import eu.agribot.agribotapp.domain.service.PathManagementService
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePathProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.InputStream

class NewPathViewModel(
    private val resourcesProvider: ResourcesProvider,
    private val filePathProvider: FilePathProvider,
    private val cloudDatabaseService: CloudDatabaseService,
    private val pathManagementService: PathManagementService,
    private val kmlLayerService: KmlLayerService
) : BaseViewModel() {
    val cameraPosition: MutableLiveData<CameraPosition> = MutableLiveData()
    private val currentPoint: MutableLiveData<CoordinatesModel> = MutableLiveData()
    val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val selectedPointsList: MutableLiveData<List<CoordinatesModel>> =
        MutableLiveData(listOf())
    val switchStatus: MutableLiveData<Boolean> = MutableLiveData()
    val selectedLayer: MutableLiveData<InputStream> = MutableLiveData()
    private val lastPointPosition = 0
    val markerPosition: MutableLiveData<LatLng> = MutableLiveData()
     val marker: MutableLiveData<Marker> = MutableLiveData()
    private val markerIndex: MutableLiveData<Int> = MutableLiveData()


    fun setSwitchStatus(isSwitched: Boolean) {
        switchStatus.value = isSwitched
    }

    fun onMarkerPositionChanged(markerPosition: LatLng) {
        this.markerPosition.value = markerPosition
    }

    fun setMarker(marker: Marker?) {
        this.marker.value = marker
        selectedPointsList.value?.forEach {
            if (LatLng(it.latitude, it.longitude) == marker?.position) {
                markerIndex.value = selectedPointsList.value!!.indexOf(
                    CoordinatesModel(
                        it.latitude,
                        it.longitude
                    )
                )

            }
        }
    }

    fun onChangeMarkerPosition(newPosition: CoordinatesModel) {
        if (marker.value != null) {
            selectedPointsList.value =
                selectedPointsList.value!!.mapIndexed { index, coordinatesModel ->
                    if (index == markerIndex.value) {
                        newPosition
                    } else {
                        coordinatesModel
                    }
                }
        }

    }


    fun getMapTilePath(path: String): String {
        return filePathProvider.getMapTilePath(path)

    }

    fun deleteLastPoint() {
        if (selectedPointsList.value!!.size > 1) {
            selectedPointsList.value =
                selectedPointsList.value!!.filterIndexed() { index, _ ->
                    index != lastPointPosition
                }
            cameraPosition.value =
                CameraPosition(
                    LatLng(
                        selectedPointsList.value!![lastPointPosition].latitude,
                        selectedPointsList.value!![lastPointPosition].longitude
                    ), true
                )
        }
    }

    fun getPath() {
        if (pathManagementService.getSelectedPathLayer() != null) {
            openSelectedLayer()
        }
    }

    private fun openSelectedLayer() = viewModelScope.launch {
        delay(200)
        if (pathManagementService.getSelectedPathLayer() != null) {
            selectedLayer.value = pathManagementService.getSelectedPathLayer()
        }
    }

    fun getCoordinatesFromKml(containers: Iterable<KmlContainer?>) {
        kmlLayerService.getCoordinates(
            containers,
            onGetLatLng = this::onGotCoordinates,
            onError = this::onErrror
        )
    }

    private fun onErrror() {

    }

    private fun onGotCoordinates(coordinates: ArrayList<LatLng>) {

        selectedPointsList.value =
            coordinates.map {
                CoordinatesModel(
                    latitude = it.latitude,
                    longitude = it.longitude
                )
            }
        cameraPosition.value = CameraPosition(
            LatLng(
                coordinates[0].latitude,
                coordinates[0].longitude
            ), false
        )
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
        selectedPointsList.value!!.any { foundCoordinates ->
            foundCoordinates != coordinates
        }.let {
            selectedPointsList.value = listOf(
                CoordinatesModel(
                    coordinates.latitude,
                    coordinates.longitude
                ), *selectedPointsList.value!!.toTypedArray()
            )
        }
    }

    fun onCoordinatesChanged(coordinates: LatLng) {
        currentPoint.value = CoordinatesModel(coordinates.latitude, coordinates.longitude)
    }

    fun onGetPointerButtonClicked() {
        onPointSelected(LatLng(currentPoint.value!!.latitude, currentPoint.value!!.longitude))
    }

    private fun onPointSelected(coordinates: LatLng) {
        onGetCoordinate(
            coordinates = CoordinatesModel(
                latitude = coordinates.latitude,
                longitude = coordinates.longitude
            )
        )
    }

    fun onSavePathClicked() {
        createNewLayer()
    }

    fun createNewLayer() {
        pathManagementService.handlePathFile(
            selectedPointsList.value!!,
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
