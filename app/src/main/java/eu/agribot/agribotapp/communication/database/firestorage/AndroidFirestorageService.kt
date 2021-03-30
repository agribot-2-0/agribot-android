package eu.agribot.agribotapp.communication.database.firestorage

import android.net.Uri
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import eu.agribot.agribotapp.communication.crashlystic.CrashlysticService
import eu.agribot.agribotapp.communication.database.clouddatabase.model.UploadFileModelCDB
import eu.agribot.agribotapp.communication.database.firebase.FirebaseService
import eu.agribot.agribotapp.communication.database.firestorage.DownloadObjects.MAPS_FILE
import eu.agribot.agribotapp.communication.database.firestorage.DownloadObjects.PATH_SOURCE
import eu.agribot.agribotapp.communication.database.firestorage.DownloadObjects.TOOL_PHOTO_SOURCE
import java.io.File

class AndroidFirestorageService(
    private val firebaseService: FirebaseService,
    private val crashlysticService: CrashlysticService
) : FirestorageService {
    private val storage = Firebase.storage

    override fun listAllItem(
        currentUserID: String,
        onFilesAvailable: (fileName: String) -> Unit,
        onError: (exception: Throwable) -> Unit
    ) {
        Firebase.storage.reference.child("$currentUserID/$PATH_SOURCE/")
            .listAll()
            .addOnSuccessListener { listResult ->
                listResult.items.forEach { item ->
                    onFilesAvailable(item.name)
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    override fun downloadPathFileFromUrl(
        fileName: String,
        filePath: String,
        onDownloadSucess: (file: File) -> Unit,
        onDownloadError: (error: Throwable) -> Unit
    ) {
        storage.reference.child("${firebaseService.getCurrentUser()?.uid}/$PATH_SOURCE/$fileName")
            .downloadUrl.addOnSuccessListener {
                PRDownloader.download(
                    it.toString(),
                    filePath,
                    fileName
                ).build()
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            onDownloadSucess(
                                File(
                                    filePath,
                                    fileName
                                )
                            )
                        }

                        override fun onError(error: Error) {
                            onDownloadError(error.connectionException)
                            crashlysticService.recordException(
                                CrashlysticService.RegisterExceptionModel(
                                "Download path exception",
                                error.connectionException
                            ))
                        }
                    })
            }
    }

    override fun removePathFileInCloudStorage(
        fileName: String,
        onSucess: () -> Unit,
        onError: (exception: Throwable) -> Unit
    ) {
        storage.reference.child("${firebaseService.getCurrentUser()?.uid}/$PATH_SOURCE/$fileName")
            .delete()
            .addOnSuccessListener {
                onSucess.invoke()
            }
            .addOnFailureListener {
                onError(it)
                crashlysticService.recordException(CrashlysticService.RegisterExceptionModel("Remove path on firestorage exception", it))
            }
    }

    override fun downloadZipMapTiles(
        pathFile: String,
        onDownloadStart: () -> Unit,
        onDownloadSucess: () -> Unit,
        onDownloadError: (error: Throwable) -> Unit
    ) {
        storage.reference.child("${firebaseService.getCurrentUser()?.uid}/$MAPS_FILE")
            .downloadUrl
            .addOnSuccessListener {
                PRDownloader.download(
                    it.toString(),
                    pathFile,
                    MAPS_FILE
                ).build()
                    .setOnStartOrResumeListener(onDownloadStart)
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            onDownloadSucess()
                        }

                        override fun onError(error: Error) {
                            onDownloadError(error.connectionException)
                            crashlysticService.recordException(
                                CrashlysticService.RegisterExceptionModel(
                                "Download mapTiles from firestorage exception",
                                error.connectionException
                            ))
                        }
                    })
            }
    }

    override fun uploadFileToCloud(
        uploadFileModelCDB: UploadFileModelCDB,
        onSucess: () -> Unit,
        onError: (uploadFileError: Throwable) -> Unit
    ) {
        storage.reference.child("${firebaseService.getCurrentUser()?.uid}/${uploadFileModelCDB.fileSource}/${uploadFileModelCDB.fileNAme}")
            .putFile(Uri.fromFile(File(uploadFileModelCDB.filePath)))
            .addOnFailureListener { exception ->
                onError(exception)
                crashlysticService.recordException(CrashlysticService.RegisterExceptionModel("Upload file to firestorage exception", exception))
            }.addOnSuccessListener {
                onSucess.invoke()
            }
    }

    override fun downloadPhotoFile(
        toolPhotoName: String,
        filePath: String,
        onDownloadSucess: () -> Unit,
        onDownloadError: (downloadFileError: Throwable) -> Unit
    ) {
        storage.reference.child("${firebaseService.getCurrentUser()?.uid}/$TOOL_PHOTO_SOURCE/$toolPhotoName")
            .downloadUrl.addOnSuccessListener {
                PRDownloader.download(
                    it.toString(),
                    filePath,
                    toolPhotoName
                ).build()
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            onDownloadSucess()
                        }

                        override fun onError(error: Error) {
                            onDownloadError(error.connectionException)
                            crashlysticService.recordException(
                                CrashlysticService.RegisterExceptionModel(
                                "Download photo from firestorage exception",
                                error.connectionException
                            ))
                        }
                    })
            }
    }

    override fun removeUserFolder() {
        storage.reference.child("${firebaseService.getCurrentUser()?.uid}").listAll()
            .addOnSuccessListener { item ->
                item.items.forEach { file ->
                    file.delete()
                }
                item.prefixes.forEach { folder ->
                    folder.listAll()
                        .addOnSuccessListener { item ->
                            item.items.forEach { file ->
                                file.delete()
                            }
                            item.prefixes.forEach { folder -> folder.delete() }
                        }
                    folder.delete()
                }
            }
            .addOnFailureListener {
                crashlysticService.recordException(CrashlysticService.RegisterExceptionModel("Remove user folder on firestorage exception", it))
            }
    }
}
