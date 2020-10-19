package eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.maps.android.kml.KmlLayer
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskTimeNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentCreatorPathSelectBinding
import eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler.CloudFileListItemAdapter
import eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler.CloudFileListItemViewModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.MapParameters
import eu.agribot.agribotapp.ui.fragment.taskcreator.TileProvider
import eu.agribot.agribotapp.ui.fragment.taskcreator.URLPath
import kotlinx.android.synthetic.main.fragment_creator_path_select.recycler_path_select
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import java.io.InputStream

class FragmentCreatorPathSelectFragment :
    BaseFragment<CreatorPathSelectViewModel, FragmentCreatorPathSelectBinding>(),
    OnMapReadyCallback {

    override val layoutResourceId: Int = R.layout.fragment_creator_path_select
    override val viewModel: CreatorPathSelectViewModel by viewModel()
    private var adapter = CloudFileListItemAdapter(this::onItemClicked)
    private var googleMapFragment: GoogleMap? = null
    var botMarker: Marker? = null
    var mapPathLayer: KmlLayer? = null


    override fun bindViewVariables(viewBinding: FragmentCreatorPathSelectBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is CreatorTaskTimeNavigationEvent -> navigateToCreatorTaskTimeFragment()
        else -> Unit
    }

    private fun updateMarker(agribotMarker: MarkerOptions) {
        if (googleMapFragment != null && agribotMarker.position != null) {
            if (botMarker != null) {
                botMarker!!.remove()
            }
            try {
                botMarker = googleMapFragment!!.addMarker(
                    MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.tank))
                        .position(agribotMarker.position)
                        .anchor(0.5f, 0.5f)
                        .rotation(agribotMarker.rotation)
                        .flat(true)
                )
                googleMapFragment!!.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        agribotMarker.position, 21F
                    )
                )
            } catch (exception: IOException) {
                showToast(getString(R.string.marker_problem))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkPathSelected()
        viewModel.availableFiles.observe(this::addToAvailableFile)
        viewModel.downloadAvailableFileList()
        viewModel.getAgribotPosition()
        viewModel.selectedLayer.observe(this::addSelectedLayer)
        viewModel.markers.observe(this::updateMarker)
        viewModel.toastEvent.observe(this::showToast)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.creator_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setupRecycler()
    }

    private fun navigateToCreatorTaskTimeFragment() {
        navigationController?.navigate(R.id.action_taskPathCreatorFragment_to_taskTimeCreatorFragment)
    }

    private fun onItemClicked(availibleFile: CloudFileListItemViewModel) {
        viewModel.downloadSelectedFile(availibleFile.fileName)
    }

    private fun setupRecycler() {
        recycler_path_select.adapter = adapter
        recycler_path_select.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun addToAvailableFile(fileInfo: List<CloudFileListItemViewModel>) {
        adapter.items = fileInfo
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
