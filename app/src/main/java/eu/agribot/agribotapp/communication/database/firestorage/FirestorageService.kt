package eu.agribot.agribotapp.communication.database.firestorage

import java.io.File

interface FirestorageService {

    fun listAllItem(
        currentUserID: String,
        onFilesAvailable: (fileName: String) -> Unit,
        onError: (exception: RuntimeException) -> Unit
    )

    fun downloadPathFileFromUrl(
        fileName: String,
        filePath: String,
        onDownloadSucess: (file: File) -> Unit,
        onDownloadError: (error: Throwable) -> Unit
    )

    fun uploadPathFile(
        filePath: String,
        fileName: String,
        onSucess: () -> Unit,
        onError: (exception: RuntimeException) -> Unit
    )

    fun removePathFileInCloudStorage(
        fileName: String,
        onSucess: () -> Unit,
        onError: (exception: Exception) -> Unit
    )

    fun downloadZipMapTiles(
        pathFile: String,
        onDownloadProgress: (currentPercent: Long) -> Unit,
        onDownloadSucess: () -> Unit
    )


}
