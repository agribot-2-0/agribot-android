package eu.agribot.agribotapp.communication.database.clouddatabase

import eu.agribot.agribotapp.communication.database.clouddatabase.model.*
import eu.agribot.agribotapp.communication.database.clouddatabase.model.programtasks.NewRobotTaskDetailsCDB

interface CloudDatabase {

    fun getUserData(
        onDatabaseSucess: (data: UserInfoCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun getEngineHealthData(
        onDatabaseSucess: (data: EngineHealthCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun getFirstRobotHealthEngineData(
        onDatabaseSucess: (data: FirstRobotHealthEngineCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun getFirstRobotLocalizationPoseData(
        onDatabaseSucess: (data: RobotLocalizationPoseCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun getFirstRobotWeatherStationHumidityData(
        onDatabaseSucess: (data: FirstRobotWeatherStationHumidityCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun getFirstRobotWeatherStationWindData(
        onDatabaseSucess: (data: FirstRobotWeatherStationWindCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun getFirstRobotWeatherStationPressureData(
        onDatabaseSucess: (data: FirstRobotWeatherStationPressureCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun getFirstRobotWeatherStationTemperatureData(
        onDatabaseSucess: (data: FirstRobotWeatherStationTemperatureCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun getSecondRobotLocalizationPoseData(
        onDatabaseSucess: (data: RobotLocalizationPoseCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun saveUserData(
        data: UserInfoCDB,
        user: String,
        onDatabaseSucess: () -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun deleteUserData(
        onDatabaseSucess: () -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun createNewTask(taskName: String, newRobotTaskDetailsCDB: NewRobotTaskDetailsCDB, onDatabaseSucess: () -> Unit, onDatabaseError: (Throwable) -> Unit)

    fun getTaskDetails(
        taskID: String,
        onGotTaskDetails: (taskDetailsCDB: TaskDetailsCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun getCurrentTaskName(
        robotName: String,
        onGetCurrentTaskSucess: (taskDetailsCDB: TaskDetailsCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun deleteSelectedTask(taskID: String, onDeleteSucess: () -> Unit, onDeleteError: (Throwable) -> Unit)

    fun getTaskItem(
        onGetTaskItem: (taskItemCDB: TaskItemCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )
}
