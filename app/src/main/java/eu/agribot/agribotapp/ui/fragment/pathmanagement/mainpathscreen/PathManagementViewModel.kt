package eu.agribot.agribotapp.ui.fragment.pathmanagement.mainpathscreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.NewPathNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.PathNameNavigationEvent
import eu.agribot.agribotapp.domain.service.PathManagementService
import eu.agribot.agribotapp.domain.service.cloud.CloudStorageService
import eu.agribot.agribotapp.ui.fragment.taskcreator.PathNameFileSymbol
import eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler.CloudFileListItemViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.InputStream

class PathManagementViewModel(
    private val pathManagementService: PathManagementService,
    private val cloudStorageService: CloudStorageService
) : BaseViewModel() {
    val availableFiles: MutableLiveData<List<CloudFileListItemViewModel>> =
        MutableLiveData(listOf())

    // MOCK COORDINATE - REMOVED ON NEXT BRANCH
    val agriBotLatLng = LatLng(51.11322, 17.01958)

    val markers: MutableLiveData<MarkerOptions> = MutableLiveData()

    fun addDefaultMarker() {
        markers.value = MarkerOptions().position(agriBotLatLng)
    }

    fun postNewPathNavigationEvent() {
        navigationEvent.postValue(PathNameNavigationEvent())
    }

    private fun onAvailableFileFound(fileName: String, isFileActive: Boolean) {
        availableFiles.value?.any {
            it.fileName == formatFilename(fileName)
        }.let {
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
        // TODO NOT YET IMPLEMENT
    }

    fun onDeleteButtonClicked(itemPathPosition: Int) {
        // TODO NOT YET IMPLEMENT
    }

    fun onEditButtonClicked(itemPathPosition: Int) = viewModelScope.launch {
        pathManagementService.updatePathName(availableFiles.value!![itemPathPosition].fileName)
        pathManagementService.findLayerFile()
        runBlocking {
       //    Log.e("xd", "" + checkIsLayerReady(pathManagementService.checkIsCurrentPathExistInStorage()))
        }
        postNavigationEvent()

    }


    private tailrec fun checkIsLayerReady(pathLayer: InputStream?): Boolean {
        return if (pathLayer != null) {
            true
        } else {
            checkIsLayerReady(pathLayer)
        }
    }

    private fun postNavigationEvent() {
        navigationEvent.postValue(NewPathNavigationEvent())
    }

}

