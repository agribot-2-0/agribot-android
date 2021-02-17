package eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen

import android.annotation.SuppressLint
import android.text.format.DateFormat
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
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateViewModel
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.items.RobotStateItems
import eu.agribot.agribotapp.ui.fragment.taskcreator.TaskDetailsElements
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.TaskListItemViewModel
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.Calendar
import java.util.Locale

class TaskCreatorViewModel(
    private val resourcesProvider: ResourcesProvider,
    private val cloudDatabaseService: CloudDatabaseService,
    private val cloudStorageService: CloudStorageService,
    private val taskCreatorDataManager: TaskCreatorDataManager,
    private val kmlLayerService: KmlLayerService,
    private val filePathProvider: FilePathProvider

) : BaseViewModel() {
    val robotDataStateList: MutableLiveData<List<RobotStateViewModel>> = MutableLiveData(listOf())
    private val currentTask: MutableLiveData<String> = MutableLiveData()

    val selectedLayer: MutableLiveData<InputStream> = MutableLiveData()
    val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val taskList: MutableLiveData<List<TaskListItemViewModel>> = MutableLiveData(listOf())
    val pathName: MutableLiveData<String> = MutableLiveData()
    val cameraPosition: MutableLiveData<LatLng> = MutableLiveData()

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
        cloudDatabaseService.deleteTaskItem(
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
            onGetTaskDetails = this::onGotCurrentTaskDetails,
            onGetTaskDetailsError = this::postToastErrorMessageEvent
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun onGotCurrentTaskDetails(taskDetails: TaskDetails) {
        (if (currentTask.value != null && currentTask.value != TaskDetailsElements.EMPTY_TASK.detailsElement) {
            setCurrentTask(currentTask.value!!)
        } else {
            setEmptyParameter(RobotStateItems.CURRENT_TASK.ID)
        })

        taskDetails.let {
            setCurrentPath(it)
            setStartDate(it)
            setStartTime(it)
            setTemperatureParam(it)
            setHumidityParam(it)
            setWindParam(it)
            setPressureParam(it)
        }
    }

    private fun setStartDate(taskDetails: TaskDetails) {
        if (!taskDetails.start_time_estimated) {
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.START_DATE.ID,
                    taskDetails.start_time?.let { getDate(it.seconds) }.toString(),
                    TaskDetailsElements.NO_UNIT_VALUE.detailsElement
                )
            )
        } else {
            setEmptyParameter(RobotStateItems.START_TIME.ID)
        }
    }

    private fun getDate(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp * 1000L
        return DateFormat.format("dd-MM-yyyy", calendar).toString()
    }

    private fun getTime(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp * 1000L
        return DateFormat.format("HH:mm", calendar).toString()
    }

    private fun setStartTime(taskDetails: TaskDetails) {
        if (!taskDetails.start_time_estimated) {
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.START_TIME.ID,
                    taskDetails.start_time?.let { getTime(it.seconds) }.toString(),
                    TaskDetailsElements.NO_UNIT_VALUE.detailsElement

                )
            )
        } else {
            setEmptyParameter(RobotStateItems.START_DATE.ID)
        }
    }

    private fun setCurrentTask(taskName: String) {
        addParameterToRobotDataStateList(
            RobotStateViewModel(
                RobotStateItems.CURRENT_TASK.ID,
                taskName,
                TaskDetailsElements.NO_UNIT_VALUE.detailsElement

            )
        )
    }

    private fun setCurrentPath(taskDetails: TaskDetails) {
        if (taskDetails.path_name != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.CURRENT_PATH.ID,
                    taskDetails.path_name,
                    TaskDetailsElements.NO_UNIT_VALUE.detailsElement
                )
            )
            pathName.value = taskDetails.path_name
            checkIsCurrentPathExistInStorage()
        } else {
            setEmptyParameter(RobotStateItems.CURRENT_PATH.ID)
        }
    }

    private fun setHumidityParam(taskDetails: TaskDetails) {
        if (taskDetails.humidity_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.HUMIDITY.ID,
                    changeTextToSymbol(taskDetails.humidity_condition) + taskDetails.humidity_value,
                    TaskDetailsElements.PERCENT.detailsElement
                )
            )
        } else {
            setEmptyParameter(RobotStateItems.HUMIDITY.ID)
        }
    }

    private fun setTemperatureParam(taskDetails: TaskDetails) {
        if (taskDetails.temperature_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.TEMPERATURE.ID,
                    changeTextToSymbol(taskDetails.temperature_condition) + taskDetails.temperature_value,
                    TaskDetailsElements.TEMPERATURE_UNIT.detailsElement
                )
            )
        } else {
            setEmptyParameter(RobotStateItems.TEMPERATURE.ID)
        }
    }

    private fun setWindParam(taskDetails: TaskDetails) {
        if (taskDetails.wind_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.WIND.ID,
                    changeTextToSymbol(taskDetails.wind_condition) + taskDetails.wind_value,
                    TaskDetailsElements.SPEED_UNIT.detailsElement
                )
            )
        } else {
            setEmptyParameter(RobotStateItems.WIND.ID)
        }
    }

    private fun setPressureParam(taskDetails: TaskDetails) {
        if (taskDetails.pressure_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.PRESSURE.ID,
                    changeTextToSymbol(taskDetails.pressure_condition) + taskDetails.pressure_value,
                    TaskDetailsElements.PRESSURE_UNIT.detailsElement
                )
            )
        } else {
            setEmptyParameter(RobotStateItems.PRESSURE.ID)
        }
    }

    private fun setEmptyParameter(paramID: Int) {
        addParameterToRobotDataStateList(
            RobotStateViewModel(
                paramID,
                resourcesProvider.getString(R.string.task_param_not_set),
                TaskDetailsElements.NO_UNIT_VALUE.detailsElement
            )
        )
    }

    private fun changeTextToSymbol(conditionText: String): String {
        return when (conditionText) {
            TaskDetailsElements.LESS_TEXT.detailsElement -> TaskDetailsElements.LESS_SYMBOL.detailsElement
            TaskDetailsElements.MORE_TEXT.detailsElement -> TaskDetailsElements.MORE_SYMBOL.detailsElement
            TaskDetailsElements.EQUAL_TEXT.detailsElement -> TaskDetailsElements.EQUAL_SYMBOL.detailsElement
            else -> TaskDetailsElements.SEPARATOR.detailsElement
        }
    }

    private fun addParameterToRobotDataStateList(robotStateParameter: RobotStateViewModel) {
        val isAttributeAlreadyAdded = robotDataStateList.value!!.any { arrtibute ->
            arrtibute.stateItemId == robotStateParameter.stateItemId
        }
        if (!isAttributeAlreadyAdded)
            robotDataStateList.value = listOf(
                *robotDataStateList.value!!.toTypedArray(),
                RobotStateViewModel(
                    robotStateParameter.stateItemId,
                    robotStateParameter.stateValue,
                    robotStateParameter.stateUnit
                )

            )
        else {
            robotDataStateList.value = robotDataStateList.value!!.map { oldAttribute ->
                if (oldAttribute.stateItemId == robotStateParameter.stateItemId) {
                    RobotStateViewModel(
                        oldAttribute.stateItemId,
                        robotStateParameter.stateValue,
                        oldAttribute.stateUnit
                    )
                } else {
                    oldAttribute
                }
            }
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

    fun getMapTilePath(filePath: String): String {
        return filePathProvider.getMapTilePath(filePath)
    }
}
