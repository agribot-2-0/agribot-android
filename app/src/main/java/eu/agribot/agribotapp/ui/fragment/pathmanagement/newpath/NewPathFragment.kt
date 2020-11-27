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
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.maps.android.kml.KmlContainer
import com.google.maps.android.kml.KmlLayer
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentPathManagementNewPathBinding
import eu.agribot.agribotapp.domain.model.CoordinatesModel
import eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath.model.CameraPosition
import eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath.model.MapParametr
import eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath.model.PointModel
import eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath.model.PointerModel
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
    private var pointer: PointerModel? = null
    var markersList = ArrayList<PointerModel>()
    private var polylineFromList: Polyline? = null
    private var alreadyCreatedPath: KmlLayer? = null


    override val viewModel: NewPathViewModel by viewModel { parametersOf(requireContext()) }
    override fun bindViewVariables(viewBinding: FragmentPathManagementNewPathBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.new_path_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        observeViewmodel()
        viewModel.setCameraPositionToAgribot()
        viewModel.getPath()
    }

    private fun observeViewmodel() {
        viewModel.cameraPosition.observe(this::moveCameraToNewPosition)
        viewModel.toastMessageEvent.observe(this::showToast)
        viewModel.addedPointList.observe(this::onSelectedPointListChanged)
        viewModel.selectedLayer.observe(this::onGotAlreadyCreatedPath)
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        googleMapFragment = googleMap
        googleMapFragment!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMapFragment!!.setOnCameraIdleListener(this)
        googleMapFragment!!.setOnCameraMoveListener(this)
        addTileOverlay()
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

    override fun onCameraIdle() {
        viewModel.setCenterCameraPosition(googleMapFragment?.cameraPosition!!.target)
    }

    override fun onCameraMove() {
        viewModel.onChangePointerPosition(
            CoordinatesModel(
                googleMapFragment?.cameraPosition!!.target.latitude,
                googleMapFragment?.cameraPosition!!.target.longitude
            )
        )
    }

    private fun moveCameraToNewPosition(cameraPosition: CameraPosition) {
        googleMapFragment?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                cameraPosition.coordinates, MapParameters.MID_ZOOM.mapParametr
            )
        )
    }

    private fun onSelectedPointListChanged(pointList: List<PointModel>) {
        addMapPointer(pointList)
        addPolylines(pointList)
    }

    private fun addMapPointer(pointList: List<PointModel>) {
        pointList.forEach { point ->
            markersList.forEach { marker ->
                if (point.pointId == marker.pointerID) {
                    marker.pointer.remove()
                }
            }
            val marker = googleMapFragment?.addMarker(
                MarkerOptions()
                    .position(LatLng(point.coordinates.latitude, point.coordinates.longitude))
                    .draggable(true)
                    .anchor(MapParameters.ANCHOR.mapParametr, MapParameters.ANCHOR.mapParametr)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_white_circle))

            )
            pointer = PointerModel(point.pointId, marker!!)
            markersList.add(PointerModel(point.pointId, marker))
        }
    }

    private fun addPolylines(pointList: List<PointModel>) {
        polylineFromList?.remove()
        if (pointList.isNotEmpty()) {
            polylineFromList = googleMapFragment!!.addPolyline(
                PolylineOptions()
                    .addAll(pointList.map {
                        LatLng(
                            it.coordinates.latitude,
                            it.coordinates.longitude
                        )
                    })
                    .width(MapParametr.POLYLINE_WIDTH.parametr)
                    .color(Color.RED)
            )
        }
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


}