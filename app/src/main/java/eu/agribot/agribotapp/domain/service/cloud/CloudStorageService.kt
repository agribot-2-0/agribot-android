package eu.agribot.agribotapp.domain.service.cloud

import eu.agribot.agribotapp.communication.database.firestorage.DownloaderPath
import eu.agribot.agribotapp.communication.database.firestorage.FirestorageService
import eu.agribot.agribotapp.domain.error.CloudStorageErrorHandler
import eu.agribot.agribotapp.domain.service.AuthenticationService
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePathProvider
import java.io.File

class CloudStorageService(
    private val firestorageService: FirestorageService,
    private val authenticationService: AuthenticationService,
    private val filePathProvider: FilePathProvider,
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

    fun onFileSelected(
        fileName: String,
        onGotFile: (file: File) -> Unit,
        onSelectedFileError: (fileError: String) -> Unit
    ) {
        val file = File(
            filePathProvider.getRootDirPath(DownloaderPath.LAYER_PATH.filePath),
            fileName
        )
        if (file.exists()) {
            onGotFile(file)
        } else {
            downloadFile(
                fileName,
                onGotFile,
                onSelectedFileError
            )
        }
    }

    private fun downloadFile(
        fileName: String,
        onDownloadFileSucess: (file: File) -> Unit,
        onDownloadFileError: (error: String) -> Unit
    ) {
        firestorageService.downloadFileFromUrl(
            fileName,
            filePathProvider.getRootDirPath(DownloaderPath.LAYER_PATH.filePath),
            onDownloadFileSucess,
            onDownloadError = { onDownloadFileError(cloudStorageErrorHandler.getStorageError(it)) }
        )
    }
}
