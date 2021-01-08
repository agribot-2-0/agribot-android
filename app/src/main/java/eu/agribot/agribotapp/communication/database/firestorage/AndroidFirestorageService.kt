package eu.agribot.agribotapp.communication.database.firestorage

import android.net.Uri
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import eu.agribot.agribotapp.communication.database.firebase.FirebaseService
import eu.agribot.agribotapp.communication.database.firestorage.DownloadObjects.MAPS_FILE
import eu.agribot.agribotapp.communication.database.firestorage.DownloadObjects.PATH_SOURCE
import java.io.File

class AndroidFirestorageService(
    private val firebaseService: FirebaseService
) : FirestorageService {
    private val storage = Firebase.storage

    override fun listAllItem(
        currentUserID: String,
        onFilesAvailable: (fileName: String) -> Unit,
        onError: (exception: java.lang.Exception) -> Unit
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
        storage.reference.child("${firebaseService.getCurrentUser()?.uid}/$PATH_SOURCE/$fileName").downloadUrl.addOnSuccessListener {
            PRDownloader.download(
                it.toString(),
                filePath,
                fileName
            )
                .build()
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
                    }
                })
        }
    }

    override fun uploadPathFile(
        filePath: String,
        fileName: String,
        onSucess: () -> Unit,
        onError: (exception: Exception) -> Unit
    ) {
        storage.reference.child("${firebaseService.getCurrentUser()?.uid}/$PATH_SOURCE/$fileName")
            .putFile(Uri.fromFile(File(filePath)))
            .addOnFailureListener { exception ->
                onError(exception)
            }.addOnSuccessListener {
                onSucess.invoke()
            }
    }

    override fun removePathFileInCloudStorage(
        fileName: String,
        onSucess: () -> Unit,
        onError: (exception: Exception) -> Unit
    ) {
        storage.reference.child("${firebaseService.getCurrentUser()?.uid}/$PATH_SOURCE/$fileName")
            .delete()
            .addOnSuccessListener {
                onSucess.invoke()
            }
            .addOnFailureListener {
                onError(it)
            }
    }

    override fun downloadZipMapTiles(
        pathFile: String,
        onDownloadStart: () -> Unit,
        onDownloadSucess: () -> Unit,
        onDownloadError: (error: Error?) -> Unit
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

                    override fun onError(error: Error?) {
                        onDownloadError(error)
                    }
                })
        }
    }
}
