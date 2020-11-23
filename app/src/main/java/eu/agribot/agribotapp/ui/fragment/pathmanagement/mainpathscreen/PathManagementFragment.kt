package eu.agribot.agribotapp.ui.fragment.pathmanagement.mainpathscreen

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
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
import eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler.CloudFileListItemAdapter
import eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler.CloudFileListItemViewModel
import kotlinx.android.synthetic.main.fragment_path_management.pathManagementRecycler
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PathManagementFragment :
    BaseFragment<PathManagementViewModel, FragmentPathManagementBinding>() {

    private lateinit var mapFragment: SupportMapFragment
    val ZOOM = 15F

    override val layoutResourceId: Int = R.layout.fragment_path_management
    private val dialogManager: DialogManager by inject { parametersOf(requireContext()) }

    override val viewModel: PathManagementViewModel by viewModel()
    private var adapter = CloudFileListItemAdapter(this::onItemClicked)

    override fun bindViewVariables(viewBinding: FragmentPathManagementBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        observeViewModel()
        viewModel.addDefaultMarker()
        viewModel.downloadAvailableFileList()
        setupRecycler()
    }

    private fun observeViewModel() {
        viewModel.markers.observe(this::onMarkerChanged)
        viewModel.availableFiles.observe(this::addToAvailableFile)
    }

    private fun onItemClicked(availibleFile: CloudFileListItemViewModel) {
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

    private fun initMap() {
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
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

    private fun onMarkerChanged(marker: MarkerOptions?) {
        mapFragment.getMapAsync { googleMap ->
            googleMap.addMarker(marker)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker?.position, ZOOM))
        }
    }
}
