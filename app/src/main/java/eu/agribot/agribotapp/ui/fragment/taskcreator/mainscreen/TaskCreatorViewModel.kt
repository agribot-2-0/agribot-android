package eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.kml.KmlContainer
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskNameNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskPathNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.TaskDetails
import eu.agribot.agribotapp.domain.model.cdb.TaskItem
import eu.agribot.agribotapp.domain.service.KmlLayerService
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.CloudStorageService
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePathProvider
import eu.agribot.agribotapp.ui.fragment.taskcreator.TaskDetailsElements
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
    private val taskCreatorDataManager: TaskCreatorDataManager,
    private val kmlLayerService: KmlLayerService,
    private val filePathProvider: FilePathProvider

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
                cloudStorageService.onFileSelected(
                    fileName,
                    onGotFile = this::onGotPathFile,
                    onSelectedFileError = this::postToastErrorMessageEvent
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
            taskCreatorDataManager.onUpdateTaskName(selectedTaskName)
            cloudDatabaseService.getTaskDetails(
                selectedTaskName,
                this::updateTaskDetails,
                onGetTaskDetailsError = this::postToastErrorMessageEvent
            )
        }
        navigationEvent.postValue(CreatorTaskPathNavigationEvent())
    }

    private fun updateTaskDetails(taskDetails: TaskDetails) {
        taskCreatorDataManager.updateAllTaskParametrs(taskDetails)
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
        taskCreatorDataManager.updateAllTaskParametrs(taskDetails)
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
            temperature.value =
                changeConditionTextToSymbol(taskDetails.temperature_condition) + TaskDetailsElements.SEPARATOR.detailsElement + taskDetails.temperature_value + TaskDetailsElements.TEMPERATURE_UNIT.detailsElement
        } else {
            temperature.value = resourcesProvider.getString(R.string.no_temperature_text)
        }

        if (taskDetails.humidity_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            humidity.value =
                changeConditionTextToSymbol(taskDetails.humidity_condition) + TaskDetailsElements.SEPARATOR.detailsElement + taskDetails.humidity_value + TaskDetailsElements.PERCENT.detailsElement
        } else {
            humidity.value = resourcesProvider.getString(R.string.no_humidity_text)
        }

        if (taskDetails.pressure_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            pressure.value =
                changeConditionTextToSymbol(taskDetails.pressure_condition) + TaskDetailsElements.SEPARATOR.detailsElement + taskDetails.pressure_value + TaskDetailsElements.PRESSURE_UNIT.detailsElement
        } else {
            pressure.value = resourcesProvider.getString(R.string.no_pressure_text)
        }

        if (taskDetails.wind_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            wind.value =
                changeConditionTextToSymbol(taskDetails.wind_condition) + TaskDetailsElements.SEPARATOR.detailsElement + taskDetails.wind_value + TaskDetailsElements.SPEED_UNIT.detailsElement
        } else {
            wind.value = resourcesProvider.getString(R.string.no_wind_text)
        }
        checkIsCurrentPathExistInStorage()
    }

    private fun changeConditionTextToSymbol(conditionText: String): String {
        return when (conditionText) {
            TaskDetailsElements.LESS_TEXT.detailsElement -> TaskDetailsElements.LESS_SYMBOL.detailsElement
            TaskDetailsElements.MORE_TEXT.detailsElement -> TaskDetailsElements.MORE_SYMBOL.detailsElement
            TaskDetailsElements.EQUAL_TEXT.detailsElement -> TaskDetailsElements.EQUAL_SYMBOL.detailsElement
            else -> TaskDetailsElements.SEPARATOR.detailsElement
        }
    }

    fun onNewTaskButtonClicked() {
        taskCreatorDataManager.updateAllTaskParametrs(TaskDetails())
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

    fun getFilePath(filePath: String): String {
        return filePathProvider.getRootDirPath(filePath)
    }
}
