package eu.agribot.agribotapp.ui.dashboard

import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentDashboardBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment :
    BaseFragment<DashboardViewModel, FragmentDashboardBinding>() {

    private lateinit var mapFragment: SupportMapFragment
    private val ZOOM = 15F

    override val layoutResourceId: Int = R.layout.fragment_dashboard
    override val viewModel: DashboardViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentDashboardBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        viewModel.markers.observe(this::onMarkerChanged)
        viewModel.showToastEvent.observe(this::showToast)
        viewModel.addDefaultMarker()
    }

    private fun initMap() {
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    }

    private fun onMarkerChanged(marker: MarkerOptions?) {
        mapFragment.getMapAsync { googleMap ->
            googleMap.addMarker(marker)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker?.position, ZOOM))
        }
    }
}
