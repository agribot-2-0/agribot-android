package eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.kml.KmlContainer
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskTimeNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.KmlLayerService
import eu.agribot.agribotapp.domain.service.cloud.CloudStorageService
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePathProvider
import eu.agribot.agribotapp.ui.fragment.taskcreator.PathNameFileSymbol
import eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler.CloudFileListItemViewModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.PickerSymbols
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class CreatorPathSelectViewModel(
    private val cloudStorageService: CloudStorageService,
    private val taskCreatorDataManager: TaskCreatorDataManager,
    private val resourcesProvider: ResourcesProvider,
    private val kmlLayerService: KmlLayerService,
    private val filePathProvider: FilePathProvider
) : BaseViewModel() {
    private val fileListPathsList: MutableLiveData<List<CloudFileListItemViewModel>> =
        MutableLiveData(listOf())
    val availableFiles: MutableLiveData<List<CloudFileListItemViewModel>> =
        MutableLiveData(listOf())
    val selectedLayer: MutableLiveData<InputStream> = MutableLiveData()
    val toastEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val cameraPosition: MutableLiveData<LatLng> = MutableLiveData()

    private fun getSavedPath() {
        if (taskCreatorDataManager.taskDetails.pathName != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
            downloadSelectedFile(taskCreatorDataManager.taskDetails.pathName)
        }
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

    private fun postToastErrorMessageEvent(errorMessage: String) {
        toastEvent.postValue(errorMessage)
    }

    private fun onAvailableFileFound(fileName: String, isFileActive: Boolean) {
        val isFileAlreadyAdded = availableFiles.value!!.any {
            it.fileName == formatFilename(fileName)
        }
        if (!isFileAlreadyAdded) {
            availableFiles.value = listOf(
                CloudFileListItemViewModel(
                    formatFilename(fileName),
                    isFileActive
                ), *availableFiles.value!!.toTypedArray()
            )
        }
    }

    private fun onFileInStorageFound(fileName: String, isActive: Boolean) {
        val isFileAlreadyAdded = fileListPathsList.value!!.any { file ->
            file.fileName == fileName
        }
        if (!isFileAlreadyAdded) {
            fileListPathsList.value = listOf(
                CloudFileListItemViewModel(
                    fileName, isActive
                ), *fileListPathsList.value!!.toTypedArray()
            )
        }
        getSavedPath()
    }

    private fun formatFilename(fileName: String): String {
        return if (fileName.contains(PathNameFileSymbol.VARSION_SEPARATOR.symbol)) {
            fileName.substringBefore(PathNameFileSymbol.VARSION_SEPARATOR.symbol)
        } else {
            fileName.substringBefore(PathNameFileSymbol.FILE_FORMAT_SEPARATOR.symbol)
        }
    }

    fun isPathSelected(): Boolean {
        return taskCreatorDataManager.taskDetails.pathName != PickerSymbols.PICKER_DEFAULT_VALUE.symbol
    }

    fun downloadAvailableFileList() {
        cloudStorageService.downloadFilesList(
            onFileAvailable = this::createListFiles,
            onError = this::onCloudDataException
        )
    }

    private fun createListFiles(fileName: String) {
        val isActive = fileName.contains(taskCreatorDataManager.getTaskParemeters().pathName)
        onAvailableFileFound(fileName, isActive)
        onFileInStorageFound(fileName, isActive)
    }

    fun downloadSelectedFile(fileName: String) {
        taskCreatorDataManager.onUpdatePathName(fileName)
        for (item in fileListPathsList.value!!) {
            if (item.fileName.contains(fileName))
                try {
                    cloudStorageService.onFileSelected(
                        item.fileName,
                        onGotFile = this::onGotPathFile,
                        onSelectedFileError = this::postToastErrorMessageEvent
                    )
                } catch (exception: IOException) {
                    onCloudDataException(exception.toString())
                }
        }
    }

    private fun onGotPathFile(file: File) {
        val fileInputStream: InputStream = FileInputStream(file)
        selectedLayer.value = fileInputStream
        fileInputStream.close()
    }

    fun onNextButtonClicked() {
        if (isPathSelected()) {
            navigationEvent.postValue(
                CreatorTaskTimeNavigationEvent()
            )
        } else {
            toastEvent.postValue(resourcesProvider.getString(R.string.no_path_selected))
        }
    }

    private fun onCloudDataException(exception: String) {
        toastEvent.postValue(exception)
    }

    fun getMapTilePath(filePath: String): String {
        return filePathProvider.getMapTilePath(filePath)
    }
}
