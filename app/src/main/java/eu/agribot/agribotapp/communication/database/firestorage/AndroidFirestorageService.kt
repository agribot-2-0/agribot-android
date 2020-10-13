package eu.agribot.agribotapp.communication.firestorage

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File

class AndroidFirestorageService(private val context: Context) :
    FirestorageService {
    val storage = Firebase.storage

    override fun listAllItem(
        currentUserID: String,
        onFilesAvailable: (fileName: String) -> Unit,
        onError: (exception: String) -> Unit
    ) {
        val storage = Firebase.storage
        val listRef = storage.reference.child("$currentUserID/paths")

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

    override fun downloadFile(
        currentUserID: String,
        fileName: String,
        onFileAlreadyDownloaded: (file: File) -> Unit,
        onDownloadSucess: (file: File) -> Unit

    ) {

        val newFile = File(
            Environment.getExternalStorageDirectory().absolutePath + File.separator +
                    "Android" + File.separator + "data" + File.separator + "eu.agribot.agribotapp" +
                    File.separator + "user_data" + File.separator + "user_paths" + File.separator + fileName
        )
        if (newFile.exists()) {
            onFileAlreadyDownloaded.invoke(newFile)
        } else {
            val storageReference: StorageReference = storage.reference
            storageReference.child("$currentUserID/paths/$fileName").downloadUrl.addOnSuccessListener { uri ->
                isFileDownloaded(
                    context, uri, fileName, onDownloadSucess
                )
            }.addOnFailureListener { exception ->
                Log.e("xd", "exception " + exception)
            }
        }
    }

    private fun isFileDownloaded(
        context: Context,
        uri: Uri,
        fileName: String,
        onDownloadSucess: (file: File) -> Unit
    ) {
        val file: File = File(
            Environment.getExternalStorageDirectory().absolutePath + File.separator +
                    "Android" + File.separator + "data" + File.separator + "eu.agribot.agribotapp" +
                    File.separator + "user_data" + File.separator + "user_paths" + File.separator + fileName
        )
        val downloadMeanger: DownloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request: DownloadManager.Request = DownloadManager.Request(uri)
            .setTitle("File Download")
            .setDescription("simle")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationUri(Uri.fromFile(file))
        try {
            downloadMeanger.enqueue(request)
            onDownloadSucess(file)
        } catch (exception: Exception) {
            Log.e("xd", "exc" + exception)
        }
    }
}
