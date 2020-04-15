package eu.agribot.agribotapp.ui.fragment.pathmanagement

import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentPathManagementBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PathManagementFragment :
    BaseFragment<PathManagementViewModel, FragmentPathManagementBinding>(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    val ZOOM = 15F

    override val layoutResourceId: Int = R.layout.fragment_path_management

    override val viewModel: PathManagementViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentPathManagementBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
    }

    fun initMap(){
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        viewModel.setMap(mMap)
        moveCamera(viewModel.agriBotLatLng)
    }

    fun moveCamera(mLatLng: LatLng){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng,ZOOM))
    }
}
