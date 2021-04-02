package eu.agribot.agribotapp.communication.database.clouddatabase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore.getInstance
import eu.agribot.agribotapp.communication.crashlystic.CrashlysticService
import eu.agribot.agribotapp.communication.database.clouddatabase.model.ActualTaskCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.EngineHealthCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.NewRobotTaskDetailsCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.RobotLocalizationPoseCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.RobotWeatherStationHumidityCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.RobotWeatherStationPressureCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.RobotWeatherStationTemperatureCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.RobotWeatherStationWindCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.TaskDetailsCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.TaskItemCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.ToolDetailsCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.TooltemCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.UserInfoCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.UserRobotNameCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.UserRoleCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabaseCollection.COLLECTION_ROBOTS
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabaseCollection.COLLECTION_USER
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabasePaths.DOCUMENT_LOCALIZATION_POSE
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabasePaths.DOCUMENT_STATE_ENGINE
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabasePaths.DOCUMENT_STATE_HYDRAULICS
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabasePaths.DOCUMENT_STATE_TASK
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabasePaths.DOCUMENT_TASKS
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabasePaths.DOCUMENT_TOOLS
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabasePaths.DOCUMENT_WEATHER_STATION_HUMIDITYE
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabasePaths.DOCUMENT_WEATHER_STATION_PRESSURE
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabasePaths.DOCUMENT_WEATHER_STATION_TEMPERATURE
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabasePaths.DOCUMENT_WEATHER_STATION_WIND
import eu.agribot.agribotapp.communication.database.clouddatabase.model.state.RobotEngineCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.state.RobotHydraulicsCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.state.RobotTaskCDB
import eu.agribot.agribotapp.communication.persistence.PersistentStorage
import timber.log.Timber
import java.io.IOException
import java.util.Locale

