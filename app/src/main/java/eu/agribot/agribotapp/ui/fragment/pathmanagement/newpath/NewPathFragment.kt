package eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.maps.android.kml.KmlContainer
import com.google.maps.android.kml.KmlLayer
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.DashboardNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentPathManagementNewPathBinding
import eu.agribot.agribotapp.domain.model.CoordinatesModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.MapParameters
import eu.agribot.agribotapp.ui.fragment.taskcreator.TileProvider
import eu.agribot.agribotapp.ui.fragment.taskcreator.URLPath
import kotlinx.android.synthetic.main.fragment_path_management_new_path.drawPolylineSwitch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.IOException
import java.io.InputStream

class NewPathFragment :
    BaseFragment<NewPathViewModel, FragmentPathManagementNewPathBinding>(), OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener,
    GoogleMap.OnMarkerClickListener {
    private var googleMapFragment: GoogleMap? = null
    private var realtimePolyline: Polyline? = null
    private var polylineFromList: Polyline? = null
    private var pointMarker: Marker? = null
    var markersList = ArrayList<Marker>()
    private var mapPathLayer: KmlLayer? = null

    override val layoutResourceId: Int = R.layout.fragment_path_management_new_path

    override val viewModel: NewPathViewModel by viewModel { parametersOf(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.new_path_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        viewModel.getAgribotPosition()
        observeViewmodel()

        drawPolylineSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.setSwitchStatus(isChecked)
        }
    }

    private fun observeViewmodel() {
        viewModel.getPath()
        viewModel.selectedLayer.observe(this::onGotSelectedLayer)
        viewModel.cameraPosition.observe(this::updateCameraPosition)
        viewModel.toastMessageEvent.observe(this::showToast)
        viewModel.selectedPointsList.observe(this::drawPolyLinesFromList)
        viewModel.markerPosition.observe(this::updateEditAction)
    }

    private fun updateEditAction(coordinates: LatLng) {
        viewModel.onChangeMarkerPosition(
            CoordinatesModel(
                coordinates.latitude,
                coordinates.longitude
            )
        )
    }

    override fun bindViewVariables(viewBinding: FragmentPathManagementNewPathBinding) {
        viewBinding.viewModel = viewModel
    }

    private fun addPathPoint(coordinateList: List<CoordinatesModel>) {
        markersList.forEach {
            it.remove()
        }
        pointMarker?.remove()
        coordinateList.forEach { coordinate ->
            pointMarker = googleMapFragment!!.addMarker(
                MarkerOptions()
                    .position(LatLng(coordinate.latitude, coordinate.longitude))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_white_circle))
                    .draggable(true)
            )
            markersList.add(pointMarker!!)
        }
    }

    private fun updateCameraPosition(cameraPosition: CameraPosition) {
        if (cameraPosition.isPointDeleted) {
            drawRealtimePolyline(cameraPosition.coordinates)
            markersList.forEach { marker ->
                marker.remove()
            }
            viewModel.selectedPointsList.value?.map { selectedPoint ->
                selectedPoint
            }.let { listOfLatLng ->
                addPathPoint(listOfLatLng!!)
            }
        }
        googleMapFragment?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                cameraPosition.coordinates, MapParameters.MID_ZOOM.mapParametr
            )
        )
    }

    private fun addTileOverlay() {
        try {
            googleMapFragment!!.addTileOverlay(
                TileOverlayOptions().transparency(MapParameters.TRANSPARENCY_VALUE.mapParametr)
                    .tileProvider(
                        TileProvider(
                            MapParameters.MAP_SIZE.mapParametr.toInt(),
                            MapParameters.MAP_SIZE.mapParametr.toInt(),
                            viewModel.getMapTilePath(URLPath.MAP_PATH.path)
                        )
                    )
            )
        } catch (exception: IOException) {
            showToast(getString(R.string.map_tile_problem))
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMapFragment = googleMap
        googleMapFragment!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMapFragment!!.setOnCameraIdleListener(this)
        googleMapFragment!!.setOnCameraMoveListener(this)
        googleMapFragment!!.setOnMarkerClickListener(this)
        addTileOverlay()
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) =
        when (navigationEvent) {
            is DashboardNavigationEvent -> navigateToDashboardFragment()
            else -> Unit
        }

    private fun navigateToDashboardFragment() {
        navigationController?.navigate(R.id.action_newPathFragment_to_dashboardFragment)
    }

    override fun onCameraIdle() {
        viewModel.onCoordinatesChanged(googleMapFragment!!.cameraPosition.target)
        viewModel.onMarkerPositionChanged(googleMapFragment!!.cameraPosition.target)
    }

    private fun drawRealtimePolyline(coordinates: LatLng) {
        if (viewModel.marker.value != null) {
            realtimePolyline?.remove()
        }
        if (viewModel.selectedPointsList.value!!.isNotEmpty() && viewModel.marker.value == null) {
            realtimePolyline?.remove()
            realtimePolyline = googleMapFragment!!.addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(
                            viewModel.selectedPointsList.value!![0].latitude,
                            viewModel.selectedPointsList.value!![0].longitude
                        ), coordinates
                    )
                    .width(MapParametr.TEMP_POLYLINE_WIDTH.parametr)
                    .color(Color.BLUE)
            )
        }
    }

    private fun drawPolyLinesFromList(pointsList: List<CoordinatesModel>) {
        polylineFromList?.remove()
        if (pointsList.isNotEmpty()) {
            polylineFromList = googleMapFragment!!.addPolyline(
                PolylineOptions()
                    .addAll(pointsList.map { LatLng(it.latitude, it.longitude) })
                    .width(MapParametr.POLYLINE_WIDTH.parametr)
                    .color(Color.RED)
            )
        }
        addPathPoint(pointsList)
    }

    override fun onCameraMove() {
        drawRealtimePolyline(googleMapFragment!!.cameraPosition.target)
    }

    private fun onGotSelectedLayer(mapStream: InputStream) {
        if (mapPathLayer != null) {
            mapPathLayer!!.removeLayerFromMap()
            setSelectedLayer(mapStream)
            mapStream.close()
        } else {
            setSelectedLayer(mapStream)
            mapStream.close()
        }
    }

    private fun setSelectedLayer(mapStream: InputStream) {
        try {
            mapPathLayer = KmlLayer(googleMapFragment!!, mapStream, requireContext())
            try {
                val container: Iterable<KmlContainer> = mapPathLayer!!.containers
                viewModel.getCoordinatesFromKml(container)
            } catch (exception: IOException) {
                showToast(getString(R.string.kml_layer_problem))
            }
        } catch (exception: Exception) {
            exception.stackTrace
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        if (viewModel.marker.value == null) {
            viewModel.setMarker(marker)
        } else {
            viewModel.setMarker(null)
        }
        return true
    }
}
