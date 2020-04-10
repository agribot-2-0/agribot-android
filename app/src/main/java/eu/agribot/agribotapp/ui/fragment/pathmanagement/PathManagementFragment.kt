package eu.agribot.agribotapp.ui.fragment.pathmanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentPathManagementBinding
import eu.agribot.agribotapp.ui.fragment.autonomousmode.AutonomousModeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PathManagementFragment :
    BaseFragment<PathManagementViewModel, FragmentPathManagementBinding>(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override val layoutResourceId: Int = R.layout.fragment_path_management

    override val viewModel: PathManagementViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentPathManagementBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        setMarker(mMap)
        setCamera(mMap)
    }

    fun initMap(){
        PathManagementViewModel.mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        PathManagementViewModel.mapFragment?.getMapAsync(this)
    }

    fun setMarker(googleMap: GoogleMap?){
        mMap.addMarker(MarkerOptions().position(viewModel.agriBotLatLng).title("Zapraszamy do AgriBot"))}

    fun setCamera(googleMap: GoogleMap?){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(viewModel.agriBotLatLng, viewModel.zoom));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(viewModel.agriBotLatLng))
    }


}
