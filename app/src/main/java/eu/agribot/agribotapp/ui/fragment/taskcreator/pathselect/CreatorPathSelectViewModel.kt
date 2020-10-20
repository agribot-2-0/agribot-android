package eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.kml.KmlContainer
import com.google.maps.android.kml.KmlPlacemark
import com.google.maps.android.kml.KmlPolygon
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskTimeNavigationEvent
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.CloudStorageService
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager
import eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler.CloudFileListItemViewModel
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class CreatorPathSelectViewModel(
    private val cloudStorageService: CloudStorageService,
    private val cloudDatabaseService: CloudDatabaseService,
    private val taskCreatorDataManager: TaskCreatorDataManager
) : BaseViewModel() {
    val fileListPathsList: MutableLiveData<List<CloudFileListItemViewModel>> =
        MutableLiveData(listOf())
    val backButtonEvent: MutableLiveEvent<Boolean> = MutableLiveEvent()
    val availableFiles: MutableLiveData<List<CloudFileListItemViewModel>> =
        MutableLiveData(listOf())
    val selectedLayer: MutableLiveData<InputStream> = MutableLiveData()
    var isPathSelected = false
    val toastEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val cameraPosition: MutableLiveData<LatLng> = MutableLiveData()

    fun accessContainers(containers: Iterable<KmlContainer?>) {
        for (container in containers) {
            if (container != null) {
                if (container.hasContainers()) {
                    accessContainers(container.containers)
                } else {
                    if (container.hasPlacemarks()) {
                        accessPlacemarks(container.placemarks)
                    }
                }
            }
        }
    }

    private fun accessPlacemarks(placemarks: Iterable<KmlPlacemark?>) {
        for (placemark in placemarks) {
            if (placemark != null) {
                val geometry = placemark.geometry
                if (geometry is KmlPolygon) {
                    cameraPosition.value = geometry.outerBoundaryCoordinates[0]
                }
            }
        }
    }

    private fun onAvailableFileFound(fileName: String) {
        onFileInStorageFound(fileName)
        val isFileAlreadyAdded = availableFiles.value!!.any {
            it.fileName == fileName
        }
        if (!isFileAlreadyAdded) {
            availableFiles.value = listOf(
                CloudFileListItemViewModel(
                    formatFilename(fileName)
                ), *availableFiles.value!!.toTypedArray()
            )
        }
    }

    private fun onFileInStorageFound(fileName: String) {
        val isFileAlreadyAdded = fileListPathsList.value!!.any { file ->
            file.fileName == fileName
        }
        if (!isFileAlreadyAdded) {
            fileListPathsList.value = listOf(
                CloudFileListItemViewModel(
                    fileName
                ), *fileListPathsList.value!!.toTypedArray()
            )
        }
    }

    private fun formatFilename(fileName: String): String {
        return if (fileName.contains("_")) {
            fileName.substringBefore("_")
        } else {
            fileName.substringBefore(".")
        }
    }

    fun onPreviousButtonClicked() {
        backButtonEvent.postValue(true)
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
        for (item in fileListPathsList.value!!) {
            if (item.fileName.contains(fileName))
                try {
                    cloudStorageService.downloadFile(
                        item.fileName,
                        onGotFile = this::onGotPathFile
                    )
                } catch (exception: IOException) {
                    onCloudDataException(exception.toString())
                }
        }
    }

    private fun onGotPathFile(file: File) {
        val fileInputStream: InputStream = FileInputStream(file)
        selectedLayer.value = fileInputStream
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

    private fun onCloudDataException(exception: String) {
        toastEvent.postValue(exception)
    }
}
