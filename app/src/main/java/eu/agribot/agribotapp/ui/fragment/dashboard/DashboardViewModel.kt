package eu.agribot.agribotapp.ui.fragment.dashboard

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.RobotLocalizationPose
import eu.agribot.agribotapp.domain.model.TaskDetails
import eu.agribot.agribotapp.domain.service.AutonomyService
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.CloudStorageService
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.TaskDetailsElements
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat

class DashboardViewModel(
    private val autonomyService: AutonomyService,
    val cloudDatabaseService: CloudDatabaseService,
    val resourcesProvider: ResourcesProvider,
    val cloudStorageService: CloudStorageService
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

    private fun checkIsCurrentPathExistInStorage() {
        cloudStorageService.downloadFilesList(
            onFileAvailable = this::checkIsFileExist,
            onError = this::postToastErrorMessageEvent
        )
    }

    private fun checkIsFileExist(fileName: String) {
        if (fileName.contains(pathName.value.toString(), ignoreCase = true)) {
            try {
                cloudStorageService.downloadFile(
                    fileName,
                    onFileAlreadyDownloaded = this::onFileAlreadyDownloaded,
                    onDownloadSucess = this::onFileAlreadyDownloaded
                )
            } catch (exception: IOException) {
                postToastErrorMessageEvent("Add PathLayer to map problem")
            }
        }
    }

    private fun onFileAlreadyDownloaded(file: File) {
        val fileInputStream: InputStream = FileInputStream(file)
        pathStream.value = fileInputStream
    }

    fun getAgribotPosition() {
        cloudDatabaseService.getFirstRobotLocalizationPose(
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

    fun getCurrentTask() {
        cloudDatabaseService.getActualTaskDetails(
            onGetCurrentTaskDetailsSucess = this::onGotCurrentTaskDetails,
            onGetCurrentTaskNameSucess = this::onGotCurrentTaskName,
            onError = this::postToastErrorMessageEvent
        )
    }

    private fun onGotCurrentTaskName(taskName: String) {
        currentTask.value = taskName
    }

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
            val temperatureCondition: String = when (taskDetails.temperature_condition) {
                TaskDetailsElements.LESS_TEXT.detailsElement -> TaskDetailsElements.LESS_SYMBOL.detailsElement
                TaskDetailsElements.MORE_TEXT.detailsElement -> TaskDetailsElements.MORE_SYMBOL.detailsElement
                TaskDetailsElements.EQUAL_TEXT.detailsElement -> TaskDetailsElements.EQUAL_SYMBOL.detailsElement
                else -> TaskDetailsElements.SEPARATOR.detailsElement
            }
            temperature.value =
                temperatureCondition + TaskDetailsElements.SEPARATOR.detailsElement + taskDetails.temperature_value + TaskDetailsElements.TEMPERATURE_UNIT.detailsElement
        } else {
            temperature.value = resourcesProvider.getString(R.string.no_temperature_text)
        }

        if (taskDetails.humidity_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            val humidityCondition: String = when (taskDetails.humidity_condition) {
                TaskDetailsElements.LESS_TEXT.detailsElement -> TaskDetailsElements.LESS_SYMBOL.detailsElement
                TaskDetailsElements.MORE_TEXT.detailsElement -> TaskDetailsElements.MORE_SYMBOL.detailsElement
                TaskDetailsElements.EQUAL_TEXT.detailsElement -> TaskDetailsElements.EQUAL_SYMBOL.detailsElement
                else -> TaskDetailsElements.SEPARATOR.detailsElement
            }
            humidity.value =
                humidityCondition + TaskDetailsElements.SEPARATOR.detailsElement + taskDetails.humidity_value + TaskDetailsElements.PERCENT.detailsElement
        } else {
            humidity.value = resourcesProvider.getString(R.string.no_humidity_text)
        }

        if (taskDetails.pressure_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            val pressureCondition: String = when (taskDetails.pressure_condition) {
                TaskDetailsElements.LESS_TEXT.detailsElement -> TaskDetailsElements.LESS_SYMBOL.detailsElement
                TaskDetailsElements.MORE_TEXT.detailsElement -> TaskDetailsElements.MORE_SYMBOL.detailsElement
                TaskDetailsElements.EQUAL_TEXT.detailsElement -> TaskDetailsElements.EQUAL_SYMBOL.detailsElement
                else -> TaskDetailsElements.SEPARATOR.detailsElement
            }
            pressure.value =
                pressureCondition + TaskDetailsElements.SEPARATOR.detailsElement + taskDetails.pressure_value + TaskDetailsElements.PRESSURE_UNIT.detailsElement
        } else {
            pressure.value = resourcesProvider.getString(R.string.no_pressure_text)
        }

        if (taskDetails.wind_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            val windCondition: String = when (taskDetails.wind_condition) {
                TaskDetailsElements.LESS_TEXT.detailsElement -> TaskDetailsElements.LESS_SYMBOL.detailsElement
                TaskDetailsElements.MORE_TEXT.detailsElement -> TaskDetailsElements.MORE_SYMBOL.detailsElement
                TaskDetailsElements.EQUAL_TEXT.detailsElement -> TaskDetailsElements.EQUAL_SYMBOL.detailsElement
                else -> TaskDetailsElements.SEPARATOR.detailsElement
            }

            wind.value =
                windCondition + TaskDetailsElements.SEPARATOR.detailsElement + taskDetails.wind_value + TaskDetailsElements.SPEED_UNIT.detailsElement
        } else {
            wind.value = resourcesProvider.getString(R.string.no_wind_text)
        }
        checkIsCurrentPathExistInStorage()
    }

    fun sendInitAutoMode() {
        autonomyService.sendInitAutonomyMode()
    }
}
