package eu.agribot.agribotapp.communication.database.clouddatabase

import eu.agribot.agribotapp.communication.database.clouddatabase.model.ActualTaskCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.EngineHealthCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.NewRobotTaskDetailsCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.RobotHealthEngineCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.RobotLocalizationPoseCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.RobotWeatherStationHumidityCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.RobotWeatherStationPressureCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.RobotWeatherStationTemperatureCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.RobotWeatherStationWindCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.TaskDetailsCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.TaskItemCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.UserInfoCDB

interface CloudDatabase {

    fun getUsersRobotName()

    fun getUserData(
        onDatabaseSucess: (data: UserInfoCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun getEngineHealthData(
        onDatabaseSucess: (data: EngineHealthCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun getFirstRobotHealthEngineData(
        onDatabaseSucess: (data: RobotHealthEngineCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun getFirstRobotLocalizationPoseData(
        onDatabaseSucess: (data: RobotLocalizationPoseCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun getFirstRobotWeatherStationHumidityData(
        onDatabaseSucess: (data: RobotWeatherStationHumidityCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun getFirstRobotWeatherStationWindData(
        onDatabaseSucess: (data: RobotWeatherStationWindCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun getFirstRobotWeatherStationPressureData(
        onDatabaseSucess: (data: RobotWeatherStationPressureCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun getFirstRobotWeatherStationTemperatureData(
        onDatabaseSucess: (data: RobotWeatherStationTemperatureCDB) -> Unit,
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

    fun createNewTask(
        taskName: String,
        newRobotTaskDetailsCDB: NewRobotTaskDetailsCDB,
        onDatabaseError: (Throwable) -> Unit
    )

    fun getTaskDetails(
        taskID: String,
        onGotTaskDetails: (taskDetailsCDB: TaskDetailsCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun getCurrentTaskDetails(
        robotName: String,
        onGetCurrentTaskNameSucess: (currentTaskName: String) -> Unit,
        onGetCurrentTaskDetailsSucess: (taskDetailsCDB: TaskDetailsCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )

    fun setCurrenttask(
        robotName: String,
        actualTaskCDB: ActualTaskCDB,
        onDatabaseError: (Throwable) -> Unit
    )

    fun deleteSelectedTask(
        taskID: String,
        onDeleteSucess: () -> Unit,
        onDeleteError: (Throwable) -> Unit
    )

    fun getTaskItem(
        onGetTaskItem: (taskItemCDB: TaskItemCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    )
}
