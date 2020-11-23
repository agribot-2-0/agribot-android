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
import eu.agribot.agribotapp.databinding.FragmentNewPathBinding
import eu.agribot.agribotapp.domain.model.CoordinatesModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.MapParameters
import eu.agribot.agribotapp.ui.fragment.taskcreator.TileProvider
import eu.agribot.agribotapp.ui.fragment.taskcreator.URLPath
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.IOException

class NewPathFragment :
    BaseFragment<NewPathViewModel, FragmentNewPathBinding>(), OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener {
    private var googleMapFragment: GoogleMap? = null
    private var realtimePolyline: Polyline? = null
    private var polylineFromList: Polyline? = null
    private var pointMarker: Marker? = null
    var markersList = ArrayList<Marker>()

    override val layoutResourceId: Int = R.layout.fragment_new_path

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
        viewModel.cameraPosition.observe(this::updateCameraPosition)
        viewModel.toastMessageEvent.observe(this::showToast)
        viewModel.postInputDialogEvent.observe(this::showCreateCategoryDialog)
        viewModel.selectedPointsList.observe(this::drawPolyLinesFromList)
    }

    override fun bindViewVariables(viewBinding: FragmentNewPathBinding) {
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
            viewModel.setCurrentCoordinate(cameraPosition.coordinates)
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

    // TODO ADD NAME FRAMGMENT INSTEAD DIALOG VIEW
    private fun showCreateCategoryDialog(show: Boolean) {
        if (show) {
            val dialogBuilder = AlertDialog.Builder(requireContext())
            val dialogView = this.layoutInflater.inflate(R.layout.dialog_path_name, null)
            dialogBuilder.setView(dialogView)
            val editText = dialogView.findViewById<View>(R.id.new_file_edit_text) as EditText
            dialogBuilder.setTitle(R.string.dialog_create_file_title)
            dialogBuilder.setPositiveButton(
                R.string.dialog_create_file_confirm
            ) { dialog, _ ->
                viewModel.createNewLayer(editText.text.toString())
                dialog.dismiss()
            }
            dialogBuilder.create().show()
        }
    }

    override fun onCameraIdle() {
        viewModel.onCoordinatesChanged(googleMapFragment!!.cameraPosition.target)
        viewModel.setCurrentCoordinate(googleMapFragment!!.cameraPosition.target)
    }

    private fun drawRealtimePolyline(coordinates: LatLng) {
        realtimePolyline?.remove()
        if (viewModel.currentCoordinate.value != null) {
            realtimePolyline = googleMapFragment!!.addPolyline(
                PolylineOptions()
                    .add(viewModel.currentCoordinate.value, coordinates)
                    .width(MapParametr.TEMP_POLYLINE_WIDTH.parametr)
                    .color(Color.BLUE)
            )
        }
    }

    private fun drawPolyLinesFromList(pointsList: List<CoordinatesModel>) {
        polylineFromList?.remove()
        if (pointsList.isNotEmpty()) {
            viewModel.setCurrentCoordinate(LatLng(pointsList[0].latitude, pointsList[0].longtitude))
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
