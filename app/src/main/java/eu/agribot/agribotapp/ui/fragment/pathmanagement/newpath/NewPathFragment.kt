package eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.maps.android.kml.KmlContainer
import com.google.maps.android.kml.KmlLayer
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.MainActivityNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.PathManagementNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.WelcomeActivityNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentPathManagementNewPathBinding
import eu.agribot.agribotapp.ui.activity.authentication.welcome.WelcomeActivity
import eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath.model.CameraPositionModel
import eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath.model.MapParametr
import eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath.model.PointModel
import eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath.model.TempPolylineModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.MapParameters
import eu.agribot.agribotapp.ui.fragment.taskcreator.TileProvider
import eu.agribot.agribotapp.ui.fragment.taskcreator.URLPath
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.IOException
import java.io.InputStream

class NewPathFragment :
    BaseFragment<NewPathViewModel, FragmentPathManagementNewPathBinding>(), OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener {
    private var googleMapFragment: GoogleMap? = null
    override val layoutResourceId: Int = R.layout.fragment_path_management_new_path
    override val viewModel: NewPathViewModel by viewModel { parametersOf(requireContext()) }
    var tempPolyline: Polyline? = null
    var polyline: Polyline? = null
    private var alreadyCreatedPath: KmlLayer? = null
    override fun bindViewVariables(viewBinding: FragmentPathManagementNewPathBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.new_path_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        observeViewModel()
        viewModel.getPath()
    }

    private fun observeViewModel() {
        viewModel.alreadyCreatedLayer.observe(this::onGotAlreadyCreatedPath)
        viewModel.selectedPointsCoordinates.observe(this::onSelectedPointListChanged)
        viewModel.cameraPosition.observe(this::moveCameraToSelectedPosition)
        viewModel.tempPolyline.observe(this::drawRealtimePolyline)
        viewModel.toastMessage.observe(this::showToast)
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is PathManagementNavigationEvent -> handlePathManagementNavigationEvent()
        else -> Unit
    }

    private fun handlePathManagementNavigationEvent() {
        navigationController?.navigate(R.id.action_newPathFragment_to_pathManagementFragment)
    }

    private fun onGotAlreadyCreatedPath(pathStream: InputStream) {
        if (alreadyCreatedPath != null) {
            alreadyCreatedPath!!.removeLayerFromMap()
            setSelectedLayer(pathStream)
            pathStream.close()
        } else {
            setSelectedLayer(pathStream)
            pathStream.close()
        }
    }

    private fun setSelectedLayer(mapStream: InputStream) {
        try {
            alreadyCreatedPath = KmlLayer(googleMapFragment!!, mapStream, requireContext())
            try {
                val container: Iterable<KmlContainer> = alreadyCreatedPath!!.containers
                viewModel.getCoordinatesFromAlreadyCreatedPath(container)
            } catch (exception: IOException) {
                showToast(getString(R.string.kml_layer_problem))
            }
        } catch (exception: Exception) {
            exception.stackTrace
        }
    }

    private fun drawRealtimePolyline(tempPolyline: TempPolylineModel) {
        if (this.tempPolyline != null) {
            this.tempPolyline!!.remove()
        }
        this.tempPolyline = googleMapFragment?.addPolyline(
            PolylineOptions().add(
                tempPolyline.oldPositin,
                tempPolyline.newPosition
            ).color(Color.RED).width(MapParametr.POLYLINE_WIDTH.parametr)
        )
    }

    private fun moveCameraToSelectedPosition(cameraPositionModel: CameraPositionModel) {
        googleMapFragment?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                cameraPositionModel.coordinates, MapParameters.MID_ZOOM.mapParametr
            )
        )
    }

    private fun onSelectedPointListChanged(selectedPointList: List<PointModel>) {
        if (polyline != null) {
            polyline!!.remove()
        }
        polyline = googleMapFragment?.addPolyline(
            PolylineOptions().addAll(
                selectedPointList.map {
                    LatLng(
                        it.coordinates.latitude,
                        it.coordinates.longitude
                    )
                }
            ).width(MapParametr.POLYLINE_WIDTH.parametr)
        )
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMapFragment = googleMap
        googleMapFragment!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMapFragment!!.setOnCameraIdleListener(this)
        googleMapFragment!!.setOnCameraMoveListener(this)
        addTileOverlay()
    }

    override fun onCameraIdle() {
        viewModel.setCenterMapCoordinate(googleMapFragment?.cameraPosition!!.target)
    }

    override fun onCameraMove() {
        viewModel.onChangePointPosition(googleMapFragment?.cameraPosition!!.target)
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
}
