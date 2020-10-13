package eu.agribot.agribotapp.domain.service.cloud

import eu.agribot.agribotapp.communication.firestorage.FirestorageService
import eu.agribot.agribotapp.domain.service.AuthenticationService
import java.io.File

class CloudStorageService(
    private val firestorageService: FirestorageService,
    private val authenticationService: AuthenticationService
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
        onFileAlreadyDownloaded: (file: File) -> Unit,
        onDownloadSucess: (file: File) -> Unit
    ) {
        firestorageService.downloadFile(
            authenticationService.getCurrentUser().userUID,
            fileName, onFileAlreadyDownloaded, onDownloadSucess
        )
    }
}
