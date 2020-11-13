package eu.agribot.agribotapp.communication.database.firestorage

import android.net.Uri
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import eu.agribot.agribotapp.communication.database.firebase.FirebaseService
import java.io.File

class AndroidFirestorageService(
    private val firebaseService: FirebaseService
) : FirestorageService {
    private val storage = Firebase.storage

    override fun listAllItem(
        currentUserID: String,
        onFilesAvailable: (fileName: String) -> Unit,
        onError: (exception: String) -> Unit
    ) {
        Firebase.storage.reference.child("$currentUserID/paths/")
            .listAll()
            .addOnSuccessListener { listResult ->
                listResult.items.forEach { item ->
                    onFilesAvailable(item.name)
                }
            }
            .addOnFailureListener { exception ->
                onError(exception.toString())
            }
    }

    override fun downloadFileFromUrl(
        fileName: String,
        filePath: String,
        onDownloadSucess: (file: File) -> Unit,
        onDownloadError: (error: Throwable) -> Unit
    ) {
        storage.reference.child("${firebaseService.getCurrentUser()?.uid}/paths/$fileName").downloadUrl.addOnSuccessListener {
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

    override fun uploadFile(
        filePath: String,
        fileName: String,
        onSucess: () -> Unit,
        onError: (exception: String) -> Unit
    ) {
        storage.reference.child("${firebaseService.getCurrentUser()?.uid}/paths/$fileName")
            .putFile(Uri.fromFile(File(filePath)))
            .addOnFailureListener {
                onError("UPLOAD FILE ERROR")
            }.addOnSuccessListener {
                onSucess.invoke()
            }
    }
}
