package eu.agribot.agribotapp.communication.firestorage

import java.io.File

interface FirestorageService {

    fun listAllItem(
        currentUserID: String,
        onFilesAvailable: (fileName: String) -> Unit,
        onError: (exception: String) -> Unit
    )

    fun downloadFile(
        currentUserID: String,
        fileName: String,
        onFileAlreadyDownloaded: (file: File) -> Unit,
        onDownloadSucess: (file: File) -> Unit
    )
}
