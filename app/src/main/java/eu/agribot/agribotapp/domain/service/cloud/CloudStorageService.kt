package eu.agribot.agribotapp.domain.service.cloud

import eu.agribot.agribotapp.communication.database.firestorage.FirestorageService
import eu.agribot.agribotapp.domain.error.CloudStorageErrorHandler
import eu.agribot.agribotapp.domain.service.AuthenticationService
import java.io.File

class CloudStorageService(
    private val firestorageService: FirestorageService,
    private val authenticationService: AuthenticationService,
    private val cloudStorageErrorHandler: CloudStorageErrorHandler
) {

    fun downloadFilesList(
        onFileAvailable: (fileName: String) -> Unit,
        onError: (exception: String) -> Unit
    ) {
        firestorageService.listAllItem(
            authenticationService.getCurrentUser().userUID,
            onFileAvailable, onError
        )
    }

    fun downloadFile(
        fileName: String,
        onGotFile: (file: File) -> Unit
    ) {
        firestorageService.onFileToDownloadSelect(
            authenticationService.getCurrentUser().userUID,
            fileName,
            onGotFile,
            onDownloadError = { cloudStorageErrorHandler.getStorageError(it as Exception) }
        )
    }
}
