package eu.agribot.agribotapp.ui.fragment.controlPanel

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
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.maps.android.kml.KmlContainer
import com.google.maps.android.kml.KmlLayer
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.TaskSelectNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentControlPanelBinding
import eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.DashboardPagerAdapter
import eu.agribot.agribotapp.ui.fragment.taskcreator.MapParameters
import eu.agribot.agribotapp.ui.fragment.taskcreator.TileProvider
import eu.agribot.agribotapp.ui.fragment.taskcreator.URLPath
import kotlinx.android.synthetic.main.fragment_control_panel.pager
import kotlinx.android.synthetic.main.fragment_control_panel.tab_layout
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.IOException
import java.io.InputStream

class ControlPanelFragment :
    BaseFragment<ControlPanelViewModel, FragmentControlPanelBinding>(), OnMapReadyCallback {

    override val layoutResourceId: Int = R.layout.fragment_control_panel
    override val viewModel: ControlPanelViewModel by viewModel { parametersOf(requireContext()) }
    private var googleMapFragment: GoogleMap? = null
    var mapPathLayer: KmlLayer? = null
    var botMarker: Marker? = null
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
        observeViewModel()
        setupViewPager()
    }

    private fun observeViewModel() {
        viewModel.pathStream.observe(this::onGotSelectedLayer)
        viewModel.markers.observe(this::updateMapMarker)
        viewModel.toastMessageEvent.observe(this::showToast)
        viewModel.cameraPosition.observe(this::updateMapCameraPosition)
        viewModel.pathName.observe(this::removeLayerFromMap)
    }

    private fun removeLayerFromMap(pathName: String) {
        if (pathName == NO_SELECTED_PATH_SYMBOL) {
            if (mapPathLayer != null) {
                mapPathLayer!!.removeLayerFromMap()
            }
        }
    }

    private fun setupViewPager() {
        pager.adapter = DashboardPagerAdapter(this)
        TabLayoutMediator(
            tab_layout, pager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text =
                arrayOf(
                    getString(R.string.robot_state),
                    getString(R.string.task_state),
                    getString(R.string.task_list)
                )[position]
        }.attach()
    }

    private fun updateMapCameraPosition(latLng: LatLng) {
        googleMapFragment?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng, getMapZoom()
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

    private fun updateMapMarker(agribotMarker: MarkerOptions) {
        botMarker?.remove()
        if (agribotMarker.position != null) {
            try {
                botMarker = googleMapFragment?.addMarker(
                    MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_agibot_marker))
                        .position(agribotMarker.position)
                        .anchor(MapParameters.ANCHOR.mapParametr, MapParameters.ANCHOR.mapParametr)
                        .rotation(agribotMarker.rotation)
                        .flat(true)
                )
                googleMapFragment?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        agribotMarker.position,
                        getMapZoom()
                    )
                )
            } catch (exception: IOException) {
                showToast(getString(R.string.map_marker_problem))
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
            mapPathLayer!!.addLayerToMap()
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

    override fun onMapReady(googleMap: GoogleMap) {
        googleMapFragment = googleMap
        googleMapFragment!!.clear()
        googleMapFragment!!.setMinZoomPreference(MapParameters.MIN_ZOOM.mapParametr)
        googleMapFragment!!.setMaxZoomPreference(MapParameters.MAX_ZOOM.mapParametr)
        addTileOverlay()
    }

    private fun getMapZoom(): Float {
        return if (googleMapFragment?.cameraPosition!!.zoom > MapParameters.MID_ZOOM.mapParametr) {
            googleMapFragment?.cameraPosition!!.zoom
        } else {
            MapParameters.MID_ZOOM.mapParametr
        }
    }

    companion object {
        const val NO_SELECTED_PATH_SYMBOL = "*"
    }
}
