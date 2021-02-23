package eu.agribot.agribotapp.domain.service.cloud

import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabase
import eu.agribot.agribotapp.domain.error.CloudStorageErrorHandler
import eu.agribot.agribotapp.domain.model.cdb.ActualTask
import eu.agribot.agribotapp.domain.model.cdb.RobotLocalizationPose
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationHumidity
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationPressure
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationTemperature
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationWind
import eu.agribot.agribotapp.domain.model.cdb.TaskDetails
import eu.agribot.agribotapp.domain.model.cdb.TaskItem
import eu.agribot.agribotapp.domain.model.cdb.ToolDetails
import eu.agribot.agribotapp.domain.model.cdb.ToolItem
import eu.agribot.agribotapp.domain.model.cdb.UserInfo
import eu.agribot.agribotapp.domain.model.cdb.state.RobotEngine
import eu.agribot.agribotapp.domain.model.cdb.state.RobotHydraulics
import eu.agribot.agribotapp.domain.model.cdb.state.RobotTask
import eu.agribot.agribotapp.domain.model.cdb.taskprogram.taskmodel.NewRobotTaskDetails

class CloudDatabaseService(
    val cloudDatabase: CloudDatabase,
    val cloudStorageErrorHandler: CloudStorageErrorHandler
) {

    fun getUserRobotName(onGotRobotName: () -> Unit) {
        cloudDatabase.getUsersRobotName(onGotRobotName)
    }

    fun getRobotStateEngine(
        onSuccess: (RobotEngine) -> Unit,
        onError: (String) -> Unit
    ) {
        cloudDatabase.getRobotStateEngineData(onDatabaseSuccess = {
            onSuccess(it.toDomainModel())
        },
            onDatabaseError = {
                onError(it.message!!)
            })
    }

    fun getRobotStateHydraulics(
        onSuccess: (RobotHydraulics) -> Unit,
        onError: (String) -> Unit
    ) {
        cloudDatabase.getRobotStateHydraulicsData(onDatabaseSuccess = {
            onSuccess(it.toDomainModel())
        },
            onDatabaseError = {
                onError(it.message!!)
            })
    }

    fun getRobotStateTask(
        onSuccess: (RobotTask) -> Unit,
        onError: (String) -> Unit
    ) {
        cloudDatabase.getRobotStateTaskData(onDatabaseSuccess = {
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
        cloudDatabase.getRobotLocalizationPoseData(onDatabaseSuccess = {
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
        cloudDatabase.getRobotWeatherStationHumidityData(onDatabaseSuccess = {
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
        cloudDatabase.getRobotWeatherStationWindData(onDatabaseSuccess = {
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
        cloudDatabase.getRobotWeatherStationPressureData(onDatabaseSuccess = {
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
        cloudDatabase.getRobotWeatherStationTemperatureData(onDatabaseSuccess = {
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
            onDatabaseSuccess = {
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
        cloudDatabase.deleteUserData(onDatabaseSuccess = {
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
        onGetCurrentTaskDetailsSuccess: (TaskDetails) -> Unit,
        onGetCurrentTaskNameSuccess: (taskName: String) -> Unit,
        onError: (error: String) -> Unit
    ) {
        cloudDatabase.getCurrentTaskDetails(
            onGetCurrentTaskNameSucess = onGetCurrentTaskNameSuccess,
            onGetCurrentTaskDetailsSuccess = { onGetCurrentTaskDetailsSuccess(it.toDomainModel()) },
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
        cloudDatabase.setCurrentTask(
            actualTaskCDB = actualTask.toCommunicationModel(),
            onDatabaseError = { cloudStorageErrorHandler.getStorageError(it as Exception) }
        )
    }

    fun addTool(
        toolName: String,
        toolDetails: ToolDetails,
        onSucess: () -> Unit
    ) {
        cloudDatabase.addTool(
            toolName,
            toolDetails.toCommunicationModel(),
            onDatabaseSucess = { onSucess() },
            onDatabaseError = { cloudStorageErrorHandler.getStorageError(it as Exception) }
        )
    }

    fun getToolItem(
        onGetToolItem: (ToolItem) -> Unit,
        onGetToolItemError: (error: String) -> Unit
    ) {
        cloudDatabase.getToolItem(
            onGetToolItem = { onGetToolItem(it.toDomainModel()) },
            onDatabaseError = {
                onGetToolItemError(it.message!!)
            })
    }

    fun deleteTool(
        toolName: String,
        onDeleteSucess: () -> Unit,
        onDeleteError: (error: String) -> Unit
    ) {
        cloudDatabase.deleteTool(
            toolName,
            onDeleteSucess,
            onDeleteError = {
                onDeleteError(it.message!!)
            })
    }

    fun getToolDetails(
        toolName: String,
        onGetToolDetails: (ToolDetails) -> Unit,
        onGetToolDetailsError: (error: String) -> Unit
    ) {
        cloudDatabase.getToolDetails(
            toolName,
            onGetToolDetailsSucess = { onGetToolDetails(it.toDomainModel()) },
            onDatabaseError = {
                onGetToolDetailsError(it.message!!)
            })
    }

    fun getOnceRobotLocalization(
        onGotRobotLocalization: (RobotLocalizationPose) -> Unit,
        onGetToolDetailsError: (error: String) -> Unit
    ) {
        return cloudDatabase.getRobotOnceRobotCoordinate(
            onGotRobotCoordinate = {
                onGotRobotLocalization(
                    it.toDomainModel()
                )
            },
            onDatabaseError = { onGetToolDetailsError(cloudStorageErrorHandler.getStorageError(it)) })
    }

    fun getCurrentTaskName(onGotCurrentTaskName: (taskName: String) -> Unit) {
        cloudDatabase.getCurrentTaskName(onGotCurrentTaskName)
    }
}
