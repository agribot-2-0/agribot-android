package eu.agribot.agribotapp.communication.database.firestorage

import android.content.Context
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import eu.agribot.agribotapp.communication.database.firestorage.downloader.DownloaderUtils
import java.io.File

class AndroidFirestorageService(
    private val context: Context,
    private val downloaderUtils: DownloaderUtils
) :
    FirestorageService {
    private val storage = Firebase.storage

    override fun listAllItem(
        currentUserID: String,
        onFilesAvailable: (fileName: String) -> Unit,
        onError: (exception: String) -> Unit
    ) {
        val storage = Firebase.storage
        val listRef = storage.reference.child("$currentUserID/paths/")
        listRef.listAll()
            .addOnSuccessListener { listResult ->
                listResult.items.forEach { item ->
                    onFilesAvailable(item.name)
                }
            }
            .addOnFailureListener { exception ->
                onError(exception.toString())
            }
    }

    override fun onFileToDownloadSelect(
        currentUserID: String,
        fileName: String,
        onGotFile: (file: File) -> Unit,
        onDownloadError: (error: Throwable) -> Unit
    ) {
        val file = File(
            downloaderUtils.getRootDirPath(context, DownloaderPath.LAYER_PATH.filePath),
            fileName
        )
        if (file.exists()) {
            onGotFile(file)
        } else {
            val storageReference: StorageReference = storage.reference
            storageReference.child("$currentUserID/paths/$fileName").downloadUrl.addOnSuccessListener { url ->
                downloadFileFromUrl(
                    context,
                    url.toString(),
                    fileName,
                    onGotFile,
                    onDownloadError
                )
            }.addOnFailureListener { exception ->
                onDownloadError(exception)
            }
        }
    }

    private fun downloadFileFromUrl(
        context: Context,
        url: String,
        fileName: String,
        onDownloadSucess: (file: File) -> Unit,
        onDownloadError: (error: Throwable) -> Unit
    ) {
        PRDownloader.download(
            url,
            downloaderUtils.getRootDirPath(context, DownloaderPath.LAYER_PATH.filePath),
            fileName
        )
            .build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    onDownloadSucess(
                        File(
                            downloaderUtils.getRootDirPath(
                                context,
                                DownloaderPath.LAYER_PATH.filePath
                            ),
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
