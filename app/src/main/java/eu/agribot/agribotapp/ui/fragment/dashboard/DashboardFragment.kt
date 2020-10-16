package eu.agribot.agribotapp.ui.fragment.dashboard

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.maps.android.kml.KmlLayer
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentDashboardBinding
import eu.agribot.agribotapp.ui.fragment.taskcreator.MapParameters
import eu.agribot.agribotapp.ui.fragment.taskcreator.TileProvider
import eu.agribot.agribotapp.ui.fragment.taskcreator.URLPath
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.IOException
import java.io.InputStream

class DashboardFragment :
    BaseFragment<DashboardViewModel, FragmentDashboardBinding>(), OnMapReadyCallback {

    override val layoutResourceId: Int = R.layout.fragment_dashboard
    override val viewModel: DashboardViewModel by viewModel { parametersOf(requireContext()) }
    private var googleMapFragment: GoogleMap? = null
    var mapPathLayer: KmlLayer? = null

    override fun bindViewVariables(viewBinding: FragmentDashboardBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.dashboard_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        viewModel.getAgribotPosition()
        viewModel.getCurrentTask()
        viewModel.pathStream.observe(this::addSelectedLayer)
        viewModel.markers.observe(this::updateMapMarker)
        viewModel.toastMessageEvent.observe(this::showToast)
    }

    private fun addTileOverlay() {
        val tileProvider =
            TileProvider(
                MapParameters.MAP_SIZE.mapParametr.toInt(),
                MapParameters.MAP_SIZE.mapParametr.toInt(),
                URLPath.FILE.path + Environment.getExternalStorageDirectory().absolutePath +
                        URLPath.PATH.path
            )
        try {
            googleMapFragment!!.addTileOverlay(
                TileOverlayOptions().transparency(MapParameters.TRANSPARENCY_VALUE.mapParametr)
                    .tileProvider(tileProvider)
            )
        } catch (exception: IOException) {
            Log.e("xd", "Problem z dodaniem nakładki na mape" + exception)
        }
    }

    private fun updateMapMarker(agribotMarker: MarkerOptions) {
        if (agribotMarker.position != null) {
            try {
                googleMapFragment?.addMarker(
                    MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.tank))
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

    private fun addSelectedLayer(mapStream: InputStream) {
        if (mapPathLayer != null) {
            mapPathLayer!!.removeLayerFromMap()
        } else {
            mapPathLayer = KmlLayer(googleMapFragment, mapStream, requireContext())
            mapPathLayer!!.containers
            mapPathLayer!!.addLayerToMap()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMapFragment = googleMap
        googleMapFragment!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMapFragment!!.setMinZoomPreference(MapParameters.MIN_ZOOM.mapParametr)
        googleMapFragment!!.setMaxZoomPreference(MapParameters.MAX_ZOOM.mapParametr)
        addTileOverlay()
    }
}
