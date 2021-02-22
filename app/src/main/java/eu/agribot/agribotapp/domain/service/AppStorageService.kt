package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabase
import eu.agribot.agribotapp.communication.persistence.PersistentStorage

class AppStorageService(
    private val persistentStorage: PersistentStorage,
    private val cloudDatabase: CloudDatabase,
    private val authenticationService: AuthenticationService
) {

    fun getStreamUrl(): String = persistentStorage.getVideoStreamUrl()

    fun saveStreamUrl(newUrl: String) {
        persistentStorage.saveVideoStreamUrl(newUrl)
    }

    fun getRobotSerialNumber(): String = persistentStorage.getRobotSerialNumber()

    fun saveRobotSerialNumber(newRobotSerialNumber: String) {
        persistentStorage.saveRobotSerialNumber(newRobotSerialNumber)
    }

     fun saveRobotSerialNumberOnCloudDatabase() {
        cloudDatabase.setUserRobotName(persistentStorage.getRobotSerialNumber(),authenticationService.getCurrentUser().userUID)
    }
}