class FirebaseDatabase(
    private val persistentStorage: PersistentStorage,
    private val crashlysticService: CrashlysticService
) : CloudDatabase {
    private val database = getInstance()

    override fun getUserData(
        onDatabaseSucess: (data: UserInfoCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        registerListenerOnDocumentChange(
            "$COLLECTION_USER/" +
                    FirebaseAuth.getInstance().currentUser?.uid.toString() + "/$COLLECTION_ROBOTS/${persistentStorage.getRobotSerialNumber()}",
            UserInfoCDB(),
            onDatabaseSucess,
            onDatabaseError,
            "Get user data exception"
        )
    }

    override fun getEngineHealthData(
        onDatabaseSuccess: (data: EngineHealthCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        registerListenerOnDocumentChange(
            "$COLLECTION_ROBOTS/" +
                    persistentStorage.getRobotSerialNumber() + DOCUMENT_STATE_ENGINE,
            EngineHealthCDB(),
            onDatabaseSuccess,
            onDatabaseError,
            "Get engine health data exception"
        )
    }

    override fun getRobotStateEngineData(
        onDatabaseSuccess: (data: RobotEngineCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        registerListenerOnDocumentChange(
            "$COLLECTION_ROBOTS/" +
                    persistentStorage.getRobotSerialNumber() + DOCUMENT_STATE_ENGINE,
            RobotEngineCDB(),
            onDatabaseSuccess,
            onDatabaseError,
            "Get robot state engine data exception"
        )
    }

    override fun getRobotStateHydraulicsData(
        onDatabaseSuccess: (data: RobotHydraulicsCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        registerListenerOnDocumentChange(
            "$COLLECTION_ROBOTS/" +
                    persistentStorage.getRobotSerialNumber() + DOCUMENT_STATE_HYDRAULICS,
            RobotHydraulicsCDB(),
            onDatabaseSuccess,
            onDatabaseError,
            "Get robot state hydraulic data exception"
        )
    }

    override fun getRobotStateTaskData(
        onDatabaseSuccess: (data: RobotTaskCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        registerListenerOnDocumentChange(
            "$COLLECTION_ROBOTS/" +
                    persistentStorage.getRobotSerialNumber() + DOCUMENT_STATE_TASK,
            RobotTaskCDB(),
            onDatabaseSuccess,
            onDatabaseError,
            " Get robot state task data exception"
        )
    }

    override fun getRobotLocalizationPoseData(
        onDatabaseSuccess: (data: RobotLocalizationPoseCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        registerListenerOnDocumentChange(
            "$COLLECTION_ROBOTS/" +
                    persistentStorage.getRobotSerialNumber() + DOCUMENT_LOCALIZATION_POSE,
            RobotLocalizationPoseCDB(),
            onDatabaseSuccess,
            onDatabaseError,
            "Get robot localization pose data exception"
        )
    }

    override fun getRobotWeatherStationHumidityData(
        onDatabaseSuccess: (data: RobotWeatherStationHumidityCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        registerListenerOnDocumentChange(
            "$COLLECTION_ROBOTS/" +
                    persistentStorage.getRobotSerialNumber() + DOCUMENT_WEATHER_STATION_HUMIDITYE,
            RobotWeatherStationHumidityCDB(),
            onDatabaseSuccess,
            onDatabaseError,
            "Get robot weather station humidity data exception"
        )
    }

    override fun getRobotWeatherStationPressureData(
        onDatabaseSuccess: (RobotWeatherStationPressureCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        registerListenerOnDocumentChange(
            "$COLLECTION_ROBOTS/" +
                    persistentStorage.getRobotSerialNumber() + DOCUMENT_WEATHER_STATION_PRESSURE,
            RobotWeatherStationPressureCDB(),
            onDatabaseSuccess,
            onDatabaseError,
            "Get robot weather station pressure data exception"
        )
    }

    override fun getRobotWeatherStationWindData(
        onDatabaseSuccess: (RobotWeatherStationWindCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        registerListenerOnDocumentChange(
            "$COLLECTION_ROBOTS/" +
                    persistentStorage.getRobotSerialNumber() + DOCUMENT_WEATHER_STATION_WIND,
            RobotWeatherStationWindCDB(),
            onDatabaseSuccess,
            onDatabaseError,
            "Get robot wather station wind data exception"
        )
    }

    override fun getRobotWeatherStationTemperatureData(
        onDatabaseSuccess: (data: RobotWeatherStationTemperatureCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        registerListenerOnDocumentChange(
            "$COLLECTION_ROBOTS/" +
                    persistentStorage.getRobotSerialNumber() + DOCUMENT_WEATHER_STATION_TEMPERATURE,
            RobotWeatherStationTemperatureCDB(),
            onDatabaseSuccess,
            onDatabaseError,
            "Get robot weather station temperature data exception"
        )
    }

    override fun saveUserData(
        data: UserInfoCDB,
        user: String,
        onDatabaseSuccess: () -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_USER)
            .document("$user/$COLLECTION_ROBOTS/" + persistentStorage.getRobotSerialNumber())
            .set(data)
            .addOnSuccessListener {
                onDatabaseSuccess.invoke()
            }
            .addOnFailureListener { saveUserDataException ->
                onDatabaseError(saveUserDataException)
                crashlysticService.recordException(
                    CrashlysticService.RegisterExceptionModel(
                        message = "Save user data Exception",
                        exception = saveUserDataException
                    )
                )
            }
    }

    override fun deleteUserData(
        onDatabaseSuccess: () -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_USER)
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString() + "/$COLLECTION_ROBOTS/" + persistentStorage.getRobotSerialNumber())
            .delete()
            .addOnSuccessListener {
                onDatabaseSuccess.invoke()
            }
            .addOnFailureListener { deleteUserDataException ->
                onDatabaseError(deleteUserDataException)
                crashlysticService.recordException(
                    CrashlysticService.RegisterExceptionModel(
                        message = "Remove user data exception",
                        exception = deleteUserDataException
                    )
                )
            }
    }

    override fun createNewTask(
        taskName: String,
        newRobotTaskDetailsCDB: NewRobotTaskDetailsCDB,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(persistentStorage.getRobotSerialNumber() + "/$DOCUMENT_TASKS/$taskName")
            .set(newRobotTaskDetailsCDB)
            .addOnFailureListener { createNewTaskExceeption ->
                onDatabaseError(createNewTaskExceeption)
                crashlysticService.recordException(
                    CrashlysticService.RegisterExceptionModel(
                        message = "Create new task Exception",
                        exception = createNewTaskExceeption
                    )
                )
            }
    }

    override fun getTaskDetails(
        taskID: String,
        onGotTaskDetails: (taskDetailsCDB: TaskDetailsCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(persistentStorage.getRobotSerialNumber() + "/$DOCUMENT_TASKS/$taskID")
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    crashlysticService.recordException(
                        CrashlysticService.RegisterExceptionModel(
                            message = "Get task details  exception",
                            exception = databaseException
                        )
                    )
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(TaskDetailsCDB::class.java)?.let {
                        onGotTaskDetails(it)
                    }
                }
            }
    }

    override fun getCurrentTaskDetails(
        onGetCurrentTaskNameSucess: (currentTaskName: String) -> Unit,
        onGetCurrentTaskDetailsSuccess: (taskDetailsCDB: TaskDetailsCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(persistentStorage.getRobotSerialNumber())
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(databaseException)
                    crashlysticService.recordException(
                        CrashlysticService.RegisterExceptionModel(
                            message = "Get current task exception",
                            exception = databaseException
                        )
                    )
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(ActualTaskCDB::class.java)?.let { it ->
                        if (it.actualTask != "") {
                            onGetCurrentTaskNameSucess(it.actualTask)
                            getTaskDetails(
                                it.actualTask,
                                onGetCurrentTaskDetailsSuccess,
                                onDatabaseError
                            )
                        } else {
                            onGetCurrentTaskNameSucess("")
                            onGetCurrentTaskDetailsSuccess(TaskDetailsCDB())
                        }
                    }
                }
            }
    }

    override fun setCurrentTask(
        actualTaskCDB: ActualTaskCDB,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(persistentStorage.getRobotSerialNumber())
            .update(
                mapOf(
                    "actual_task" to actualTaskCDB.actualTask
                )
            )
            .addOnFailureListener { setCurrentTask ->
                onDatabaseError(setCurrentTask)
                crashlysticService.recordException(
                    CrashlysticService.RegisterExceptionModel(
                        message = "Set current task exception",
                        exception = setCurrentTask
                    )
                )
            }
    }

    override fun deleteSelectedTask(
        taskID: String,
        onDeleteSuccess: () -> Unit,
        onDeleteError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(persistentStorage.getRobotSerialNumber() + "/$DOCUMENT_TASKS/$taskID")
            .delete()
            .addOnSuccessListener {
                onDeleteSuccess()
            }
            .addOnFailureListener { deleteSelectedTaskException ->
                onDeleteError(deleteSelectedTaskException)
                crashlysticService.recordException(
                    CrashlysticService.RegisterExceptionModel(
                        message = "Delete task exception",
                        exception = deleteSelectedTaskException
                    )
                )
            }
    }

    override fun getTaskItem(
        onGetTaskItem: (taskItemCDB: TaskItemCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection("$COLLECTION_ROBOTS/" + persistentStorage.getRobotSerialNumber() + "/$DOCUMENT_TASKS")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    database.collection(COLLECTION_ROBOTS)
                        .document(persistentStorage.getRobotSerialNumber() + "/" + DOCUMENT_TASKS + "/" + document.id)
                        .addSnapshotListener { snapshot, databaseException ->
                            if (databaseException != null) {
                                onDatabaseError(Throwable("Could not parse firebase data"))
                                crashlysticService.recordException(
                                    CrashlysticService.RegisterExceptionModel(
                                        message = "getTaskItem exception",
                                        exception = Throwable("Could not parse firebase data")
                                    )
                                )

                                return@addSnapshotListener
                            }
                            if (snapshot != null && snapshot.exists()) {
                                snapshot.toObject(TaskDetailsCDB::class.java)?.let {
                                    onGetTaskItem(
                                        TaskItemCDB(
                                            taskName = document.id,
                                            pathName = it.pathName
                                        )
                                    )
                                }
                            }
                        }
                }
            }
            .addOnFailureListener {
                onDatabaseError(it)
                crashlysticService.recordException(
                    CrashlysticService.RegisterExceptionModel(
                        message = "Get task item exception",
                        exception = it
                    )
                )
            }
    }

    override fun addTool(
        toolName: String,
        toolDetailsCDB: ToolDetailsCDB,
        onDatabaseSucess: () -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_USER)
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString() + "/$DOCUMENT_TOOLS/$toolName")
            .set(toolDetailsCDB)
            .addOnSuccessListener {
                onDatabaseSucess()
            }
            .addOnFailureListener { addToolException ->
                onDatabaseError(addToolException)
                crashlysticService.recordException(
                    CrashlysticService.RegisterExceptionModel(
                        message = "Add Tool to cloud database exception",
                        exception = addToolException
                    )
                )
            }
    }

    override fun getToolItem(
        onGetToolItem: (toolItemCDB: TooltemCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection("$COLLECTION_USER/" + FirebaseAuth.getInstance().currentUser?.uid.toString() + "/$DOCUMENT_TOOLS")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    onGetToolItem(
                        TooltemCDB(
                            toolName = document.id
                        )
                    )
                }
            }
            .addOnFailureListener {
                crashlysticService.recordException(
                    CrashlysticService.RegisterExceptionModel(
                        message = "Get tool item from cloud database exception",
                        exception = it
                    )
                )
            }
    }

    override fun deleteTool(
        toolName: String,
        onDeleteSucess: () -> Unit,
        onDeleteError: (Throwable) -> Unit
    ) {
        database.collection("$COLLECTION_USER/" + FirebaseAuth.getInstance().currentUser?.uid.toString() + "/$DOCUMENT_TOOLS")
            .document(toolName)
            .delete()
            .addOnSuccessListener {
                onDeleteSucess()
            }
            .addOnFailureListener { deleteToolException ->
                onDeleteError(deleteToolException)
                crashlysticService.recordException(
                    CrashlysticService.RegisterExceptionModel(
                        message = "Delete Tool on cloud database exception",
                        exception = deleteToolException
                    )
                )
            }
    }

    override fun getToolDetails(
        toolName: String,
        onGetToolDetailsSucess: (toolDetailsCDB: ToolDetailsCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        registerListenerOnDocumentChange(
            "$COLLECTION_USER/" + FirebaseAuth.getInstance().currentUser?.uid.toString() + "/$DOCUMENT_TOOLS" +
                    toolName,
            ToolDetailsCDB(), onGetToolDetailsSucess, onDatabaseError, "Get tool details exception"
        )
        database.collection("$COLLECTION_USER/" + FirebaseAuth.getInstance().currentUser?.uid.toString() + "/$DOCUMENT_TOOLS")
            .document(toolName)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    crashlysticService.recordException(
                        CrashlysticService.RegisterExceptionModel(
                            message = "Get Tool details from cloud database exception",
                            exception = databaseException
                        )
                    )
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(ToolDetailsCDB::class.java)?.let {
                        onGetToolDetailsSucess(it)
                    }
                }
            }
    }

    override fun getRobotOnceRobotCoordinate(
        onGotRobotCoordinate: (RobotLocalizationPoseCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(persistentStorage.getRobotSerialNumber() + DOCUMENT_LOCALIZATION_POSE)
            .get()
            .addOnSuccessListener {
                it.toObject(RobotLocalizationPoseCDB::class.java).let { robotLocalization ->
                    if (robotLocalization != null) {
                        onGotRobotCoordinate(robotLocalization)
                    }
                }
            }
            .addOnFailureListener {
                onDatabaseError(it)
                crashlysticService.recordException(
                    CrashlysticService.RegisterExceptionModel(
                        message = "Get robot coordinate from cloud database exception",
                        exception = it
                    )
                )
            }
    }

    override fun getUserRole(
        onGotUserRole: (UserRoleCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        registerListenerOnDocumentChange(
            "$COLLECTION_USER/" +
                    FirebaseAuth.getInstance().currentUser?.uid.toString(),
            UserRoleCDB(), onGotUserRole, onDatabaseError, "Get user role exception"
        )
        database.collection(COLLECTION_USER)
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {
                it.toObject(UserRoleCDB::class.java).let { userRole ->
                    if (userRole != null) {
                        onGotUserRole(userRole)
                    }
                }
            }
            .addOnFailureListener {
                onDatabaseError(it)
                crashlysticService.recordException(
                    CrashlysticService.RegisterExceptionModel(
                        message = "Get user role from cloud database exception",
                        exception = it
                    )
                )
            }
    }

    override fun removeUserDocuments() {
        database.collection(COLLECTION_USER)
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .delete()
            .addOnFailureListener {
                crashlysticService.recordException(
                    CrashlysticService.RegisterExceptionModel(
                        message = "Remove User documents on cloud database exception",
                        exception = it
                    )
                )
            }
    }

    override fun setUserRole(user: String, onDatabaseError: (Throwable) -> Unit) {
        database.collection(COLLECTION_USER)
            .document(user)
            .set(UserRoleCDB(role = UserRoleType.USER.name.toLowerCase(Locale.ROOT)))
            .addOnFailureListener { setUserRoleException ->
                onDatabaseError(setUserRoleException)
                crashlysticService.recordException(
                    CrashlysticService.RegisterExceptionModel(
                        message = "Set user role on cloud database exception",
                        exception = setUserRoleException
                    )
                )
            }
    }

    override fun setUserRobotName(userRobotName: String, currentUser: String) {
        database.collection("$COLLECTION_USER/")
            .document("$currentUser/$COLLECTION_ROBOTS/$userRobotName")
            .set(UserRobotNameCDB(robotName = userRobotName))
    }

    override fun getCurrentTaskName(onGotCurrentTask: (curerentTask: String) -> Unit) {
        database.collection(COLLECTION_ROBOTS)
            .document(persistentStorage.getRobotSerialNumber())
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(ActualTaskCDB::class.java)?.let { it ->
                        if (it.actualTask != "") {
                            onGotCurrentTask(it.actualTask)
                        } else {
                            onGotCurrentTask("")
                        }
                    }
                }
            }
    }

    override fun getUsersRobotName(onGetUserRobotNameSuccess: () -> Unit) {
        try {
            database.collection("$COLLECTION_USER/" + FirebaseAuth.getInstance().currentUser?.uid.toString() + "/$COLLECTION_ROBOTS")
                .get()
                .addOnSuccessListener { result ->
                    if (result != null && !result.isEmpty) {
                        persistentStorage.saveRobotSerialNumber(result.documents[0].id)
                        onGetUserRobotNameSuccess.invoke()
                    } else {
                        Timber.e("Robot name problem ")
                    }
                }
        } catch (getUserRoleException: IOException) {
            Timber.e(String.format("Robot name problem %s", getUserRoleException))
            crashlysticService.recordException(
                CrashlysticService.RegisterExceptionModel(
                    message = "Get user role from cloud database exception",
                    exception = getUserRoleException
                )
            )
        }
    }

    private fun <T> registerListenerOnDocumentChange(
        documentSource: String,
        databaseObject: T,
        onDatabaseSucess: (T) -> Unit,
        onDataBaseError: (Throwable) -> Unit,
        crashlysticMessage: String

    ) {
        database.document(documentSource)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    crashlysticService.recordException(
                        CrashlysticService.RegisterExceptionModel(
                            message = crashlysticMessage,
                            exception = databaseException
                        )
                    )
                    onDataBaseError(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(databaseObject!!::class.java)?.let {
                        onDatabaseSucess(it)
                    }
                }
            }
    }
}
