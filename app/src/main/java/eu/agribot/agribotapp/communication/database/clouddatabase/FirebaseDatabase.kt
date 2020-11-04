package eu.agribot.agribotapp.communication.database.clouddatabase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore.getInstance
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabasePaths.DOCUMENT_ADI75R1_HEALTH_ENGINE
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabasePaths.DOCUMENT_ADI75R1_LOCALIZATION_POSE
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabasePaths.DOCUMENT_ADI75R1_TASKS_LIST
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabasePaths.DOCUMENT_ADI75R1_WEATHER_STATION_HUMIDITYE
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabasePaths.DOCUMENT_ADI75R1_WEATHER_STATION_PRESSURE
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabasePaths.DOCUMENT_ADI75R1_WEATHER_STATION_TEMPERATURE
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabasePaths.DOCUMENT_ADI75R1_WEATHER_STATION_WIND
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabasePaths.DOCUMENT_ADI75R2_LOCALIZATION_POSE
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabasePaths.DOCUMENT_HEALTH
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabasePaths.DOCUMENT_ROBOTS_ADI75R1
import eu.agribot.agribotapp.communication.database.clouddatabase.model.ActualTaskCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.EngineHealthCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.RobotHealthEngineCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.RobotWeatherStationHumidityCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.RobotWeatherStationPressureCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.RobotWeatherStationTemperatureCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.RobotWeatherStationWindCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.RobotLocalizationPoseCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.TaskDetailsCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.TaskItemCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.UserInfoCDB
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabaseCollection.COLLECTION_ENGINE
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabaseCollection.COLLECTION_ROBOTS
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabaseCollection.COLLECTION_TASK_ITEM
import eu.agribot.agribotapp.communication.database.clouddatabase.model.paths.CloudDatabaseCollection.COLLECTION_USER
import eu.agribot.agribotapp.communication.database.clouddatabase.model.NewRobotTaskDetailsCDB

class FirebaseDatabase() : CloudDatabase {
    private val currentFirebaseUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val database = getInstance()

/*
    private fun getDataFromCloudDatabase(databaseCollection : String, databaseDocument : String, onDatabaseSucess : () -> Unit, onDatabaseError: (Throwable) -> Unit){
        database.collection(param1)
            .document(param2)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    param3(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(FirstRobotWeatherStationHumidityCDB::class.java)?.let {
                        param4(it)
                    }
                }
            }
    }


 */
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
        onDatabaseSucess: (data: RobotHealthEngineCDB) -> Unit,
        onDatabaseError: (Throwable) -> Unit
    ) {
        database.collection(COLLECTION_ROBOTS).document(DOCUMENT_ADI75R1_HEALTH_ENGINE)
            .addSnapshotListener { snapshot, databaseException ->
                if (databaseException != null) {
                    onDatabaseError(Throwable("Could not parse firebase data"))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(RobotHealthEngineCDB::class.java)?.let {
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
        onDatabaseSucess: (data: RobotWeatherStationHumidityCDB) -> Unit,
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
                    snapshot.toObject(RobotWeatherStationHumidityCDB::class.java)?.let {
                        onDatabaseSucess(it)
                    }
                }
            }
    }

    override fun getFirstRobotWeatherStationPressureData(
        onDatabaseSucess: (RobotWeatherStationPressureCDB) -> Unit,
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
                    snapshot.toObject(RobotWeatherStationPressureCDB::class.java)?.let {
                        onDatabaseSucess(it)
                    }
                }
            }
    }

    override fun getFirstRobotWeatherStationWindData(
        onDatabaseSucess: (RobotWeatherStationWindCDB) -> Unit,
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
                    snapshot.toObject(RobotWeatherStationWindCDB::class.java)?.let {
                        onDatabaseSucess(it)
                    }
                }
            }
    }

    override fun getFirstRobotWeatherStationTemperatureData(
        onDatabaseSucess: (data: RobotWeatherStationTemperatureCDB) -> Unit,
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
                    snapshot.toObject(RobotWeatherStationTemperatureCDB::class.java)?.let {
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
                        Log.e("xd", "IT: " + it.actual_task)
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
}
