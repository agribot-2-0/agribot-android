package eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.kml.KmlContainer
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.PathManagementNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.CoordinatesModel
import eu.agribot.agribotapp.domain.model.cdb.RobotLocalizationPose
import eu.agribot.agribotapp.domain.service.KmlLayerService
import eu.agribot.agribotapp.domain.service.PathManagementService
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePathProvider
import eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath.model.CameraPosition
import eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath.model.PointModel
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
    val pointIndex: MutableLiveData<Int> = MutableLiveData()
    val cameraPosition: MutableLiveData<CameraPosition> = MutableLiveData()
    val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()
    private val pointID: MutableLiveData<Int> = MutableLiveData()
    val selectedLayer: MutableLiveData<InputStream> = MutableLiveData()

    val addedPointList: MutableLiveData<List<PointModel>> = MutableLiveData(listOf())
    val centerMapPosition: MutableLiveData<CoordinatesModel> = MutableLiveData()
    fun setCameraPositionToAgribot() {
        cloudDatabaseService.getRobotLocalizationPose(
            onSuccess = this::onGotAgribotPosition,
            onError = this::postErrorToastEvent
        )
    }

    private fun onGotAgribotPosition(robotLocalizationPose: RobotLocalizationPose) {
        cameraPosition.value = CameraPosition(
            LatLng(
                robotLocalizationPose.latitude,
                robotLocalizationPose.longitude
            ), false
        )
    }

    private fun postErrorToastEvent(error: String) {
        toastMessageEvent.postValue(resourcesProvider.getString(R.string.create_layer_error))
    }

    fun onNextPointButtonCLicked() {
        pointIndex.value = pointIndex.value?.plus(1)
        if (pointIndex.value!! > pointID.value!! || pointIndex.value!! < 0) {
            pointIndex.value = 0
        }

        addedPointList.value!!.forEach {
            if (it.pointId == pointIndex.value)
                cameraPosition.value =
                    CameraPosition(LatLng(it.coordinates.latitude, it.coordinates.longitude), false)
        }
    }

    fun onPreviousPointButtonCLicked() {
        if (pointIndex.value!! > pointID.value!! || pointIndex.value!! < 0) {
            pointIndex.value = pointID.value!!
        }
        pointIndex.value = pointIndex.value?.minus(1)

        addedPointList.value!!.forEach {
            if (it.pointId == pointIndex.value)
                cameraPosition.value =
                    CameraPosition(LatLng(it.coordinates.latitude, it.coordinates.longitude), false)
        }
    }

    fun onChangePointerPosition(cameraPosition: CoordinatesModel) {
        pointIndex.value?.let {
            addedPointList.value = addedPointList.value!!.map { pointer ->
                if (pointer.pointId == pointIndex.value) {
                    PointModel(
                        pointIndex.value!!,
                        cameraPosition
                    )
                } else {
                    pointer
                }
            }
        }
    }

    fun onDeleteLastPointButtonClicked() {
    }

    fun onAddNewPointButtonClicked() {
        addPointToPointList(PointModel(getPointID(), centerMapPosition.value!!))
    }

    fun onSavePathButtonClicked() {
        pathManagementService.handlePathFile(addedPointList.value!!.map {
            CoordinatesModel(
                latitude = it.coordinates.latitude,
                longitude = it.coordinates.longitude
            )
        }, onSucess = this::onSavePathSucess, onError = this::postErrorToastEvent)
    }

    private fun onSavePathSucess() {
        navigationEvent.postValue(PathManagementNavigationEvent())
    }

    fun getMapTilePath(path: String): String {
        return filePathProvider.getMapTilePath(path)
    }

    fun setCenterCameraPosition(cameraPosition: LatLng) {
        centerMapPosition.value =
            CoordinatesModel(cameraPosition.latitude, cameraPosition.longitude)
    }

    private fun getPointID(): Int {
        if (pointID.value == null) {
            pointID.value = 0
        } else {
            pointID.value = pointID.value!! + 1
        }
        pointIndex.value = pointID.value
        return pointID.value!!
    }

    private fun addPointToPointList(newPoint: PointModel) {
        addedPointList.value!!.any { alreadyAddedPoint -> alreadyAddedPoint.pointId != newPoint.pointId }
            .let {
                addedPointList.value = listOf(
                    PointModel(
                        newPoint.pointId,
                        CoordinatesModel(
                            newPoint.coordinates.latitude,
                            newPoint.coordinates.longitude
                        )
                    ), *addedPointList.value!!.toTypedArray()
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

    fun getCoordinatesFromAlreadyCreatedPath(containers: Iterable<KmlContainer?>) {
        kmlLayerService.getCoordinates(
            containers,
            onGetLatLng = this::onGotCoordinates,
            onError = this::onGetKmlUnsuccess
        )
    }

    private fun onGetKmlUnsuccess() {
        toastMessageEvent.postValue("problem")
    }

    private fun onGotCoordinates(coordinates: ArrayList<LatLng>) {
        coordinates.forEach {
            addPointToPointList(
                PointModel(getPointID(), CoordinatesModel(it.latitude, it.longitude))
            )
        }
        cameraPosition.value = CameraPosition(
            LatLng(
                coordinates[0].latitude,
                coordinates[0].longitude
            ), false
        )
    }
}
