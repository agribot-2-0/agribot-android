package eu.agribot.agribotapp.domain.service.cloud

import android.util.Log
import eu.agribot.agribotapp.communication.database.firestorage.DownloaderPath
import eu.agribot.agribotapp.communication.database.firestorage.FirestorageService
import eu.agribot.agribotapp.domain.error.CloudStorageErrorHandler
import eu.agribot.agribotapp.domain.service.AuthenticationService
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePath.MAPS_FILE
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePath.MAPS_PATH
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePathProvider
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.progress.ProgressMonitor
import java.io.File

class CloudStorageService(
    private val firestorageService: FirestorageService,
    private val authenticationService: AuthenticationService,
    private val filePathProvider: FilePathProvider,
    private val cloudStorageErrorHandler: CloudStorageErrorHandler
) {
    companion object {
        const val PROGRESS_COMPLETE = 100
    }

    fun downloadFilesList(
        onFileAvailable: (fileName: String) -> Unit,
        onError: (exception: String) -> Unit
    ) {
        firestorageService.listAllItem(
            authenticationService.getCurrentUser().userUID,
            onFileAvailable, onError = { onError(cloudStorageErrorHandler.getStorageError(it)) }
        )
    }

    fun onFileSelected(
        fileName: String,
        onGotFile: (file: File) -> Unit,
        onSelectedFileError: (fileError: String) -> Unit
    ) {
        val file = File(
            filePathProvider.getLayerPath(DownloaderPath.LAYER_PATH.filePath),
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
        firestorageService.downloadPathFileFromUrl(
            fileName,
            filePathProvider.getLayerPath(DownloaderPath.LAYER_PATH.filePath),
            onDownloadFileSucess,
            onDownloadError = { onDownloadFileError(cloudStorageErrorHandler.getStorageError(it)) }
        )
    }

    fun uploadPathFile(
        filePath: String,
        fileName: String,
        onSucess: () -> Unit,
        onError: (exception: String) -> Unit
    ) {
        firestorageService.uploadPathFile(
            filePath,
            fileName,
            onSucess,
            onError = { onError(cloudStorageErrorHandler.getStorageError(it)) }
        )
    }

    fun removeFileInCLoud(
        fileName: String,
        onSucess: () -> Unit,
        onError: (exception: String) -> Unit
    ) {
        firestorageService.removePathFileInCloudStorage(
            fileName,
            onSucess,
            onError = { onError(cloudStorageErrorHandler.getStorageError(it)) })
    }

    fun onMapTilesRequest(
        onGotMapTilesAlready: () -> Unit,
        downloadProgress: (downloadProgress: Long) -> Unit,
        onDownloadedMapTiles: () -> Unit,
        onDownloadedMapTilesError: (error: String) -> Unit

    ) {
        if (!isTileFileExistInStorage()) {
            Log.e("xd", "download map")
            downloadZipTiles(onDownloadedMapTiles, downloadProgress, onDownloadedMapTilesError)
        } else {
            onGotMapTilesAlready.invoke()
        }
    }

    private fun isTileFileExistInStorage(): Boolean {
        return (File(filePathProvider.getLayerPath(DownloaderPath.MAP_PATH.filePath + MAPS_PATH)).exists())
    }

    private fun downloadZipTiles(
        onDownloadFileSucess: () -> Unit,
        downloadProgress: (downloadProgress: Long) -> Unit,
        onDownloadFileError: (error: String) -> Unit
    ) {
        firestorageService.downloadZipMapTiles(
            filePathProvider.getLayerPath(DownloaderPath.MAP_PATH.filePath),
            onDownloadSucess = onDownloadFileSucess,
            onDownloadProgress = downloadProgress,
            onDownloadError = {
                if (it != null) {
                    onDownloadFileError(cloudStorageErrorHandler.getStorageError(it.connectionException))
                }
            }
        )
    }

    fun extractDownloadedFile(onDecompressProgress: (decompressProgress: Int) -> Unit) {
        val zipFile: ZipFile =
            ZipFile(filePathProvider.getLayerPath(DownloaderPath.MAP_PATH.filePath + MAPS_FILE))
        zipFile.extractAll(filePathProvider.getLayerPath(DownloaderPath.MAP_PATH.filePath))
        val progressMonitor: ProgressMonitor = zipFile.progressMonitor
        onDecompressProgress(progressMonitor.percentDone)
        if (progressMonitor.percentDone == PROGRESS_COMPLETE) {
            File(filePathProvider.getLayerPath(DownloaderPath.MAP_PATH.filePath + MAPS_FILE)).delete()
        }
    }
}
