package eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentNewPathBinding
import eu.agribot.agribotapp.ui.fragment.taskcreator.URLPath
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class NewPathFragment :
    BaseFragment<NewPathViewModel, FragmentNewPathBinding>(), OnMapReadyCallback,
    GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
    private var googleMapFragment: GoogleMap? = null
    var tempCordinate: LatLng? = null

    override val layoutResourceId: Int = R.layout.fragment_new_path

    override val viewModel: NewPathViewModel by viewModel { parametersOf(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.new_path_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        viewModel.selectedPoints.observe(this::selectLatLng)
        viewModel.selectedPointsList.observe(this::showList)

    }

    private fun getRootDirPath(context: Context, pathDirection: String): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[0]
            URLPath.FILE.path + file.absolutePath + pathDirection
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }

    private fun showList(coordinateList: List<CoordinatesModel>) {
        val file = File(viewModel.getPath())

        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            Log.e("xd", "jest: ")
            val xmlSerializer = XmlPullParserFactory.newInstance().newSerializer()
            xmlSerializer.setOutput(fileOutputStream, "UTF-8")
            xmlSerializer.startDocument(null, null)
            xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true)
            xmlSerializer.startTag(null, "kml")
            xmlSerializer.startTag(null, "Document")
            xmlSerializer.startTag(null, "name")
            xmlSerializer.text("kmlFile")
            xmlSerializer.endTag(null, "name")
            xmlSerializer.startTag(null, "Style")
            xmlSerializer.attribute(null, "id", "transGreenPoly")
            xmlSerializer.startTag(null, "LineStyle")
            xmlSerializer.startTag(null, "width")
            xmlSerializer.text("1")
            xmlSerializer.endTag(null, "width")
            xmlSerializer.startTag(null, "color")
            xmlSerializer.text("7dff0000")
            xmlSerializer.endTag(null, "color")
            xmlSerializer.startTag(null, "colorMode")
            xmlSerializer.text("random")
            xmlSerializer.endTag(null, "colorMode")
            xmlSerializer.endTag(null, "LineStyle")
            xmlSerializer.endTag(null, "Style")
            xmlSerializer.startTag(null, "Folder")
            xmlSerializer.startTag(null, "name")
            xmlSerializer.text("Google Campus")
            xmlSerializer.endTag(null, "name")
            xmlSerializer.startTag(null, "visibility")
            xmlSerializer.text("1")
            xmlSerializer.endTag(null, "visibility")
            xmlSerializer.startTag(null, "description")
            xmlSerializer.text("Your Data")
            xmlSerializer.endTag(null, "description")
            xmlSerializer.startTag(null, "Placemark")
            xmlSerializer.startTag(null, "name")
            xmlSerializer.text("Data")
            xmlSerializer.endTag(null, "name")
            xmlSerializer.startTag(null, "visibility")
            xmlSerializer.text("1")
            xmlSerializer.endTag(null, "visibility")
            xmlSerializer.startTag(null, "styleUrl")
            xmlSerializer.text("#transRedPoly")
            xmlSerializer.endTag(null, "styleUrl")
            xmlSerializer.startTag(null, "LineString")
            xmlSerializer.startTag(null, "extrude")
            xmlSerializer.text("1")
            xmlSerializer.endTag(null, "extrude")
            xmlSerializer.startTag(null, "altitudeMode")
            xmlSerializer.text("relativeToGround")
            xmlSerializer.endTag(null, "altitudeMode")
            //xmlSerializer.startTag(null, "outerBoundaryIs");
            // xmlSerializer.startTag(null, "LinearRing");
            xmlSerializer.startTag(null, "coordinates")
            for (i in coordinateList.indices) {
                xmlSerializer.text(
                    coordinateList[i].coordinates.longitude.toString() + "," + coordinateList[i].coordinates.latitude + ",17 \n"
                )
            }
            xmlSerializer.endTag(null, "coordinates")
            // xmlSerializer.endTag(null, "LinearRing");
            //xmlSerializer.endTag(null, "outerBoundaryIs");
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
            Log.e("xd", "lipa: " + e)
            e.printStackTrace()

        }
    }

    override fun bindViewVariables(viewBinding: FragmentNewPathBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMapFragment = googleMap
        googleMapFragment!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMapFragment!!.setOnMapClickListener(this)
        googleMapFragment!!.setOnMapLongClickListener(this)
    }

    private fun selectLatLng(coordinate: LatLng) {
        if (tempCordinate != null) {
            googleMapFragment?.addPolyline(
                PolylineOptions()
                    .add(tempCordinate, coordinate)
                    .width(5f)
                    .color(Color.RED)
            )
        }
        tempCordinate = coordinate
    }


    override fun onMapClick(coordinate: LatLng?) {
        showToast("Click! : " + coordinate)
    }

    override fun onMapLongClick(coordinate: LatLng?) {
        viewModel.onPointSelected(coordinate!!)
    }
}
