package eu.agribot.agribotapp.communication.database.firestorage.downloader

import android.content.Context

interface DownloaderUtils {

    fun getRootDirPath(context: Context, pathDirection: String): String

    fun getProgressDisplayLine(currentBytes: Long, totalBytes: Long): String
}
