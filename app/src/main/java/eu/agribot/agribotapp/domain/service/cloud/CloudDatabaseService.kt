package eu.agribot.agribotapp.domain.service.cloud

import android.util.Log
import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabase
import eu.agribot.agribotapp.domain.model.EngineHealth
import eu.agribot.agribotapp.domain.model.FirstRobotHealthEngine
import eu.agribot.agribotapp.domain.model.FirstRobotWeatherStationHumidity
import eu.agribot.agribotapp.domain.model.FirstRobotWeatherStationTemperature
import eu.agribot.agribotapp.domain.model.FirstRobotWeatherStationWind
import eu.agribot.agribotapp.domain.model.RobotLocalizationPose
import eu.agribot.agribotapp.domain.model.TaskDetails
import eu.agribot.agribotapp.domain.model.TaskItem
import eu.agribot.agribotapp.domain.model.UserInfo
import eu.agribot.agribotapp.domain.model.cdb.FirstRobotWeatherStationPressure
import eu.agribot.agribotapp.domain.model.program.weather.toCommunication.NewRobotTaskDetails

class CloudDatabaseService(
    val cloudDatabase: CloudDatabase
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

    fun getActualTaskName(
        onGetCurrentTaskSucess: (TaskDetails) -> Unit,
        onError: (error: String) -> Unit
    ) {
        cloudDatabase.getCurrentTaskName(
            robotName = "ADI75R1",
            onGetCurrentTaskSucess = { onGetCurrentTaskSucess(it.toDomainModel()) },
            onDatabaseError = {
                onError(it.message!!)
            })
    }

    fun programTaskToRobot(taskName: String, newRobotTaskDetails: NewRobotTaskDetails) {
        Log.e("xd", "programTask")
        cloudDatabase.createNewTask(taskName, newRobotTaskDetails.toCommunicationModel(), onDatabaseSucess = this :: onsucess, onDatabaseError = this::onError)
    }

    fun onsucess() {
        Log.e("xd", "send sucess")
    }

    fun onError(error: Throwable) {
        Log.e("xd", "send error" + error)
    }
}
