package eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentNewPathBinding
import eu.agribot.agribotapp.domain.model.CoordinatesModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.URLPath
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class NewPathFragment :
    BaseFragment<NewPathViewModel, FragmentNewPathBinding>(), OnMapReadyCallback,
    GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
    private var googleMapFragment: GoogleMap? = null
    var tempCordinate: LatLng? = null

    override val layoutResourceId: Int = R.layout.fragment_new_path

    override val viewModel: NewPathViewModel by viewModel { parametersOf(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.new_path_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        viewModel.selectedPoints.observe(this::selectLatLng)
    }

    override fun bindViewVariables(viewBinding: FragmentNewPathBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMapFragment = googleMap
        googleMapFragment!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMapFragment!!.setOnMapClickListener(this)
        googleMapFragment!!.setOnMapLongClickListener(this)
    }

    private fun selectLatLng(coordinate: LatLng) {
        if (tempCordinate != null) {
            googleMapFragment?.addPolyline(
                PolylineOptions()
                    .add(tempCordinate, coordinate)
                    .width(5f)
                    .color(Color.RED)
            )
        }
        tempCordinate = coordinate
    }


    override fun onMapClick(coordinate: LatLng?) {
        showToast("Click! : " + coordinate)
    }

    override fun onMapLongClick(coordinate: LatLng?) {
        viewModel.onPointSelected(coordinate!!)
    }
}
