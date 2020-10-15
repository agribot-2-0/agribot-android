package eu.agribot.agribotapp.ui.fragment.dashboard

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

    private lateinit var googleMapFragment: SupportMapFragment
    private val mapZoom = 15F

    override val layoutResourceId: Int = R.layout.fragment_dashboard
    override val viewModel: DashboardViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentDashboardBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeGoogleMaps()
        viewModel.markers.observe(this::onMarkerChanged)
        viewModel.showToastEvent.observe(this::showToast)
        viewModel.addDefaultMarker()
    }

    private fun initializeGoogleMaps() {
        googleMapFragment = childFragmentManager.findFragmentById(R.id.dashboard_map) as SupportMapFragment
    }

    private fun onMarkerChanged(marker: MarkerOptions?) {
        googleMapFragment.getMapAsync { googleMap ->
            googleMap.addMarker(marker)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker?.position, mapZoom))
        }
    }
}
