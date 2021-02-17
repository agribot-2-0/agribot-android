package eu.agribot.agribotapp.communication.database.clouddatabase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore.getInstance
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
import timber.log.Timber
import java.io.IOException

class FirebaseDatabase : CloudDatabase {
    private val currentFirebaseUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val database = getInstance()
    var userRobotName: String? = null

    override fun getUserData(
        onDatabaseSucess: (data: UserInfoCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_USER)
            .document("$currentFirebaseUser/$COLLECTION_ROBOTS/$userRobotName")
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(UserInfoCDB::class.java)?.let {
                        onDatabaseSucess(it)
                    }
                }
            }
    }

    override fun getEngineHealthData(
        onDatabaseSuccess: (data: EngineHealthCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS).document(userRobotName + DOCUMENT_STATE_ENGINE)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(EngineHealthCDB::class.java)?.let {
                        onDatabaseSuccess(it)
                    }
                }
            }
    }

    override fun getRobotStateEngineData(
        onDatabaseSuccess: (data: RobotEngineCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(userRobotName + DOCUMENT_STATE_ENGINE)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(RobotEngineCDB::class.java)?.let {
                        onDatabaseSuccess(it)
                    }
                }
            }
    }

    override fun getRobotStateHydraulicsData(
        onDatabaseSuccess: (data: RobotHydraulicsCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(userRobotName + DOCUMENT_STATE_HYDRAULICS)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(RobotHydraulicsCDB::class.java)?.let {
                        onDatabaseSuccess(it)
                    }
                }
            }
    }

    override fun getRobotStateTaskData(
        onDatabaseSuccess: (data: RobotTaskCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(userRobotName + DOCUMENT_STATE_TASK)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(RobotTaskCDB::class.java)?.let {
                        onDatabaseSuccess(it)
                    }
                }
            }
    }

    override fun getRobotLocalizationPoseData(
        onDatabaseSuccess: (data: RobotLocalizationPoseCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(userRobotName + DOCUMENT_LOCALIZATION_POSE)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(RobotLocalizationPoseCDB::class.java)?.let {
                        onDatabaseSuccess(it)
                    }
                }
            }
    }

    override fun getRobotWeatherStationHumidityData(
        onDatabaseSuccess: (data: RobotWeatherStationHumidityCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(userRobotName + DOCUMENT_WEATHER_STATION_HUMIDITYE)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(RobotWeatherStationHumidityCDB::class.java)?.let {
                        onDatabaseSuccess(it)
                    }
                }
            }
    }

    override fun getRobotWeatherStationPressureData(
        onDatabaseSuccess: (RobotWeatherStationPressureCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(userRobotName + DOCUMENT_WEATHER_STATION_PRESSURE)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(RobotWeatherStationPressureCDB::class.java)?.let {
                        onDatabaseSuccess(it)
                    }
                }
            }
    }

    override fun getRobotWeatherStationWindData(
        onDatabaseSuccess: (RobotWeatherStationWindCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(userRobotName + DOCUMENT_WEATHER_STATION_WIND)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(RobotWeatherStationWindCDB::class.java)?.let {
                        onDatabaseSuccess(it)
                    }
                }
            }
    }

    override fun getRobotWeatherStationTemperatureData(
        onDatabaseSuccess: (data: RobotWeatherStationTemperatureCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(userRobotName + DOCUMENT_WEATHER_STATION_TEMPERATURE)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(RobotWeatherStationTemperatureCDB::class.java)?.let {
                        onDatabaseSuccess(it)
                    }
                }
            }
    }

    override fun saveUserData(
        data: UserInfoCDB,
        user: String,
        onDatabaseSuccess: () -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_USER)
            .document("$user/$COLLECTION_ROBOTS/$userRobotName")
            .set(data)
            .addOnSuccessListener {
                onDatabaseSuccess.invoke()
            }
            .addOnFailureListener { exception ->
                onDatabaseError(exception)
            }
    }

    override fun deleteUserData(
        onDatabaseSuccess: () -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_USER)
            .document("$currentFirebaseUser/$COLLECTION_ROBOTS/$userRobotName")
            .delete()
            .addOnSuccessListener {
                onDatabaseSuccess.invoke()
            }
            .addOnFailureListener { exception ->
                onDatabaseError(exception)
            }
    }

    override fun createNewTask(
        taskName: String,
        newRobotTaskDetailsCDB: NewRobotTaskDetailsCDB,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document("$userRobotName/$DOCUMENT_TASKS/$taskName")
            .set(newRobotTaskDetailsCDB)
            .addOnFailureListener { exception ->
                onDatabaseError(exception)
            }
    }

    override fun getTaskDetails(
        taskID: String,
        onGotTaskDetails: (taskDetailsCDB: TaskDetailsCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document("$userRobotName/$DOCUMENT_TASKS/$taskID")
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
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
            .document(userRobotName!!)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(databaseException)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(ActualTaskCDB::class.java)?.let { it ->
                        if (it.actual_task != "") {
                            onGetCurrentTaskNameSucess(it.actual_task)
                            getTaskDetails(
                                it.actual_task,
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

    override fun setCurrenttask(
        actualTaskCDB: ActualTaskCDB,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(userRobotName!!)
            .update(
                mapOf(
                    "actual_task" to actualTaskCDB.actual_task
                )
            )
            .addOnFailureListener { exception ->
                onDatabaseError(exception)
            }
    }

    override fun deleteSelectedTask(
        taskID: String,
        onDeleteSuccess: () -> Unit,
        onDeleteError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document("$userRobotName/$DOCUMENT_TASKS/$taskID")
            .delete()
            .addOnSuccessListener {
                onDeleteSuccess()
            }
            .addOnFailureListener { exception ->
                onDeleteError(exception)
            }
    }

    override fun getTaskItem(
        onGetTaskItem: (taskItemCDB: TaskItemCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection("$COLLECTION_ROBOTS/$userRobotName/$DOCUMENT_TASKS")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    database.collection(COLLECTION_ROBOTS)
                        .document(userRobotName + "/" + DOCUMENT_TASKS + "/" + document.id)
                        .addSnapshotListener { snapshot, databaseException ->
                            if (databaseException != null) {
                                onDatabaseError(Throwable("Could not parse firebase data"))
                                return@addSnapshotListener
                            }
                            if (snapshot != null && snapshot.exists()) {
                                snapshot.toObject(TaskDetailsCDB::class.java)?.let {
                                    onGetTaskItem(
                                        TaskItemCDB(
                                            taskName = document.id,
                                            pathName = it.path_name
                                        )
                                    )
                                }
                            }
                        }
                }
            }
    }

    override fun addTool(
        toolName: String,
        toolDetailsCDB: ToolDetailsCDB,
        onDatabaseSucess: () -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_USER)
            .document("$currentFirebaseUser/$DOCUMENT_TOOLS/$toolName")
            .set(toolDetailsCDB)
            .addOnSuccessListener {
                onDatabaseSucess()
            }
            .addOnFailureListener { exception ->
                onDatabaseError(exception)
            }
    }

    override fun getToolItem(
        onGetToolItem: (toolItemCDB: TooltemCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection("$COLLECTION_USER/$currentFirebaseUser/$DOCUMENT_TOOLS")
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
    }

    override fun deleteTool(
        toolName: String,
        onDeleteSucess: () -> Unit,
        onDeleteError: (Throwable) -> Unit
    ) {
        database.collection("$COLLECTION_USER/$currentFirebaseUser/$DOCUMENT_TOOLS")
            .document(toolName)
            .delete()
            .addOnSuccessListener {
                onDeleteSucess()
            }
            .addOnFailureListener { exception ->
                onDeleteError(exception)
            }
    }

    override fun getToolDetails(
        toolName: String,
        onGetToolDetailsSucess: (toolDetailsCDB: ToolDetailsCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection("$COLLECTION_USER/$currentFirebaseUser/$DOCUMENT_TOOLS")
            .document(toolName)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
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
            .document(userRobotName + DOCUMENT_LOCALIZATION_POSE)
            .get()
            .addOnSuccessListener { it ->
                it.toObject(RobotLocalizationPoseCDB::class.java).let { robotLocalization ->
                    if (robotLocalization != null) {
                        onGotRobotCoordinate(robotLocalization)
                    }
                }
            }
            .addOnFailureListener(onDatabaseError)
    }

    override fun getUsersRobotName(onGetUsserRobotNAmeSucess: () -> Unit) {
        try {
            database.collection("$COLLECTION_USER/$currentFirebaseUser/$COLLECTION_ROBOTS")
                .get()
                .addOnSuccessListener { result ->
                    userRobotName = result.documents[0].id
                    onGetUsserRobotNAmeSucess.invoke()
                }
        } catch (exception: IOException) {
            Timber.e("Robot name problem %s", exception)
        }
    }
}
