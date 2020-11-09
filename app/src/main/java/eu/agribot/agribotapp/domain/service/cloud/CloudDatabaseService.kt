package eu.agribot.agribotapp.domain.service.cloud

import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabase
import eu.agribot.agribotapp.domain.error.CloudStorageErrorHandler
import eu.agribot.agribotapp.domain.model.cdb.ActualTask
import eu.agribot.agribotapp.domain.model.cdb.EngineHealth
import eu.agribot.agribotapp.domain.model.cdb.RobotHealthEngine
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationHumidity
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationPressure
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationTemperature
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationWind
import eu.agribot.agribotapp.domain.model.cdb.RobotLocalizationPose
import eu.agribot.agribotapp.domain.model.cdb.TaskDetails
import eu.agribot.agribotapp.domain.model.cdb.TaskItem
import eu.agribot.agribotapp.domain.model.cdb.UserInfo
import eu.agribot.agribotapp.domain.model.cdb.taskprogram.taskmodel.NewRobotTaskDetails

class CloudDatabaseService(
    val cloudDatabase: CloudDatabase,
    val cloudStorageErrorHandler: CloudStorageErrorHandler
) {

    fun getUserRobotName() {
        cloudDatabase.getUsersRobotName()
    }
    fun getEngineHealth(
        onSuccess: (EngineHealth) -> Unit,
        onError: (error: String) -> Unit
    ) {
        cloudDatabase.getEngineHealthData(
            onDatabaseSucess = {
                onSuccess(it.toDomainModel())
            },
            onDatabaseError = {
                onError(it.message!!)
            }
        )
    }

    fun getRobotHealthEngine(
        onSuccess: (RobotHealthEngine) -> Unit,
        onError: (String) -> Unit
    ) {
        cloudDatabase.getRobotHealthEngineData(onDatabaseSucess = {
            onSuccess(it.toDomainModel())
        },
            onDatabaseError = {
                onError(it.message!!)
            })
    }

    fun getRobotLocalizationPose(
        onSuccess: (RobotLocalizationPose) -> Unit,
        onError: (String) -> Unit
    ) {
        cloudDatabase.getRobotLocalizationPoseData(onDatabaseSucess = {
            onSuccess(it.toDomainModel())
        },
            onDatabaseError = {
                onError(it.message!!)
            })
    }

    fun getRobotWeatherStationHumidity(
        onSuccess: (RobotWeatherStationHumidity) -> Unit,
        onError: (String) -> Unit
    ) {
        cloudDatabase.getRobotWeatherStationHumidityData(onDatabaseSucess = {
            onSuccess(it.toDomainModel())
        },
            onDatabaseError = {
                onError(it.message!!)
            })
    }

    fun getRobotWeatherStationWind(
        onSuccess: (RobotWeatherStationWind) -> Unit,
        onError: (String) -> Unit
    ) {
        cloudDatabase.getRobotWeatherStationWindData(onDatabaseSucess = {
            onSuccess(it.toDomainModel())
        },
            onDatabaseError = {
                onError(it.message!!)
            })
    }

    fun getRobotWeatherStationPressure(
        onSuccess: (RobotWeatherStationPressure) -> Unit,
        onError: (String) -> Unit
    ) {
        cloudDatabase.getRobotWeatherStationPressureData(onDatabaseSucess = {
            onSuccess(it.toDomainModel())
        },
            onDatabaseError = {
                onError(it.message!!)
            })
    }

    fun getRobotWeatherStationTemperature(
        onSuccess: (RobotWeatherStationTemperature) -> Unit,
        onError: (String) -> Unit
    ) {
        cloudDatabase.getRobotWeatherStationTemperatureData(onDatabaseSucess = {
            onSuccess(it.toDomainModel())
        },
            onDatabaseError = {
                onError(it.message!!)
            })
    }

    fun getUserInfo(
        onSuccess: (UserInfo) -> Unit,
        onError: (String) -> Unit
    ) {
        cloudDatabase.getUserData(onDatabaseSucess = {
            onSuccess(it.toDomainModel())
        },
            onDatabaseError = {
                onError(it.message!!)
            })
    }

    fun saveUserInfo(
        data: UserInfo,
        user: String,
        onSucess: () -> Unit,
        onError: (String) -> Unit
    ) {
        cloudDatabase.saveUserData(
            onDatabaseSucess = {
                onSucess()
            },
            onDatabaseError = {
                onError(it.message!!)
            }, data = data.toCommunicationModel(),
            user = user
        )
    }

    fun deleteUserInfo(
        onSucess: () -> Unit,
        onError: (String) -> Unit
    ) {
        cloudDatabase.deleteUserData(onDatabaseSucess = {
            onSucess()
        },
            onDatabaseError = {
                onError(it.message!!)
            })
    }

    fun getTaskItem(
        onGetTaskItem: (TaskItem) -> Unit,
        onGetTaskItemError: (error: String) -> Unit
    ) {
        cloudDatabase.getTaskItem(
            onGetTaskItem = { onGetTaskItem(it.toDomainModel()) },
            onDatabaseError = {
                onGetTaskItemError(it.message!!)
            })
    }

    fun getTaskDetails(
        taskId: String,
        onGetTaskDetails: (TaskDetails) -> Unit,
        onGetTaskDetailsError: (error: String) -> Unit
    ) {
        cloudDatabase.getTaskDetails(
            taskId,
            onGotTaskDetails = { onGetTaskDetails(it.toDomainModel()) }, onDatabaseError = {
                onGetTaskDetailsError(it.message!!)
            })
    }

    fun deleteTaskItem(
        taskName: String,
        onDeleteSucess: () -> Unit,
        onDeleteError: (error: String) -> Unit
    ) {
        cloudDatabase.deleteSelectedTask(taskName, onDeleteSucess, onDeleteError = {
            onDeleteError(it.message!!)
        })
    }

    fun getActualTaskDetails(
        onGetCurrentTaskDetailsSucess: (TaskDetails) -> Unit,
        onGetCurrentTaskNameSucess: (taskName: String) -> Unit,
        onError: (error: String) -> Unit
    ) {
        cloudDatabase.getCurrentTaskDetails(
            onGetCurrentTaskNameSucess = onGetCurrentTaskNameSucess,
            onGetCurrentTaskDetailsSucess = { onGetCurrentTaskDetailsSucess(it.toDomainModel()) },
            onDatabaseError = {
                onError(it.message!!)
            })
    }

    fun addTask(taskName: String, newRobotTaskDetails: NewRobotTaskDetails) {
        cloudDatabase.createNewTask(
            taskName,
            newRobotTaskDetails.toCommunicationModel(),
            onDatabaseError = { cloudStorageErrorHandler.getStorageError(it as Exception) }
        )
    }

    fun setCurrentTask(actualTask: ActualTask) {
        cloudDatabase.setCurrenttask(
            actualTaskCDB = actualTask.toCommunicationModel(),
            onDatabaseError = { cloudStorageErrorHandler.getStorageError(it as Exception) }
        )
    }
}
