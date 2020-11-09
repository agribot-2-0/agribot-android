package eu.agribot.agribotapp.domain.service.cloud.downloader

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import java.io.File

class FilePathProvider(private val context: Context) {

    fun getRootDirPath(pathDirection: String): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[0]
            file.absolutePath + pathDirection
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }
}
