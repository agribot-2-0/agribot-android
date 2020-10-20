package eu.agribot.agribotapp.communication.database.firestorage

import java.io.File

interface FirestorageService {

    fun listAllItem(
        currentUserID: String,
        onFilesAvailable: (fileName: String) -> Unit,
        onError: (exception: String) -> Unit
    )

    fun onFileToDownloadSelect(
        currentUserID: String,
        fileName: String,
        onGotFile: (file: File) -> Unit,
        onDownloadError: (error: Throwable) -> Unit
    )
}
