package eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.DashboardNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentNewPathBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

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
        viewModel.toastMessageEvent.observe(this::showToast)
        viewModel.postInputDialogEvent.observe(this::showCreateCategoryDialog)
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

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) =
        when (navigationEvent) {
            is DashboardNavigationEvent -> navigateToDashboardFragment()
            else -> Unit
        }

    private fun navigateToDashboardFragment() {
        navigationController?.navigate(R.id.action_newPathFragment_to_dashboardFragment)
    }

    private fun showCreateCategoryDialog(show: Boolean) {
        if (show) {
            val dialogBuilder = AlertDialog.Builder(requireContext())
            val dialogView = this.layoutInflater.inflate(R.layout.dialog_path_name, null)
            dialogBuilder.setView(dialogView)
            val editText = dialogView.findViewById<View>(R.id.new_file_edit_text) as EditText
            dialogBuilder.setTitle(R.string.dialog_create_file_title)
            dialogBuilder.setPositiveButton(
                R.string.dialog_create_file_confirm
            ) { dialog, _ ->
                viewModel.createNewLayer(editText.text.toString())
                dialog.dismiss()
            }
            val b = dialogBuilder.create()
            b.show()
        }
    }
}
