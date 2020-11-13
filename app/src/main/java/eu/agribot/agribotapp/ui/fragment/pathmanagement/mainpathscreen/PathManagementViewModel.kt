package eu.agribot.agribotapp.ui.fragment.pathmanagement.mainpathscreen

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.NewPathNavigationEvent
import eu.agribot.agribotapp.domain.service.PathManagementService
import eu.agribot.agribotapp.domain.service.cloud.CloudStorageService
import eu.agribot.agribotapp.ui.fragment.taskcreator.PathNameFileSymbol
import eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler.CloudFileListItemViewModel

class PathManagementViewModel(
    private val pathManagementService: PathManagementService,
    private val cloudStorageService: CloudStorageService
) : BaseViewModel() {
    val availableFiles: MutableLiveData<List<CloudFileListItemViewModel>> =
        MutableLiveData(listOf())

    val agriBotLatLng = LatLng(51.11322, 17.01958)

    val markers: MutableLiveData<MarkerOptions> = MutableLiveData()

    fun addDefaultMarker() {
        markers.value = MarkerOptions().position(agriBotLatLng)
    }

    fun postNewPathNavigationEvent() {
        navigationEvent.postValue(NewPathNavigationEvent())
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
    }
}
