package eu.agribot.agribotapp.ui.fragment.pathmanagement.mainpathscreen

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
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
import eu.agribot.agribotapp.common.dialog.DialogManager
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.NewPathNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.PathNameNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentPathManagementBinding
import eu.agribot.agribotapp.ui.fragment.swiperecyclerutils.SwipeHelper
import eu.agribot.agribotapp.ui.fragment.swiperecyclerutils.SwipeRecyclerClickListener
import eu.agribot.agribotapp.ui.fragment.swiperecyclerutils.SwipeRecyclerHelper
import eu.agribot.agribotapp.ui.fragment.taskcreator.MapParameters
import eu.agribot.agribotapp.ui.fragment.taskcreator.TileProvider
import eu.agribot.agribotapp.ui.fragment.taskcreator.URLPath
import eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler.CloudFileListItemAdapter
import eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler.CloudFileListItemViewModel
import kotlinx.android.synthetic.main.fragment_path_management.pathManagementRecycler
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.IOException
import java.io.InputStream

class PathManagementFragment :
    BaseFragment<PathManagementViewModel, FragmentPathManagementBinding>(), OnMapReadyCallback {

    private var googleMapFragment: GoogleMap? = null

    override val layoutResourceId: Int = R.layout.fragment_path_management
    private val dialogManager: DialogManager by inject { parametersOf(requireContext()) }

    override val viewModel: PathManagementViewModel by viewModel { parametersOf(requireContext()) }
    private var adapter = CloudFileListItemAdapter(this::onItemClicked)
    private var mapPathLayer: KmlLayer? = null

    override fun bindViewVariables(viewBinding: FragmentPathManagementBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) =
        when (navigationEvent) {
            is PathNameNavigationEvent -> handlePathNameNavigationEvent()
            is NewPathNavigationEvent -> handleNewPathNavigationEvent()
            else -> Unit
        }

    private fun handlePathNameNavigationEvent() {
        navigationController?.navigate(R.id.action_pathManagementFragment_to_pathNameFragment)
    }

    private fun handleNewPathNavigationEvent() {
        navigationController?.navigate(R.id.action_pathManagementFragment_to_newPathFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.path_management_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        observeViewModel()
        viewModel.downloadAvailableFileList()
        setupRecycler()
    }

    private fun observeViewModel() {
        viewModel.availableFiles.observe(this::addToAvailableFile)
        viewModel.selectedLayer.observe(this::onGotSelectedLayer)
        viewModel.toastMessageEvent.observe(this::showToast)
        viewModel.cameraPosition.observe((this::updateMapCameraPosition))
    }

    private fun onItemClicked(availibleFile: CloudFileListItemViewModel) {
        viewModel.openSelectedLayer(availibleFile.fileName)
    }

    private fun addToAvailableFile(fileInfo: List<CloudFileListItemViewModel>) {
        adapter.items = fileInfo
    }

    private fun setupRecycler() {
        pathManagementRecycler.adapter = adapter
        pathManagementRecycler.layoutManager = LinearLayoutManager(requireActivity())

        pathManagementRecycler.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        ItemTouchHelper(
            SwipeRecyclerHelper(
                pathManagementRecycler,
                deleteButton = this::deleteButton,
                editButton = this::editButton
            )
        ).attachToRecyclerView(pathManagementRecycler)
    }

    private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            getString(R.string.delete_task_button),
            resources.getDimension(R.dimen.text_size_small),
            android.R.color.holo_red_light,
            SwipeRecyclerClickListener(position, onButtonClicked = this::showDeletePathDialog)
        )
    }

    private fun showDeletePathDialog(itemPosition: Int) {
        dialogManager.showDeletePathDialog(itemPosition, viewModel::onDeleteButtonClicked)
    }

    private fun editButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            getString(R.string.edit_task_button),
            resources.getDimension(R.dimen.text_size_small),
            android.R.color.holo_blue_light,
            SwipeRecyclerClickListener(position, onButtonClicked = viewModel::onEditButtonClicked)
        )
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMapFragment = googleMap
        googleMapFragment!!.clear()
        googleMapFragment!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMapFragment!!.setMinZoomPreference(MapParameters.MIN_ZOOM.mapParametr)
        googleMapFragment!!.setMaxZoomPreference(MapParameters.MAX_ZOOM.mapParametr)
        addTileOverlay()
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

    private fun updateMapCameraPosition(latLng: LatLng) {
        googleMapFragment?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng, MapParameters.MID_ZOOM.mapParametr
            )
        )
    }
}
