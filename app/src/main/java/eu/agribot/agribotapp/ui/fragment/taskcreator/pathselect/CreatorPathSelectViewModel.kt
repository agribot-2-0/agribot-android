package eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskTimeNavigationEvent
import eu.agribot.agribotapp.domain.model.RobotLocalizationPose
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.CloudStorageService
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager
import eu.agribot.agribotapp.ui.fragment.autonomousmode.recycler.CloudFileListItemViewModel
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class CreatorPathSelectViewModel(
    private val cloudStorageService: CloudStorageService,
    private val cloudDatabaseService: CloudDatabaseService,
    private val taskCreatorDataManager: TaskCreatorDataManager
) : BaseViewModel() {
    val availableFiles: MutableLiveData<List<CloudFileListItemViewModel>> =
        MutableLiveData(listOf())
    val robotPathName: MutableLiveData<String> = MutableLiveData()
    val markers: MutableLiveData<MarkerOptions> = MutableLiveData()
    val selectedLayer: MutableLiveData<InputStream> = MutableLiveData()
    var isPathSelected = false
    val toastEvent: MutableLiveEvent<String> = MutableLiveEvent()

    private fun onAvailableFileFound(fileName: String) {
        val isFileAlreadyAdded = availableFiles.value!!.any {
            it.fileName == fileName
        }
        if (!isFileAlreadyAdded) {
            availableFiles.value = listOf(
                CloudFileListItemViewModel(
                    fileName
                ), *availableFiles.value!!.toTypedArray()
            )
        }
    }

    fun checkPathSelected() {
        isPathSelected = taskCreatorDataManager.getPathName() != null
        Log.e("xd", "Path : " + taskCreatorDataManager.getPathName())
    }

    fun downloadAvailableFileList() {
        cloudStorageService.downloadFilesList(
            onFileAvailable = this::onAvailableFileFound,
            onError = this::onError
        )
    }

    fun downloadSelectedFile(fileName: String) {
        isPathSelected = true
        val withoutKmL = fileName.replace(".kml", "")
        val withoutseparatot = withoutKmL.split("_")
        robotPathName.value = withoutseparatot.component1()
        try {
            cloudStorageService.downloadFile(
                fileName,
                onFileAlreadyDownloaded = this::onFileAlreadyDownloaded,
                onDownloadSucess = this::onFileAlreadyDownloaded
            )
        } catch (exception: IOException) {
            Log.e("xd", "exception  " + exception)
        }
    }

    fun addTileToMap() {
       //mapService.addTileOverlay(onMapException = this::onError)
    }

    private fun onFileAlreadyDownloaded(file: File) {
        val fileInputStream: InputStream = FileInputStream(file)
        selectedLayer.value = fileInputStream
    }

    private fun onError(error: String) {
    }

    fun getAgribotPosition() {
        cloudDatabaseService.getFirstRobotLocalizationPose(
            onSuccess = this::setCurrentRobotPosition,
            onError = this::onError
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

    fun setSavedLayer() {
        if (taskCreatorDataManager.getPathName() != null) {
            val fileInputStream: InputStream = FileInputStream(taskCreatorDataManager.getPathName())
            selectedLayer.value = fileInputStream
        }
    }

    fun onNextButtonClicked() {
        if (isPathSelected) {
            navigationEvent.postValue(
                CreatorTaskTimeNavigationEvent()
            )
        } else {
            toastEvent.postValue("No path file selected")
        }
    }

    fun setupMap(googleMap: GoogleMap) {
        //mapService.setupMap(googleMap)
    }
}
