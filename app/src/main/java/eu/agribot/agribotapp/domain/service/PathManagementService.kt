package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.database.firestorage.DownloaderPath
import eu.agribot.agribotapp.domain.error.CloudStorageErrorHandler
import eu.agribot.agribotapp.domain.error.PathManagementErrorHandler
import eu.agribot.agribotapp.domain.model.CoordinatesModel
import eu.agribot.agribotapp.domain.service.cloud.CloudStorageService
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePathProvider
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class PathManagementService(
    private val filepathProvider: FilePathProvider,
    private val cloudStorageService: CloudStorageService,
    private val cloudStorageErrorHandler: CloudStorageErrorHandler,
    private val pathManagementErrorHandler: PathManagementErrorHandler
) {
    companion object {
        const val fileNameSuffix = "_v"
        const val sourceSeparator = "/"
        const val fileFormat = ".kml"
        const val nextVersion = 1
        var versionNumber = 1
        var robotPathName: String = ""
        var layerStream: InputStream? = null
    }

    fun updatePathName(pathName: String) {
        robotPathName = pathName
    }

    fun getPathName(): String {
        return robotPathName
    }

    private fun onError(error: String) {
    }

    fun findLayerFile() {
        cloudStorageService.downloadFilesList(
            onFileAvailable = this::checkIsFileExist,
            onError = this::onError
        )
    }

    fun getSelectedPathLayer(): InputStream? {
        return layerStream
    }

    private fun checkIsFileExist(fileName: String) {
        if (fileName.contains(robotPathName, ignoreCase = true)) {
            try {
                cloudStorageService.onFileSelected(
                    fileName,
                    onGotFile = this::onGotLayerFile,
                    onSelectedFileError = this::onError
                )
            } catch (exception: IOException) {
                // DO NOTHING
            }
        }
    }

    private fun onGotLayerFile(file: File) {
        val fileInputStream: InputStream = FileInputStream(file)
        layerStream = fileInputStream
    }

    fun handlePathFile(
        coordinateList: List<CoordinatesModel>,
        onSucess: () -> Unit,
        onError: (error: String) -> Unit
    ) {
        val fileVersionName = getFileVersion(getPathName())
        createKmlFile(getFileOutputStream(fileVersionName), coordinateList, onError)
        uploadPathToCloud(fileVersionName, onSucess, onError)
    }

    private fun createKmlFile(
        fileOutputStream: FileOutputStream,
        coordinateList: List<CoordinatesModel>,
        onError: (error: String) -> Unit
    ) {
        try {
            val xmlSerializer = XmlPullParserFactory.newInstance().newSerializer()
            xmlSerializer.setOutput(fileOutputStream, "UTF-8")
            xmlSerializer.startDocument("UTF-8", null)
            xmlSerializer.setFeature(
                "http://xmlpull.org/v1/doc/features.html#indent-output",
                true
            )
            xmlSerializer.startTag(null, "kml")
            xmlSerializer.attribute(null, "xmlns", "http://www.opengis.net/kml/2.2")
            xmlSerializer.attribute(null, "xmlns:gx", "http://www.google.com/kml/ext/2.2")
            xmlSerializer.attribute(null, "xmlns:kml", "http://www.opengis.net/kml/2.2")
            xmlSerializer.attribute(null, "xmlns:atom", "http://www.w3.org/2005/Atom")
            xmlSerializer.startTag(null, "Document")

            xmlSerializer.startTag(null, "Style")
            xmlSerializer.attribute(null, "id", "style-Tracks")
            xmlSerializer.startTag(null, "LineStyle")
            xmlSerializer.startTag(null, "color")
            xmlSerializer.text("fe72cd00")
            xmlSerializer.endTag(null, "color")
            xmlSerializer.startTag(null, "width")
            xmlSerializer.text("3")
            xmlSerializer.endTag(null, "width")
            xmlSerializer.endTag(null, "LineStyle")
            xmlSerializer.endTag(null, "Style")

            xmlSerializer.startTag(null, "Folder")
            xmlSerializer.startTag(null, "name")
            xmlSerializer.text("Tracks")
            xmlSerializer.endTag(null, "name")

            xmlSerializer.startTag(null, "Placemark")
            xmlSerializer.startTag(null, "name")
            xmlSerializer.text(getPathName())
            xmlSerializer.endTag(null, "name")
            xmlSerializer.startTag(null, "styleUrl")
            xmlSerializer.text("#style-Tracks")
            xmlSerializer.endTag(null, "styleUrl")
            xmlSerializer.startTag(null, "LineString")
            xmlSerializer.startTag(null, "altitudeMode")
            xmlSerializer.text("relativeToGround")
            xmlSerializer.endTag(null, "altitudeMode")
            xmlSerializer.startTag(null, "coordinates")
            xmlSerializer.text(" ")
            for (i in coordinateList.indices) {
                xmlSerializer.text(
                    " " + coordinateList[i].longitude + "," + coordinateList[i].latitude + ",0"
                )
            }
            xmlSerializer.endTag(null, "coordinates")
            xmlSerializer.endTag(null, "LineString")
            xmlSerializer.endTag(null, "Placemark")
            xmlSerializer.endTag(null, "Folder")

            xmlSerializer.endTag(null, "Document")
            xmlSerializer.endTag(null, "kml")
            xmlSerializer.endDocument()
            xmlSerializer.flush()
            fileOutputStream.close()
        } catch (e: RuntimeException) {
            onError(pathManagementErrorHandler.getCreateKmlError(e))
        } catch (e: XmlPullParserException) {
            onError(pathManagementErrorHandler.getCreateKmlError(e))
        } catch (e: IOException) {
            onError(pathManagementErrorHandler.getCreateKmlError(e))
        }
    }

    private fun getFileOutputStream(fileVersionName: String): FileOutputStream {
        return FileOutputStream(getFile(fileVersionName))
    }

    private fun getFile(filename: String): File {
        return File(filepathProvider.getLayerPath(DownloaderPath.LAYER_PATH.filePath + sourceSeparator + filename + fileFormat))
    }

    private fun getPathFilename(fileName: String): String {
        return fileName + fileNameSuffix
    }

    private fun checkIsPathExist(pathName: String): Boolean {
        return (File(filepathProvider.getLayerPath(DownloaderPath.LAYER_PATH.filePath + sourceSeparator + pathName + versionNumber.toString() + fileFormat)).exists())
    }

    private fun getFileVersion(pathName: String): String {
        return if (checkIsPathExist(pathName)) {
            versionNumber += nextVersion
            getPathFilename(pathName) + versionNumber.toString()
        } else {
            getPathFilename(pathName) + versionNumber.toString()
        }
    }

    private fun uploadPathToCloud(
        fileVersionName: String,
        onSucess: () -> Unit,
        onError: (error: String) -> Unit
    ) {
        cloudStorageService.uploadPathFile(
            filepathProvider.getLayerPath(DownloaderPath.LAYER_PATH.filePath + sourceSeparator + fileVersionName + fileFormat),
            fileVersionName + fileFormat,
            onSucess,
            onError = { onError(cloudStorageErrorHandler.getStorageError(it)) }

        )
    }
}
