package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.database.firebase.FirebaseService
import eu.agribot.agribotapp.communication.persistence.PersistentStorage
import timber.log.Timber

class PathManagementService(
    private val firebaseService: FirebaseService,
    private val persistentStorage: PersistentStorage
) {

    // TODO: Remove sample method
    fun samplePathManagementAction() {
        persistentStorage.getExampleEntry()
        Timber.d("Sample method called")
    }
}
