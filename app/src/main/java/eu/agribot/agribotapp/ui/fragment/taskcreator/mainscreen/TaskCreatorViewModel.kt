package eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.kml.KmlContainer
import com.google.maps.android.kml.KmlLineString
import com.google.maps.android.kml.KmlPlacemark
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskNameNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskPathNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.TaskDetails
import eu.agribot.agribotapp.domain.model.TaskItem
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.CloudStorageService
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.TaskListItemViewModel
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat

class TaskCreatorViewModel(
    private val resourcesProvider: ResourcesProvider,
    private val cloudDatabaseService: CloudDatabaseService,
    private val cloudStorageService: CloudStorageService,
    private val taskCreatorDataManager: TaskCreatorDataManager

) : BaseViewModel() {
    val selectedLayer: MutableLiveData<InputStream> = MutableLiveData()
    val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val taskDate: MutableLiveData<String> = MutableLiveData()
    val taskList: MutableLiveData<List<TaskListItemViewModel>> = MutableLiveData(listOf())
    val pathName: MutableLiveData<String> = MutableLiveData()
    val temperature: MutableLiveData<String> = MutableLiveData()
    val humidity: MutableLiveData<String> = MutableLiveData()
    val pressure: MutableLiveData<String> = MutableLiveData()
    val wind: MutableLiveData<String> = MutableLiveData()
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
                try {
                    val kmlLineString = placemark.geometry as KmlLineString
                    cameraPosition.value = kmlLineString.geometryObject[0]
                } catch (exception: Exception) {
                    postToastErrorMessageEvent("Kml LatLngException")
                }
            }
        }
    }

    fun getAvailableTask() {
        cloudDatabaseService.getTaskItem(
            onGetTaskItem = this::onAvailableTaskFound,
            onGetTaskItemError = this::postToastErrorMessageEvent
        )
    }

    private fun checkIsCurrentPathExistInStorage() {
        cloudStorageService.downloadFilesList(
            onFileAvailable = this::checkIsFileExist,
            onError = this::postToastErrorMessageEvent
        )
    }

    private fun checkIsFileExist(fileName: String) {
        if (fileName.contains(pathName.value!!.toString(), ignoreCase = false)) {
            try {
                cloudStorageService.downloadFile(
                    fileName,
                    onGotFile = this::onGotPathFile
                )
            } catch (exception: IOException) {
                postToastErrorMessageEvent("Add PathLayer to map problem")
            }
        }
    }

    private fun onGotPathFile(file: File) {
        val fileInputStream: InputStream = FileInputStream(file)
        selectedLayer.value = fileInputStream
    }

    private fun onAvailableTaskFound(taskItem: TaskItem) {
        val isTaskAlreadyAdded = taskList.value!!.any { foundTask ->
            foundTask.taskName == taskItem.taskName
        }
        if (!isTaskAlreadyAdded) {
            taskList.value = listOf(
                TaskListItemViewModel(
                    taskItem.taskName,
                    taskItem.pathName
                ), *taskList.value!!.toTypedArray()
            )
        }
    }

    fun onDeleteButtonClicked(positionOnList: Int) {
        cloudDatabaseService.deleteSelectedTask(
            taskList.value?.get(positionOnList)!!.taskName,
            onDeleteError = this::postToastErrorMessageEvent,
            onDeleteSucess = this::onDeleteSucess
        )
        taskList.value = taskList.value!!.minus(
            TaskListItemViewModel(
                taskList.value?.get(positionOnList)!!.taskName,
                taskList.value?.get(positionOnList)!!.pathName
            )
        )
    }

    private fun onDeleteSucess() {
        postToastInformationMessageEvent(resourcesProvider.getString(R.string.delete_sucess_text))
    }

    fun onEditButtonClicked(positionOnList: Int) {
        taskList.value?.get(positionOnList)?.taskName?.let { selectedTaskName ->
            taskCreatorDataManager.onUpdateTaskName(
                selectedTaskName
            )
            cloudDatabaseService.getTaskDetails(
                selectedTaskName,
                this::updateTaskDetails,
                onGetTaskDetailsError = this::postToastErrorMessageEvent
            )
        }
        navigationEvent.postValue(CreatorTaskPathNavigationEvent())
    }

    private fun updateTaskDetails(taskDetails: TaskDetails) {
        taskCreatorDataManager.updateAllTaskParameters(taskDetails)
    }

    fun onTaskSelected(taskListItemViewModel: TaskListItemViewModel) {
        cloudDatabaseService.getTaskDetails(
            taskId = taskListItemViewModel.taskName,
            onGetTaskDetails = this::onGotTaskDetails,
            onGetTaskDetailsError = this::postToastErrorMessageEvent
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun onGotTaskDetails(taskDetails: TaskDetails) {
        taskCreatorDataManager.updateAllTaskParameters(taskDetails)
        if (!taskDetails.start_time_estimated) {
            val dateFormat = SimpleDateFormat(TaskDetailsElements.DATE_FORMAT.detailsElement)
            taskDate.value =
                resourcesProvider.getString(R.string.start_time) + dateFormat.format(taskDetails.start_time!!.toDate())
        } else {
            taskDate.value = resourcesProvider.getString(R.string.no_start_date)
        }
        if (taskDetails.path_name != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            pathName.value =
                taskDetails.path_name
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

    fun onNewTaskButtonClicked() {
        navigationEvent.postValue(
            CreatorTaskNameNavigationEvent()
        )
    }

    private fun postToastErrorMessageEvent(errorMessage: String) {
        toastMessageEvent.postValue(errorMessage)
    }

    private fun postToastInformationMessageEvent(informationMessage: String) {
        toastMessageEvent.postValue(informationMessage)
    }
}
