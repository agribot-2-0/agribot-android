package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.database.firebase.FirebaseService
import eu.agribot.agribotapp.communication.database.firestorage.DownloaderPath
import eu.agribot.agribotapp.communication.database.firestorage.FirestorageService
import eu.agribot.agribotapp.communication.persistence.PersistentStorage
import eu.agribot.agribotapp.domain.model.CoordinatesModel
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePathProvider
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PathManagementService(
    private val firebaseService: FirebaseService,
    private val persistentStorage: PersistentStorage,
    private val filepathProvider: FilePathProvider,
    private val fileStorageService: FirestorageService
) {
    private val sourceSeparator = "/"
    private val fileFormat = ".kml"
    private var versionNumber = 1
    private val nextVersion = 1

    // TODO: Remove sample method
    fun samplePathManagementAction() {
        persistentStorage.getExampleEntry()
        Timber.d("Sample method called")
    }

    fun createPathFile(
        coordinateList: List<CoordinatesModel>,
        fileName: String,
        onSucess: () -> Unit,
        onError: (error: String) -> Unit
    ) {

        val fileVersionName =
            if (File(filepathProvider.getLayerPath(DownloaderPath.LAYER_PATH.filePath + sourceSeparator + fileName + versionNumber.toString() + fileFormat)).exists()) {
                versionNumber += nextVersion
                fileName + versionNumber.toString()
            } else {
                fileName + versionNumber.toString()
            }

        val file =
            File(filepathProvider.getLayerPath(DownloaderPath.LAYER_PATH.filePath + sourceSeparator + fileVersionName + fileFormat))

        val fileOutputStream: FileOutputStream?
        try {
            fileOutputStream = FileOutputStream(file)
            val xmlSerializer = XmlPullParserFactory.newInstance().newSerializer()
            xmlSerializer.setOutput(fileOutputStream, "UTF-8")
            xmlSerializer.startDocument("UTF-8", null)
            xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true)
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
            xmlSerializer.text(fileName)
            xmlSerializer.endTag(null, "name")
            xmlSerializer.startTag(null, "styleUrl")
            xmlSerializer.text("#style-Tracks")
            xmlSerializer.endTag(null, "styleUrl")
            xmlSerializer.startTag(null, "LineString")
            xmlSerializer.startTag(null, "altitudeMode")
            xmlSerializer.text("relativeToGround")
            xmlSerializer.endTag(null, "altitudeMode")
            xmlSerializer.startTag(null, "coordinates")
            for (i in coordinateList.indices) {
                xmlSerializer.text(
                    coordinateList[i].coordinates.longitude.toString() + "," + coordinateList[i].coordinates.latitude + ",0 "
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
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        fileStorageService.uploadFile(
            filepathProvider.getLayerPath(DownloaderPath.LAYER_PATH.filePath + sourceSeparator + fileVersionName + fileFormat),
            fileVersionName + fileFormat, onSucess, onError
        )
    }
}
