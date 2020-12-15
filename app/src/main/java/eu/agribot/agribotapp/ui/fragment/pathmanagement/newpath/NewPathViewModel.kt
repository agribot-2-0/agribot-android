package eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath

import android.util.Log
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
import eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath.model.CameraPositionModel
import eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath.model.PointModel
import eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath.model.TempPolylineModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.InputStream

class NewPathViewModel(
    private val resourcesProvider: ResourcesProvider,
    private val filePathProvider: FilePathProvider,
    private val pathManagementService: PathManagementService,
    private val kmlLayerService: KmlLayerService,
    private val cloudDatabaseService: CloudDatabaseService
) : BaseViewModel() {
    val alreadyCreatedLayer: MutableLiveData<InputStream> = MutableLiveData()
    val selectedPointsCoordinates: MutableLiveData<List<PointModel>> = MutableLiveData(listOf())
    private val centerMapCoordinate: MutableLiveData<LatLng> = MutableLiveData()
    private val pointIndex: MutableLiveData<Int> = MutableLiveData()
    val cameraPosition: MutableLiveData<CameraPositionModel> = MutableLiveData()
    val tempPolyline: MutableLiveData<TempPolylineModel> = MutableLiveData()
    private val currentIndex: MutableLiveData<Int> = MutableLiveData()
    val toastMessage: MutableLiveEvent<String> = MutableLiveEvent()
    private val initializationDelay = 200L
    fun setCenterMapCoordinate(coordinate: LatLng) {
        centerMapCoordinate.value = coordinate
    }

    fun getMapTilePath(path: String): String {
        return filePathProvider.getMapTilePath(path)
    }

    private fun getPointIndex(): Int {
        if (pointIndex.value == null) {
            pointIndex.value = 0
        } else {
            pointIndex.value = pointIndex.value!! + 1
        }
        return pointIndex.value!!
    }

    fun onAddNewPointButtonClicked() {
        addPointToPointList(
            PointModel(
                getPointIndex(),
                CoordinatesModel(
                    latitude = centerMapCoordinate.value!!.latitude,
                    longitude = centerMapCoordinate.value!!.longitude
                )
            )
        )
        currentIndex.value = pointIndex.value
    }

    private fun addPointToPointList(newPoint: PointModel) {
        val isAttributeAlreadyAdded = selectedPointsCoordinates.value!!.any { arrtibute ->
            arrtibute.pointId == newPoint.pointId
        }
        if (!isAttributeAlreadyAdded)
            selectedPointsCoordinates.value = listOf(
                PointModel(newPoint.pointId, newPoint.coordinates),
                *selectedPointsCoordinates.value!!.toTypedArray()
            )
        else {
            selectedPointsCoordinates.value =
                selectedPointsCoordinates.value!!.map { oldAttribute ->
                    if (oldAttribute.pointId == newPoint.pointId) {
                        PointModel(
                            newPoint.pointId,
                            CoordinatesModel(
                                newPoint.coordinates.latitude,
                                newPoint.coordinates.longitude
                            )
                        )
                    } else {
                        oldAttribute
                    }
                }
        }
    }

    fun onChangePointPosition(position: LatLng) {
        if (currentIndex.value == pointIndex.value || selectedPointsCoordinates.value!!.size == 1 || currentIndex.value == null) {
            val lastPosition =
                selectedPointsCoordinates.value!!.find { it.pointId == pointIndex.value }?.coordinates
            if (lastPosition != null) {
                tempPolyline.value =
                    TempPolylineModel(
                        LatLng(lastPosition.latitude, lastPosition.longitude),
                        position
                    )
            }
        } else if (currentIndex.value != null) {
            addPointToPointList(
                PointModel(
                    currentIndex.value!!, CoordinatesModel(
                        latitude = position.latitude,
                        longitude = position.longitude
                    )
                )
            )
        }
    }

    fun onDeleteLastPointButtonClicked() {
        selectedPointsCoordinates.value =
            selectedPointsCoordinates.value?.filter { it.pointId != pointIndex.value }
        pointIndex.value = pointIndex.value!! - 1

        cameraPosition.value =
            selectedPointsCoordinates.value?.find { it.pointId == pointIndex.value }?.let {
                CameraPositionModel(
                    LatLng(it.coordinates.latitude, it.coordinates.longitude), true
                )
            }
    }

    fun onPreviousPointButtonCLicked() {
        if (currentIndex.value == null) {
            currentIndex.value = pointIndex.value
        } else {
            currentIndex.value = currentIndex.value!! - 1
        }

        if (currentIndex.value!! > pointIndex.value!! || currentIndex.value!! < 0) {
            currentIndex.value = pointIndex.value
        }

        val last =
            selectedPointsCoordinates.value!!.find { it.pointId == currentIndex.value }?.coordinates
        if (last != null) {
            cameraPosition.value = CameraPositionModel(LatLng(last.latitude, last.longitude), false)
        }

    }

    fun onNextPointButtonCLicked() {
        if (currentIndex.value == null) {
            currentIndex.value = pointIndex.value
        }
        currentIndex.value = currentIndex.value!! + 1
        if (currentIndex.value!! > pointIndex.value!! || currentIndex.value!! < 0) {
            currentIndex.value = 0
        }
        val last =
            selectedPointsCoordinates.value!!.find { it.pointId == currentIndex.value }?.coordinates
        if (last != null) {
            cameraPosition.value = CameraPositionModel(LatLng(last.latitude, last.longitude), false)
        }

    }

    fun onSavePathButtonClicked() {
        pathManagementService.handlePathFile(
            selectedPointsCoordinates.value!!.map { it.coordinates },
            onSucess = this::moveToPathManagement,
            onError = this::saveKMLError
        )
    }

    private fun saveKMLError(errorText: String) {
        toastMessage.postValue(errorText)
    }

    private fun moveToPathManagement() {
        navigationEvent.postValue(PathManagementNavigationEvent())
    }

    fun getPath() {
        if (pathManagementService.getSelectedPathLayer() != null) {
            openSelectedLayer()
        } else {
            setCameraPositionToAgribot()
        }
    }

    private fun setCameraPositionToAgribot() {
        cloudDatabaseService.getRobotLocalizationPose(
            onSuccess = this::onGotAgribotPosition,
            onError = this::getRobotLocalizationError
        )
    }

    private fun getRobotLocalizationError(getLocalizationError: String) {
        toastMessage.postValue(getLocalizationError)
    }

    private fun onGotAgribotPosition(robotLocalizationPose: RobotLocalizationPose) {
        cameraPosition.value = CameraPositionModel(
            LatLng(
                robotLocalizationPose.latitude,
                robotLocalizationPose.longitude
            ), false
        )
    }

    private fun openSelectedLayer() = viewModelScope.launch {
        delay(initializationDelay)
        if (pathManagementService.getSelectedPathLayer() != null) {
            alreadyCreatedLayer.value = pathManagementService.getSelectedPathLayer()
        }
    }

    private fun onGotCoordinates(coordinates: ArrayList<LatLng>) {
        coordinates.forEach {
            addPointToPointList(
                PointModel(getPointIndex(), CoordinatesModel(it.latitude, it.longitude))
            )
        }
        cameraPosition.value =
            selectedPointsCoordinates.value?.find { it.pointId == pointIndex.value }?.let {
                CameraPositionModel(
                    LatLng(it.coordinates.latitude, it.coordinates.longitude), false
                )
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
        toastMessage.postValue(resourcesProvider.getString(R.string.loading_path_problem))
    }
}
