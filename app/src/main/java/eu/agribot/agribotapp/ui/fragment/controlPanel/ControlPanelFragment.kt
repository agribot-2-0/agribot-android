package eu.agribot.agribotapp.ui.fragment.controlPanel

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.maps.android.kml.KmlContainer
import com.google.maps.android.kml.KmlLayer
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.TaskSelectNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentControlPanelBinding
import eu.agribot.agribotapp.ui.fragment.taskcreator.MapParameters
import eu.agribot.agribotapp.ui.fragment.taskcreator.TileProvider
import eu.agribot.agribotapp.ui.fragment.taskcreator.URLPath
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.IOException
import java.io.InputStream

class ControlPanelFragment :
    BaseFragment<ControlPanelViewModel, FragmentControlPanelBinding>(), OnMapReadyCallback {

    override val layoutResourceId: Int = R.layout.fragment_control_panel
    override val viewModel: ControlPanelViewModel by viewModel { parametersOf(requireContext()) }
    private var googleMapFragment: GoogleMap? = null
    var mapPathLayer: KmlLayer? = null

    override fun bindViewVariables(viewBinding: FragmentControlPanelBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is TaskSelectNavigationEvent -> handleTaskSelectNavigationEvent()
        else -> Unit
    }

    private fun handleTaskSelectNavigationEvent() {
        navigationController?.navigate(R.id.action_dashboard_to_taskSelectFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.dashboard_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        viewModel.getAgribotPosition()
        viewModel.getActualTask()
        viewModel.pathStream.observe(this::onGotSelectedLayer)
        viewModel.markers.observe(this::updateMapMarker)
        viewModel.toastMessageEvent.observe(this::showToast)
        viewModel.cameraPosition.observe((this::updateMapCameraPosition))
    }

    private fun updateMapCameraPosition(latLng: LatLng) {
        googleMapFragment?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng, MapParameters.MID_ZOOM.mapParametr
            )
        )
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

    private fun addTileOverlay() {
        val tileProvider =
            TileProvider(
                MapParameters.MAP_SIZE.mapParametr.toInt(),
                MapParameters.MAP_SIZE.mapParametr.toInt(),
                getRootDirPath(requireContext(), URLPath.MAP_PATH.path)
            )
        try {
            googleMapFragment!!.addTileOverlay(
                TileOverlayOptions().transparency(MapParameters.TRANSPARENCY_VALUE.mapParametr)
                    .tileProvider(tileProvider)
            )
        } catch (exception: IOException) {
            showToast("Map Tile Problem")
        }
    }

    private fun updateMapMarker(agribotMarker: MarkerOptions) {
        if (agribotMarker.position != null) {
            try {
                googleMapFragment?.addMarker(
                    MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_agibot_marker))
                        .position(agribotMarker.position)
                        .anchor(MapParameters.ANCHOR.mapParametr, MapParameters.ANCHOR.mapParametr)
                        .rotation(agribotMarker.rotation)
                        .flat(true)
                )
                googleMapFragment?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        agribotMarker.position, MapParameters.MID_ZOOM.mapParametr
                    )
                )
            } catch (exception: IOException) {
                showToast("marker exception$exception")
            }
        }
    }

    private fun onGotSelectedLayer(mapStream: InputStream) {
        if (mapPathLayer != null) {
            mapPathLayer!!.removeLayerFromMap()
            setSelectedLayer(mapStream)
        } else {
            setSelectedLayer(mapStream)
        }
    }

    private fun setSelectedLayer(mapStream: InputStream) {
        try {
            mapPathLayer = KmlLayer(googleMapFragment, mapStream, requireContext())
            mapPathLayer!!.containers
            mapPathLayer!!.addLayerToMap()
            try {
                val container: Iterable<KmlContainer> = mapPathLayer!!.containers
                viewModel.accessContainers(container)
            } catch (exception: IOException) {
                showToast("Map Layer Problem")
            }
        }catch (exception : Exception){
            exception.stackTrace
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMapFragment = googleMap
        googleMapFragment!!.clear()
        googleMapFragment!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMapFragment!!.setMinZoomPreference(MapParameters.MIN_ZOOM.mapParametr)
        googleMapFragment!!.setMaxZoomPreference(MapParameters.MAX_ZOOM.mapParametr)
        addTileOverlay()
    }
}
