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
    GoogleMap.OnCameraIdleListener {
    private var googleMapFragment: GoogleMap? = null
    var tempCordinate: LatLng? = null
    private var polyline: Polyline? = null
    private var polylines: Polyline? = null

    override val layoutResourceId: Int = R.layout.fragment_new_path

    override val viewModel: NewPathViewModel by viewModel { parametersOf(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.new_path_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        viewModel.getAgribotPosition()
        viewModel.cameraPosition.observe(this::updateCameraPosition)
        viewModel.selectedPoints.observe(this::selectLatLng)
        viewModel.toastMessageEvent.observe(this::showToast)
        viewModel.postInputDialogEvent.observe(this::showCreateCategoryDialog)
        viewModel.selectedPointsList.observe(this::drawPolyLinesFromList)
    }

    override fun bindViewVariables(viewBinding: FragmentNewPathBinding) {
        viewBinding.viewModel = viewModel
    }

    private fun addPathPoint(coordinate: LatLng) {
        googleMapFragment?.addMarker(
            MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_layer_pointer))
                .position(coordinate)
        )
    }

    private fun updateCameraPosition(cameraPosition: LatLng) {
        tempCordinate = cameraPosition
        googleMapFragment?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                cameraPosition, MapParameters.MID_ZOOM.mapParametr
            )
        )
    }

    private fun addTileOverlay() {
        val tileProvider =
            TileProvider(
                MapParameters.MAP_SIZE.mapParametr.toInt(),
                MapParameters.MAP_SIZE.mapParametr.toInt(),
                viewModel.getMapTilePath(URLPath.MAP_PATH.path)
            )
        try {
            googleMapFragment!!.addTileOverlay(
                TileOverlayOptions().transparency(MapParameters.TRANSPARENCY_VALUE.mapParametr)
                    .tileProvider(tileProvider)
            )
        } catch (exception: IOException) {
            showToast(getString(R.string.map_tile_problem))
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMapFragment = googleMap
        googleMapFragment!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMapFragment!!.setOnCameraIdleListener(this)
        addTileOverlay()
    }

    private fun selectLatLng(coordinates: LatLng) {
        addPathPoint(coordinates)
        tempCordinate = coordinates
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) =
        when (navigationEvent) {
            is DashboardNavigationEvent -> navigateToDashboardFragment()
            else -> Unit
        }

    private fun navigateToDashboardFragment() {
        navigationController?.navigate(R.id.action_newPathFragment_to_dashboardFragment)
    }

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
            val b = dialogBuilder.create()
            b.show()
        }
    }

    override fun onCameraIdle() {
        viewModel.onCoordinatesChanged(googleMapFragment!!.cameraPosition.target)
        drawTempPolyline(googleMapFragment!!.cameraPosition.target)
    }

    private fun drawTempPolyline(coordinates: LatLng) {
        if (tempCordinate != null) {
            if (polyline != null) {
                polyline!!.remove()
            }
            polyline = googleMapFragment!!.addPolyline(
                PolylineOptions()
                    .add(tempCordinate, coordinates)
                    .width(7f)
                    .color(Color.BLUE)
            )
        }

    }

    private fun drawPolyLinesFromList(pointsList: List<CoordinatesModel>) {
        val list = arrayListOf<LatLng>()
        for (element in pointsList) {
            list += element.coordinates
        }
        if (list.size >= 2) {
            if (polylines != null) {
                polylines!!.remove()
            }
            polylines = googleMapFragment!!.addPolyline(
                PolylineOptions()
                    .addAll(list)
                    .width(9f)
                    .color(Color.RED)
            )
        }
    }


}





