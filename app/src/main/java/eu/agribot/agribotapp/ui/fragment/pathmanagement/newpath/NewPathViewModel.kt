package eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath

import android.util.Log
import android.util.Xml
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePathProvider
import org.xmlpull.v1.XmlSerializer
import java.io.StringWriter

class NewPathViewModel(private val resourcesProvider: ResourcesProvider, private val filePathProvider: FilePathProvider) : BaseViewModel() {

    val selectedPoints: MutableLiveData<LatLng> = MutableLiveData()
    val selectedPointsList: MutableLiveData<List<CoordinatesModel>> = MutableLiveData(listOf())

    private fun onGetCoordinate(coordinates: CoordinatesModel) {
        val isCoordinatesAlreadyAdded = selectedPointsList.value!!.any { foundCoordinates ->
            foundCoordinates.coordinates == coordinates.coordinates
        }
        if (!isCoordinatesAlreadyAdded) {
            selectedPointsList.value = listOf(
                CoordinatesModel(
                    coordinates.coordinates
                ), *selectedPointsList.value!!.toTypedArray()
            )
        }
    }

    fun getPath() : String{
        return filePathProvider.getLayerPath("/scieszgaXD.kml")
    }

    fun onPointSelected(coordinates: LatLng) {
        selectedPoints.value = coordinates
        onGetCoordinate(coordinates = CoordinatesModel(coordinates))
    }


    /*   fun createXML(list: List<CoordinatesModel>): String? {


           val serializer: XmlSerializer = Xml.newSerializer()
           val writer = StringWriter()
           return try {
               serializer.setOutput(writer)
               serializer.startDocument("UTF-8?", true)
               serializer.startTag("", "kml")
               serializer.attribute("", "xmlns", "http://www.opengis.net/kml/2.2")
               serializer.attribute("", "xmlns:gx", "http://www.google.com/kml/ext/2.2")
               serializer.attribute("", "xmlns:kml", "http://www.opengis.net/kml/2.2")
               serializer.attribute("", "xmlns:atom", "http://www.w3.org/2005/Atom")


               serializer.startTag("", "Document")

               serializer.startTag("", "Style")
               serializer.attribute("", "id", "style-Tracks")
               serializer.startTag("", "LineStyle")
               serializer.startTag("", "color")
               serializer.attribute("", "", "fe72cd00")

               serializer.endTag("", "color")

               serializer.startTag("", "width")
               serializer.attribute("", "", 3.toString())

               serializer.endTag("", "width")

               serializer.endTag("", "LineStyle")

               serializer.endTag("", "Style")


               serializer.startTag("", "Folder")

               serializer.endTag("", "Folder")

               serializer.endTag("", "Document")


               serializer.endTag("", "kml")
               serializer.endDocument()
               writer.toString()
           } catch (e: Exception) {
               throw RuntimeException(e)
           }

       }


     */


}
