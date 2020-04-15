package eu.agribot.agribotapp.ui.fragment.autonomousmode

import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentAutonomousModeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AutonomousModeFragment :
    BaseFragment<AutonomousModeViewModel, FragmentAutonomousModeBinding>() {

    private lateinit var mapFragment: SupportMapFragment
    val ZOOM = 15F

    override val layoutResourceId: Int = R.layout.fragment_autonomous_mode
    override val viewModel: AutonomousModeViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentAutonomousModeBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        viewModel.markers.observe(this::onMarkerChanged)
        viewModel.addDefaultMarker()
    }

    fun initMap() {
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    }

    private fun onMarkerChanged(marker: MarkerOptions?) {
        mapFragment.getMapAsync { googleMap ->
            googleMap.addMarker(marker)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker?.position, ZOOM))
        }
    }
}
