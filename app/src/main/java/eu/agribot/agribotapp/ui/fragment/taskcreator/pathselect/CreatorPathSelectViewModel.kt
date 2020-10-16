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
    }

    fun downloadAvailableFileList() {
        cloudStorageService.downloadFilesList(
            onFileAvailable = this::onAvailableFileFound,
            onError = this::onCloudDataException
        )
    }

    fun downloadSelectedFile(fileName: String) {
        isPathSelected = true
        taskCreatorDataManager.onUpdatePathName(fileName)
        try {
            cloudStorageService.downloadFile(
                fileName,
                onFileAlreadyDownloaded = this::onFileAlreadyDownloaded,
                onDownloadSucess = this::onFileAlreadyDownloaded
            )
        } catch (exception: IOException) {
            onCloudDataException(exception.toString())
        }
    }

    private fun onFileAlreadyDownloaded(file: File) {
        val fileInputStream: InputStream = FileInputStream(file)
        selectedLayer.value = fileInputStream
    }

    fun getAgribotPosition() {
        cloudDatabaseService.getFirstRobotLocalizationPose(
            onSuccess = this::setCurrentRobotPosition,
            onError = this::onCloudDataException
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

    fun onNextButtonClicked() {
        if (isPathSelected) {
            navigationEvent.postValue(
                CreatorTaskTimeNavigationEvent()
            )
        } else {
            toastEvent.postValue("No path file selected")
        }
    }

    private fun onCloudDataException(exception : String){
        toastEvent.postValue(exception)
    }

}
