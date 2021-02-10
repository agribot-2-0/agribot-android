package eu.agribot.agribotapp.communication.database.firestorage

import com.downloader.Error
import java.io.File

interface FirestorageService {

    fun listAllItem(
        currentUserID: String,
        onFilesAvailable: (fileName: String) -> Unit,
        onError: (exception: Exception) -> Unit
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
        onError: (exception: Exception) -> Unit
    )

    fun removePathFileInCloudStorage(
        fileName: String,
        onSucess: () -> Unit,
        onError: (exception: Exception) -> Unit
    )

    fun downloadZipMapTiles(
        pathFile: String,
        onDownloadStart: () -> Unit,
        onDownloadSucess: () -> Unit,
        onDownloadError: (error: Error?) -> Unit

    )

    fun uploadPhotoFile(
        toolPhotoPath: String,
        toolName: String,
        onSucess: () -> Unit,
        onError: (exception: Exception) -> Unit
    )

    fun downloadPhotoFile(
        toolPhotoName: String,
        filePath: String,
        onDownloadSucess: (file: File) -> Unit,
        onDownloadError: (error: Throwable) -> Unit
    )

}
