package eu.agribot.agribotapp.ui.fragment.pathmanagement.mainpathscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.kml.KmlContainer
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.NewPathNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.PathNameNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.KmlLayerService
import eu.agribot.agribotapp.domain.service.PathManagementService
import eu.agribot.agribotapp.domain.service.cloud.CloudStorageService
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePathProvider
import eu.agribot.agribotapp.ui.fragment.taskcreator.PathNameFileSymbol
import eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler.CloudFileListItemViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.InputStream

class PathManagementViewModel(
    private val pathManagementService: PathManagementService,
    private val cloudStorageService: CloudStorageService,
    private val filePathProvider: FilePathProvider,
    private val kmlLayerService: KmlLayerService,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {
    val availableFiles: MutableLiveData<List<CloudFileListItemViewModel>> =
        MutableLiveData(listOf())
    val cameraPosition: MutableLiveData<List<LatLng>> = MutableLiveData()
    val selectedLayer: MutableLiveData<InputStream> = MutableLiveData()
    val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()
    private val delay = 200L
    fun getMapTilePath(path: String): String {
        return filePathProvider.getMapTilePath(path)
    }

    fun getCoordinatesFromKml(containers: Iterable<KmlContainer?>) {
        kmlLayerService.getCoordinates(
            containers,
            onGetLatLng = this::onGotCoordinates,
            onError = this::onGetCoordinatesError
        )
    }

    private fun onGotCoordinates(coordinates: ArrayList<LatLng>) {
        cameraPosition.value = coordinates
    }

    private fun onGetCoordinatesError() {
        postToastErrorMessageEvent(resourcesProvider.getString(R.string.kml_layer_problem))
    }

    fun openSelectedLayer(fileName: String) = viewModelScope.launch {
        pathManagementService.updatePathName(fileName)
        pathManagementService.findLayerFile()
        delay(delay)
        if (pathManagementService.getSelectedPathLayer() != null) {
            selectedLayer.value = pathManagementService.getSelectedPathLayer()
        }
    }

    private fun postToastErrorMessageEvent(errorMessage: String) {
        toastMessageEvent.postValue(errorMessage)
    }

    fun postNewPathNavigationEvent() {
        navigationEvent.postValue(PathNameNavigationEvent())
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

    private fun formatFilename(fileName: String): String {
        return if (fileName.contains(PathNameFileSymbol.VARSION_SEPARATOR.symbol)) {
            fileName.substringBefore(PathNameFileSymbol.VARSION_SEPARATOR.symbol)
        } else {
            fileName.substringBefore(PathNameFileSymbol.FILE_FORMAT_SEPARATOR.symbol)
        }
    }

    fun downloadAvailableFileList() {
        cloudStorageService.downloadFilesList(
            onFileAvailable = this::createListFiles,
            onError = this::onCloudDataException
        )
    }

    private fun createListFiles(fileName: String) {
        onAvailableFileFound(fileName, false)
    }

    private fun onCloudDataException(exception: String) {
        toastMessageEvent.postValue(exception)
    }

    fun onDeleteButtonClicked(itemPathPosition: Int) {
        pathManagementService.handleRemovePathOperation(
            availableFiles.value!![itemPathPosition].fileName,
            this::onDeletePathComplete,
            this::onCloudDataException
        )
        refreshRecyclerList(itemPathPosition)
    }

    private fun refreshRecyclerList(pathToDelete: Int) {
        availableFiles.value =
            availableFiles.value!!.filterIndexed { index, _ -> index != pathToDelete }
    }

    private fun onDeletePathComplete() {
        toastMessageEvent.postValue(resourcesProvider.getString(R.string.path_deleted_message))
    }

    fun onEditButtonClicked(itemPathPosition: Int) {
        editPathFile(itemPathPosition)
    }

    private fun editPathFile(itemPathPosition: Int) = viewModelScope.launch {
        pathManagementService.updatePathName(availableFiles.value!![itemPathPosition].fileName)
        pathManagementService.findLayerFile()
        delay(delay)
        checkIsLayerReady()
    }

    private fun checkIsLayerReady() {
        if (pathManagementService.getSelectedPathLayer() != null) {
            postNavigationEvent()
        }
    }

    private fun postNavigationEvent() {
        navigationEvent.postValue(NewPathNavigationEvent())
    }
}
