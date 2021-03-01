package eu.agribot.agribotapp.domain.service.cloud.downloader

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import eu.agribot.agribotapp.communication.database.firestorage.DownloaderPath
import eu.agribot.agribotapp.ui.fragment.taskcreator.URLPath
import java.io.File

class FilePathProvider(private val context: Context) {

    fun getLayerPath(pathDirection: String): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[FILE_PATH_ARRAY_POSITION]
            file.absolutePath + pathDirection
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }

    fun getMapTilePath(mapPath: String): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[FILE_PATH_ARRAY_POSITION]
            URLPath.FILE.path + file.absolutePath + mapPath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }

    fun getToolPhotoDirectory(): String {
        val folder = File(getToolPhotoPath())
        if (!folder.exists()) {
            folder.mkdir()
        }
        return getToolPhotoPath()
    }

    private fun getToolPhotoPath(): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[FILE_PATH_ARRAY_POSITION]
            file.absolutePath + DownloaderPath.TOOL_PATH.filePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }

    companion object {
        const val FILE_PATH_ARRAY_POSITION = 0
    }
}
