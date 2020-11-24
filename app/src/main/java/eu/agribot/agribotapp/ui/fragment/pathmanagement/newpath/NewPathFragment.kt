package eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
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
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.DashboardNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentPathManagementNewPathBinding
import eu.agribot.agribotapp.domain.model.CoordinatesModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.MapParameters
import eu.agribot.agribotapp.ui.fragment.taskcreator.TileProvider
import eu.agribot.agribotapp.ui.fragment.taskcreator.URLPath
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.IOException

class NewPathFragment :
    BaseFragment<NewPathViewModel, FragmentPathManagementNewPathBinding>(), OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener {
    private var googleMapFragment: GoogleMap? = null
    private var realtimePolyline: Polyline? = null
    private var polylineFromList: Polyline? = null
    private var pointMarker: Marker? = null
    var markersList = ArrayList<Marker>()

    override val layoutResourceId: Int = R.layout.fragment_path_management_new_path

    override val viewModel: NewPathViewModel by viewModel { parametersOf(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.new_path_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        viewModel.getAgribotPosition()
        observeViewmodel()
    }

    private fun observeViewmodel() {
        viewModel.getPath()
        viewModel.cameraPosition.observe(this::updateCameraPosition)
        viewModel.toastMessageEvent.observe(this::showToast)
        viewModel.selectedPointsList.observe(this::drawPolyLinesFromList)
    }

    override fun bindViewVariables(viewBinding: FragmentPathManagementNewPathBinding) {
        viewBinding.viewModel = viewModel
    }

    private fun addPathPoint(coordinateList: List<CoordinatesModel>) {
        pointMarker?.remove()
        coordinateList.forEach { coordinate ->
            pointMarker = googleMapFragment!!.addMarker(
                MarkerOptions()
                    .position(LatLng(coordinate.latitude, coordinate.longtitude))
                    .anchor(MapParametr.ANCHOR.parametr, MapParametr.ANCHOR.parametr)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_white_circle))
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
    }

    private fun drawRealtimePolyline(coordinates: LatLng) {
        if (viewModel.selectedPointsList.value!!.isNotEmpty()) {
            realtimePolyline?.remove()
            realtimePolyline = googleMapFragment!!.addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(
                            viewModel.selectedPointsList.value!![0].latitude,
                            viewModel.selectedPointsList.value!![0].longtitude
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
                    .addAll(pointsList.map { LatLng(it.latitude, it.longtitude) })
                    .width(MapParametr.POLYLINE_WIDTH.parametr)
                    .color(Color.RED)
            )
        }
        addPathPoint(pointsList)
    }

    override fun onCameraMove() {
        drawRealtimePolyline(googleMapFragment!!.cameraPosition.target)
    }
}
