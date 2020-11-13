package eu.agribot.agribotapp.ui.fragment.pathmanagement.mainpathscreen

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.NewPathNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentPathManagementBinding
import eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler.CloudFileListItemAdapter
import eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler.CloudFileListItemViewModel
import kotlinx.android.synthetic.main.fragment_path_management.path_management_recycler
import org.koin.androidx.viewmodel.ext.android.viewModel

class PathManagementFragment :
    BaseFragment<PathManagementViewModel, FragmentPathManagementBinding>() {

    private lateinit var mapFragment: SupportMapFragment
    val ZOOM = 15F

    override val layoutResourceId: Int = R.layout.fragment_path_management

    override val viewModel: PathManagementViewModel by viewModel()
    private var adapter = CloudFileListItemAdapter(this::onItemClicked)

    override fun bindViewVariables(viewBinding: FragmentPathManagementBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        viewModel.markers.observe(this::onMarkerChanged)
        viewModel.addDefaultMarker()
        viewModel.availableFiles.observe(this::addToAvailableFile)
        viewModel.downloadAvailableFileList()
        setupRecycler()
    }

    private fun onItemClicked(availibleFile: CloudFileListItemViewModel) {

    }

    private fun addToAvailableFile(fileInfo: List<CloudFileListItemViewModel>) {
        adapter.items = fileInfo
    }

    private fun setupRecycler() {
        path_management_recycler.adapter = adapter
        path_management_recycler.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun initMap() {
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) =
        when (navigationEvent) {
            is NewPathNavigationEvent -> handleTaskSelectNavigationEvent()
            else -> Unit
        }

    private fun handleTaskSelectNavigationEvent() {
        navigationController?.navigate(R.id.action_pathManagementFragment_to_newPathFragment)
    }

    private fun onMarkerChanged(marker: MarkerOptions?) {
        mapFragment.getMapAsync { googleMap ->
            googleMap.addMarker(marker)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker?.position, ZOOM))
        }
    }
}
