package eu.agribot.agribotapp.communication.database.clouddatabase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore.getInstance
import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabasePaths.COLLECTION_ENGINE
import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabasePaths.COLLECTION_ROBOTS
import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabasePaths.COLLECTION_TASK_ITEM
import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabasePaths.COLLECTION_USER
import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabasePaths.DOCUMENT_ADI75R1_HEALTH_ENGINE
import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabasePaths.DOCUMENT_ADI75R1_LOCALIZATION_POSE
import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabasePaths.DOCUMENT_ADI75R1_TASKS_LIST
import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabasePaths.DOCUMENT_ADI75R1_WEATHER_STATION_HUMIDITYE
import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabasePaths.DOCUMENT_ADI75R1_WEATHER_STATION_PRESSURE
import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabasePaths.DOCUMENT_ADI75R1_WEATHER_STATION_TEMPERATURE
import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabasePaths.DOCUMENT_ADI75R1_WEATHER_STATION_WIND
import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabasePaths.DOCUMENT_ADI75R2_LOCALIZATION_POSE
import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabasePaths.DOCUMENT_HEALTH
import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabasePaths.DOCUMENT_ROBOTS_ADI75R1
import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabasePaths.DOCUMENT_TOOLS
import eu.agribot.agribotapp.communication.database.clouddatabase.model.*
import eu.agribot.agribotapp.communication.database.clouddatabase.model.programtasks.NewRobotTaskDetailsCDB

class FirebaseDatabase() : CloudDatabase {
    private val currentFirebaseUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val database = getInstance()

    override fun getUserData(
        onDatabaseSucess: (data: UserInfoCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_USER).document(currentFirebaseUser + DOCUMENT_ROBOTS_ADI75R1)
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
        onDatabaseSucess: (data: EngineHealthCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ENGINE).document(DOCUMENT_HEALTH)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(EngineHealthCDB::class.java)?.let {
                        onDatabaseSucess(it)
                    }
                }
            }
    }

    override fun getFirstRobotHealthEngineData(
        onDatabaseSucess: (data: FirstRobotHealthEngineCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS).document(DOCUMENT_ADI75R1_HEALTH_ENGINE)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(FirstRobotHealthEngineCDB::class.java)?.let {
                        onDatabaseSucess(it)
                    }
                }
            }
    }

    override fun getFirstRobotLocalizationPoseData(
        onDatabaseSucess: (data: RobotLocalizationPoseCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS).document(DOCUMENT_ADI75R1_LOCALIZATION_POSE)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(RobotLocalizationPoseCDB::class.java)?.let {
                        onDatabaseSucess(it)
                    }
                }
            }
    }

    override fun getFirstRobotWeatherStationHumidityData(
        onDatabaseSucess: (data: FirstRobotWeatherStationHumidityCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(DOCUMENT_ADI75R1_WEATHER_STATION_HUMIDITYE)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(FirstRobotWeatherStationHumidityCDB::class.java)?.let {
                        onDatabaseSucess(it)
                    }
                }
            }
    }

    override fun getFirstRobotWeatherStationPressureData(
        onDatabaseSucess: (FirstRobotWeatherStationPressureCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(DOCUMENT_ADI75R1_WEATHER_STATION_PRESSURE)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(FirstRobotWeatherStationPressureCDB::class.java)?.let {
                        onDatabaseSucess(it)
                    }
                }
            }
    }

    override fun getFirstRobotWeatherStationWindData(
        onDatabaseSucess: (FirstRobotWeatherStationWindCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(DOCUMENT_ADI75R1_WEATHER_STATION_WIND)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(FirstRobotWeatherStationWindCDB::class.java)?.let {
                        onDatabaseSucess(it)
                    }
                }
            }
    }

    override fun getFirstRobotWeatherStationTemperatureData(
        onDatabaseSucess: (data: FirstRobotWeatherStationTemperatureCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(DOCUMENT_ADI75R1_WEATHER_STATION_TEMPERATURE)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(FirstRobotWeatherStationTemperatureCDB::class.java)?.let {
                        onDatabaseSucess(it)
                    }
                }
            }
    }

    override fun getSecondRobotLocalizationPoseData(
        onDatabaseSucess: (data: RobotLocalizationPoseCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(DOCUMENT_ADI75R2_LOCALIZATION_POSE)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(RobotLocalizationPoseCDB::class.java)?.let {
                        onDatabaseSucess(it)
                    }
                }
            }
    }

    override fun saveUserData(
        data: UserInfoCDB,
        user: String,
        onDatabaseSucess: () -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_USER)
            .document(user + DOCUMENT_ROBOTS_ADI75R1)
            .set(data)
            .addOnSuccessListener {
                onDatabaseSucess.invoke()
            }
            .addOnFailureListener { exception ->
                onDatabaseError(exception)
            }
    }

    override fun deleteUserData(
        onDatabaseSucess: () -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_USER)
            .document(currentFirebaseUser + DOCUMENT_ROBOTS_ADI75R1)
            .delete()
            .addOnSuccessListener {
                onDatabaseSucess.invoke()
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
            .document(DOCUMENT_ADI75R1_TASKS_LIST + taskName)
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
            .document(DOCUMENT_ADI75R1_TASKS_LIST + taskID)
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
        robotName: String,
        onGetCurrentTaskNameSucess: (currentTaskName: String) -> Unit,
        onGetCurrentTaskDetailsSucess: (taskDetailsCDB: TaskDetailsCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(robotName)
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
                                onGetCurrentTaskDetailsSucess,
                                onDatabaseError
                            )
                        }
                    }
                }
            }
    }

    override fun setCurrenttask(
        robotName: String,
        actualTaskCDB: ActualTaskCDB,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(robotName)
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
        onDeleteSucess: () -> Unit,
        onDeleteError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS)
            .document(DOCUMENT_ADI75R1_TASKS_LIST + taskID)
            .delete()
            .addOnSuccessListener {
                onDeleteSucess()
            }
            .addOnFailureListener { exception ->
                onDeleteError(exception)
            }
    }

    override fun getTaskItem(
        onGetTaskItem: (taskItemCDB: TaskItemCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_TASK_ITEM)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    database.collection(COLLECTION_ROBOTS)
                        .document(DOCUMENT_ADI75R1_TASKS_LIST + document.id)
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
                    Log.w("FirebaseDatabase", document.id)
                    onGetToolItem(
                        TooltemCDB(
                            toolName = document.id,
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


}
