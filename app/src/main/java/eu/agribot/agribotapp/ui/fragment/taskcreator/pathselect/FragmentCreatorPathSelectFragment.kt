package eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.maps.android.kml.KmlContainer
import com.google.maps.android.kml.KmlLayer
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskTimeNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentCreatorPathSelectBinding
import eu.agribot.agribotapp.ui.fragment.taskcreator.MapParameters
import eu.agribot.agribotapp.ui.fragment.taskcreator.TileProvider
import eu.agribot.agribotapp.ui.fragment.taskcreator.URLPath
import eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler.CloudFileListItemAdapter
import eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler.CloudFileListItemViewModel
import kotlinx.android.synthetic.main.fragment_creator_path_select.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException
import java.io.InputStream

class FragmentCreatorPathSelectFragment :
    BaseFragment<CreatorPathSelectViewModel, FragmentCreatorPathSelectBinding>(),
    OnMapReadyCallback {

    override val layoutResourceId: Int = R.layout.fragment_creator_path_select
    override val viewModel: CreatorPathSelectViewModel by viewModel()
    private var adapter = CloudFileListItemAdapter(this::onItemClicked)
    private var googleMapFragment: GoogleMap? = null
    var mapPathLayer: KmlLayer? = null

    override fun bindViewVariables(viewBinding: FragmentCreatorPathSelectBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is CreatorTaskTimeNavigationEvent -> navigateToCreatorTaskTimeFragment()
        else -> Unit
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkPathSelected()
        viewModel.availableFiles.observe(this::addToAvailableFile)
        viewModel.downloadAvailableFileList()
        viewModel.selectedLayer.observe(this::onGotSelectedLayer)
        viewModel.toastEvent.observe(this::showToast)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.creator_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setupRecycler()
        viewModel.cameraPosition.observe((this::updateMapCameraPosition))
    }

    private fun updateMapCameraPosition(latLng: LatLng) {
        googleMapFragment?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng, MapParameters.MID_ZOOM.mapParametr
            )
        )
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
                getRootDirPath(requireContext(), URLPath.MAP_PATH.path)
            )
        try {
            googleMapFragment!!.addTileOverlay(
                TileOverlayOptions()
                    .transparency(MapParameters.TRANSPARENCY_VALUE.mapParametr)
                    .tileProvider(tileProvider)
            )
        } catch (exception: IOException) {
            showToast(getString(R.string.map_tile_problem))
        }
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
            mapPathLayer!!.containers
            mapPathLayer!!.addLayerToMap()
            try {
                val container: Iterable<KmlContainer> = mapPathLayer!!.containers
                viewModel.accessContainers(container)
            } catch (exception: IOException) {
                showToast(getString(R.string.kml_layer_problem))
            }
        } catch (exception: Exception) {
            exception.stackTrace
        }
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

    override fun onMapReady(googleMap: GoogleMap) {
        googleMapFragment = googleMap
        googleMapFragment!!.clear()
        googleMapFragment!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMapFragment!!.setMinZoomPreference(MapParameters.MIN_ZOOM.mapParametr)
        googleMapFragment!!.setMaxZoomPreference(MapParameters.MAX_ZOOM.mapParametr)
        addTileOverlay()
    }
}
