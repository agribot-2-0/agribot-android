package eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
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
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskNameNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskPathNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentCreatorConfiguratorBinding
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateAdapter
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateViewModel
import eu.agribot.agribotapp.ui.fragment.swiperecyclerutils.SwipeRecyclerUtil
import eu.agribot.agribotapp.ui.fragment.taskcreator.MapParameters
import eu.agribot.agribotapp.ui.fragment.taskcreator.TileProvider
import eu.agribot.agribotapp.ui.fragment.taskcreator.URLPath
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.TaskListItemAdapter
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.TaskListItemViewModel
import kotlinx.android.synthetic.main.fragment_creator_configurator.task_details_recycler
import kotlinx.android.synthetic.main.fragment_creator_configurator.task_recycler
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.IOException
import java.io.InputStream

class TaskCreatorFragment :
    BaseFragment<TaskCreatorViewModel, FragmentCreatorConfiguratorBinding>(), OnMapReadyCallback {

    private val taskDetailsAdapter = RobotStateAdapter()
    private val dialogManager: DialogManager by inject { parametersOf(requireContext()) }
    override val layoutResourceId: Int = R.layout.fragment_creator_configurator
    override val viewModel: TaskCreatorViewModel by viewModel { parametersOf(requireContext()) }
    private var googleMapFragment: GoogleMap? = null
    private var adapter = TaskListItemAdapter(this::onItemClicked)
    var mapPathLayer: KmlLayer? = null
    override fun bindViewVariables(viewBinding: FragmentCreatorConfiguratorBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.toastMessageEvent.observe(this::showToast)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.creator_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setupRecyclers()
        viewModel.getAvailableTask()
        viewModel.selectedLayer.observe(this::onGotSelectedLayer)
        viewModel.taskList.observe(this::addToTaskList)
        viewModel.cameraPosition.observe((this::updateMapCameraPosition))
        viewModel.robotDataStateList.observe(this::addTaskStateItems)
    }

    private fun updateMapCameraPosition(latLng: LatLng) {
        googleMapFragment?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng, MapParameters.MID_ZOOM.mapParametr
            )
        )
    }

    private fun setupRecyclers() {
        setupTaskListRecycler()
        setupTaskDetailsRecycler()
    }

    private fun setupTaskListRecycler() {
        task_recycler.adapter = adapter
        task_recycler.layoutManager = LinearLayoutManager(requireActivity())
        setuptouchHelper()
    }

    private fun setuptouchHelper() {
        ItemTouchHelper(
            SwipeRecyclerUtil(
                requireContext(),
                onEditSwiped = this::editButton,
                onRemoveSwiped = this::deleteButton
            )
        ).attachToRecyclerView(
            task_recycler
        )
    }

    private fun setupTaskDetailsRecycler() {
        task_details_recycler.adapter = taskDetailsAdapter
        task_details_recycler.layoutManager = GridLayoutManager(context, 4)
    }

    private fun refreshRecycler() {
        adapter.notifyDataSetChanged()
    }

    private fun deleteButton(position: Int) {
        showDeleteTaskDialog(position)
    }

    private fun showDeleteTaskDialog(itemPosition: Int) {
        dialogManager.showDeleteTaskDialog(
            itemPosition,
            viewModel::onDeleteButtonClicked,
            this::refreshRecycler
        )
    }

    private fun editButton(position: Int) {
        viewModel.onEditButtonClicked(position)
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is CreatorTaskNameNavigationEvent -> navigateToNewTaskName()
        is CreatorTaskPathNavigationEvent -> navigateToPathSelect()
        else -> Unit
    }

    private fun navigateToPathSelect() {
        navigationController?.navigate(R.id.action_taskCreatorFragment_to_taskPathCreatorFragment)
    }

    private fun navigateToNewTaskName() {
        navigationController?.navigate(R.id.action_taskCreatorFragment_to_taskNameCreatorFragment)
    }

    private fun onItemClicked(taskCreatorViewModel: TaskListItemViewModel) {
        viewModel.onTaskSelected(taskCreatorViewModel)
    }

    private fun addToTaskList(task: List<TaskListItemViewModel>) {
        adapter.items = task
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
        } else {
            setSelectedLayer(mapStream)
        }
    }

    private fun setSelectedLayer(mapStream: InputStream) {
        try {
            mapPathLayer = KmlLayer(googleMapFragment!!, mapStream, requireContext())
            mapPathLayer!!.containers
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
        googleMapFragment!!.mapType = GoogleMap.MAP_TYPE_SATELLITE
        googleMapFragment!!.setMinZoomPreference(MapParameters.MIN_ZOOM.mapParametr)
        googleMapFragment!!.setMaxZoomPreference(MapParameters.MAX_ZOOM.mapParametr)
        addTileOverlay()
    }

    private fun addTaskStateItems(robotTaskStateItems: List<RobotStateViewModel>) {
        taskDetailsAdapter.items = robotTaskStateItems
    }
}
