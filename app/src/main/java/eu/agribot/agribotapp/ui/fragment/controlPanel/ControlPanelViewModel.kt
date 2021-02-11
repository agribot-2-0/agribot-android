package eu.agribot.agribotapp.ui.fragment.controlPanel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.kml.KmlContainer
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.RobotLocalizationPose
import eu.agribot.agribotapp.domain.model.cdb.TaskDetails
import eu.agribot.agribotapp.domain.service.KmlLayerService
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.CloudStorageService
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePathProvider
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class ControlPanelViewModel(
    private val cloudDatabaseService: CloudDatabaseService,
    private val resourcesProvider: ResourcesProvider,
    private val cloudStorageService: CloudStorageService,
    private val kmlLayerService: KmlLayerService,
    private val filePathProvider: FilePathProvider
) : BaseViewModel() {
    private val currentTask: MutableLiveData<String> = MutableLiveData()
    val pathName: MutableLiveData<String> = MutableLiveData()
    val pathStream: MutableLiveData<InputStream> = MutableLiveData()
    val toastMessageEvent: LiveEvent<String> = MutableLiveEvent()
    val markers: MutableLiveData<MarkerOptions> = MutableLiveData()
    val cameraPosition: MutableLiveData<LatLng> = MutableLiveData()

    fun getMapTilePath(path: String): String {
        return filePathProvider.getMapTilePath(path)
    }

    fun getActualTask() {
        cloudDatabaseService.getActualTaskDetails(
            onGetCurrentTaskDetailsSucess = this::onGotCurrentTaskDetails,
            onGetCurrentTaskNameSucess = this::onGotCurrentTaskName,
            onError = this::postToastErrorMessageEvent
        )
    }

    private fun onGotCurrentTaskName(taskName: String) {
        currentTask.value = taskName
    }

    @SuppressLint("SimpleDateFormat")
    private fun onGotCurrentTaskDetails(taskDetails: TaskDetails) {
        checkIsCurrentPathExistInStorage()
    }

    fun getCoordinatesFromKml(containers: Iterable<KmlContainer?>) {
        kmlLayerService.getCoordinates(
            containers,
            onGetLatLng = this::onGotCoordinates,
            onError = this::onGetCoordinatesError
        )
    }

    private fun onGotCoordinates(coordinates: ArrayList<LatLng>) {
        cameraPosition.value = coordinates[0]
    }

    private fun onGetCoordinatesError() {
        postToastErrorMessageEvent(resourcesProvider.getString(R.string.kml_layer_problem))
    }

    private fun checkIsCurrentPathExistInStorage() {
        cloudStorageService.downloadFilesList(
            onFileAvailable = this::checkIsFileExist,
            onError = this::postToastErrorMessageEvent
        )
    }

    private fun checkIsFileExist(fileName: String) {
        if (fileName.contains(pathName.value.toString(), ignoreCase = true)) {
            try {
                cloudStorageService.onFileSelected(
                    fileName,
                    onGotFile = this::onFileAlreadyDownloaded,
                    onSelectedFileError = this::postToastErrorMessageEvent
                )
            } catch (exception: IOException) {
                postToastErrorMessageEvent(resourcesProvider.getString(R.string.kml_layer_problem))
            }
        }
    }

    private fun onFileAlreadyDownloaded(file: File) {
        val fileInputStream: InputStream = FileInputStream(file)
        pathStream.value = fileInputStream
    }

    fun getAgribotPosition() {
        cloudDatabaseService.getRobotLocalizationPose(
            onSuccess = this::setCurrentRobotPosition,
            onError = this::postToastErrorMessageEvent
        )
    }

    private fun setCurrentRobotPosition(robotLocalizationPose: RobotLocalizationPose) {
        markers.value = MarkerOptions()
            .rotation(robotLocalizationPose.heading)
            .position(
                LatLng(
                    robotLocalizationPose.latitude,
                    robotLocalizationPose.longitude
                )
            )
    }

    private fun postToastErrorMessageEvent(errorMessage: String) {
        toastMessageEvent.postValue(errorMessage)
    }
}
