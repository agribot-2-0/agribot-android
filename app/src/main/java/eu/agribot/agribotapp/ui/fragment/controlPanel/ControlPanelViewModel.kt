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
import eu.agribot.agribotapp.common.navigation.events.TaskSelectNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.RobotLocalizationPose
import eu.agribot.agribotapp.domain.model.cdb.TaskDetails
import eu.agribot.agribotapp.domain.service.KmlLayerService
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.CloudStorageService
import eu.agribot.agribotapp.ui.fragment.taskcreator.TaskDetailsElements
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat

class ControlPanelViewModel(
    private val cloudDatabaseService: CloudDatabaseService,
    private val resourcesProvider: ResourcesProvider,
    private val cloudStorageService: CloudStorageService,
    private val kmlLayerService: KmlLayerService
) : BaseViewModel() {
    val currentTask: MutableLiveData<String> = MutableLiveData()
    val pathName: MutableLiveData<String> = MutableLiveData()
    val temperature: MutableLiveData<String> = MutableLiveData()
    val humidity: MutableLiveData<String> = MutableLiveData()
    val pressure: MutableLiveData<String> = MutableLiveData()
    val wind: MutableLiveData<String> = MutableLiveData()
    val taskDate: MutableLiveData<String> = MutableLiveData()
    val pathStream: MutableLiveData<InputStream> = MutableLiveData()
    val toastMessageEvent: LiveEvent<String> = MutableLiveEvent()
    val markers: MutableLiveData<MarkerOptions> = MutableLiveData()
    val cameraPosition: MutableLiveData<LatLng> = MutableLiveData()

    fun getCoordinatesFromKml(containers: Iterable<KmlContainer?>) {
        kmlLayerService.getCoordinates(
            containers,
            onGetLatLng = this::onGotCoordinates,
            onError = this::onGetCoordinatesError
        )
    }

    private fun onGotCoordinates(coordinates: LatLng) {
        cameraPosition.value = coordinates
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
                    onGotFile = this::onFileAlreadyDownloaded
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
        if (!taskDetails.start_time_estimated) {
            val dateFormat = SimpleDateFormat(TaskDetailsElements.DATE_FORMAT.detailsElement)
            taskDate.value =
                resourcesProvider.getString(R.string.start_time) + dateFormat.format(taskDetails.start_time!!.toDate())
        } else {
            taskDate.value = resourcesProvider.getString(R.string.no_start_date)
        }
        if (taskDetails.path_name != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            pathName.value = taskDetails.path_name
        } else {
            pathName.value = resourcesProvider.getString(R.string.no_path_text)
        }

        if (taskDetails.temperature_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            temperature.value =
                changeTextToSymbol(taskDetails.temperature_condition) + TaskDetailsElements.SEPARATOR.detailsElement + taskDetails.temperature_value + TaskDetailsElements.TEMPERATURE_UNIT.detailsElement
        } else {
            temperature.value = resourcesProvider.getString(R.string.no_temperature_text)
        }

        if (taskDetails.humidity_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {

            humidity.value =
                changeTextToSymbol(taskDetails.humidity_condition) + TaskDetailsElements.SEPARATOR.detailsElement + taskDetails.humidity_value + TaskDetailsElements.PERCENT.detailsElement
        } else {
            humidity.value = resourcesProvider.getString(R.string.no_humidity_text)
        }

        if (taskDetails.pressure_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            pressure.value =
                changeTextToSymbol(taskDetails.pressure_condition) + TaskDetailsElements.SEPARATOR.detailsElement + taskDetails.pressure_value + TaskDetailsElements.PRESSURE_UNIT.detailsElement
        } else {
            pressure.value = resourcesProvider.getString(R.string.no_pressure_text)
        }

        if (taskDetails.wind_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            wind.value =
                changeTextToSymbol(taskDetails.wind_condition) + TaskDetailsElements.SEPARATOR.detailsElement + taskDetails.wind_value + TaskDetailsElements.SPEED_UNIT.detailsElement
        } else {
            wind.value = resourcesProvider.getString(R.string.no_wind_text)
        }
        checkIsCurrentPathExistInStorage()
    }

    private fun changeTextToSymbol(conditionText: String): String {
        return when (conditionText) {
            TaskDetailsElements.LESS_TEXT.detailsElement -> TaskDetailsElements.LESS_SYMBOL.detailsElement
            TaskDetailsElements.MORE_TEXT.detailsElement -> TaskDetailsElements.MORE_SYMBOL.detailsElement
            TaskDetailsElements.EQUAL_TEXT.detailsElement -> TaskDetailsElements.EQUAL_SYMBOL.detailsElement
            else -> TaskDetailsElements.SEPARATOR.detailsElement
        }
    }

    fun onSelectTaskButtonClicked() {
        navigationEvent.postValue(TaskSelectNavigationEvent())
    }
}
