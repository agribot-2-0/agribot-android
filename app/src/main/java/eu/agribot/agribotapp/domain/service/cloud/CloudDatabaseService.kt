package eu.agribot.agribotapp.domain.service.cloud

import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabase
import eu.agribot.agribotapp.domain.error.CloudStorageErrorHandler
import eu.agribot.agribotapp.domain.model.cdb.ActualTask
import eu.agribot.agribotapp.domain.model.cdb.EngineHealth
import eu.agribot.agribotapp.domain.model.cdb.FirstRobotHealthEngine
import eu.agribot.agribotapp.domain.model.cdb.FirstRobotWeatherStationHumidity
import eu.agribot.agribotapp.domain.model.cdb.FirstRobotWeatherStationPressure
import eu.agribot.agribotapp.domain.model.cdb.FirstRobotWeatherStationTemperature
import eu.agribot.agribotapp.domain.model.cdb.FirstRobotWeatherStationWind
import eu.agribot.agribotapp.domain.model.cdb.RobotLocalizationPose
import eu.agribot.agribotapp.domain.model.cdb.TaskDetails
import eu.agribot.agribotapp.domain.model.cdb.TaskItem
import eu.agribot.agribotapp.domain.model.cdb.UserInfo
import eu.agribot.agribotapp.domain.model.cdb.taskprogram.taskmodel.NewRobotTaskDetails

class CloudDatabaseService(
    val cloudDatabase: CloudDatabase,
    val cloudStorageErrorHandler: CloudStorageErrorHandler
) {
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

    fun getFirstRobotHealthEngine(
        onSuccess: (FirstRobotHealthEngine) -> Unit,
        onError: (String) -> Unit
    ) {
        cloudDatabase.getFirstRobotHealthEngineData(onDatabaseSucess = {
            onSuccess(it.toDomainModel())
        },
            onDatabaseError = {
                onError(it.message!!)
            })
    }

    fun getFirstRobotLocalizationPose(
        onSuccess: (RobotLocalizationPose) -> Unit,
        onError: (String) -> Unit
    ) {
        cloudDatabase.getFirstRobotLocalizationPoseData(onDatabaseSucess = {
            onSuccess(it.toDomainModel())
        },
            onDatabaseError = {
                onError(it.message!!)
            })
    }

    fun getFirstRobotWeatherStationHumidity(
        onSuccess: (FirstRobotWeatherStationHumidity) -> Unit,
        onError: (String) -> Unit
    ) {
        cloudDatabase.getFirstRobotWeatherStationHumidityData(onDatabaseSucess = {
            onSuccess(it.toDomainModel())
        },
            onDatabaseError = {
                onError(it.message!!)
            })
    }

    fun getFirstRobotWeatherStationWind(
        onSuccess: (FirstRobotWeatherStationWind) -> Unit,
        onError: (String) -> Unit
    ) {
        cloudDatabase.getFirstRobotWeatherStationWindData(onDatabaseSucess = {
            onSuccess(it.toDomainModel())
        },
            onDatabaseError = {
                onError(it.message!!)
            })
    }

    fun getFirstRobotWeatherStationPressure(
        onSuccess: (FirstRobotWeatherStationPressure) -> Unit,
        onError: (String) -> Unit
    ) {
        cloudDatabase.getFirstRobotWeatherStationPressureData(onDatabaseSucess = {
            onSuccess(it.toDomainModel())
        },
            onDatabaseError = {
                onError(it.message!!)
            })
    }

    fun getFirstRobotWeatherStationTemperature(
        onSuccess: (FirstRobotWeatherStationTemperature) -> Unit,
        onError: (String) -> Unit
    ) {
        cloudDatabase.getFirstRobotWeatherStationTemperatureData(onDatabaseSucess = {
            onSuccess(it.toDomainModel())
        },
            onDatabaseError = {
                onError(it.message!!)
            })
    }

    fun getSecondRobotLocalizationPose(
        onSuccess: (RobotLocalizationPose) -> Unit,
        onError: (String) -> Unit
    ) {
        cloudDatabase.getSecondRobotLocalizationPoseData(onDatabaseSucess = {
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

    fun deleteSelectedTask(
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
            robotName = "ADI75R1",
            onGetCurrentTaskNameSucess = onGetCurrentTaskNameSucess,
            onGetCurrentTaskDetailsSucess = { onGetCurrentTaskDetailsSucess(it.toDomainModel()) },
            onDatabaseError = {
                onError(it.message!!)
            })
    }

    fun programTaskToRobot(taskName: String, newRobotTaskDetails: NewRobotTaskDetails) {
        cloudDatabase.createNewTask(
            taskName,
            newRobotTaskDetails.toCommunicationModel(),
            onDatabaseError = { cloudStorageErrorHandler.getStorageError(it as Exception) }
        )
    }

    fun setCurrentTask(actualTask: ActualTask) {
        cloudDatabase.setCurrenttask(
            actualTaskCDB = actualTask.toCommunicationModel(),
            robotName = "ADI75R1",
            onDatabaseError = { cloudStorageErrorHandler.getStorageError(it as Exception) }
        )
    }
}
