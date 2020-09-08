package eu.agribot.agribotapp.ui.fragment.bluetoothconnection

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ManualControlNavigationEvent
import eu.agribot.agribotapp.communication.bluetooth.locale.BluetoothDiscoveryBroadcastReceiver
import eu.agribot.agribotapp.databinding.FragmentBluetoothConnectionBinding
import eu.agribot.agribotapp.ui.fragment.bluetoothconnection.recycler.BluetoothNearbyListItemAdapter
import eu.agribot.agribotapp.ui.fragment.bluetoothconnection.recycler.BluetoothNearbyListItemViewModel
import kotlinx.android.synthetic.main.fragment_bluetooth_connection.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BluetoothConnectionFragment :
    BaseFragment<BluetoothConnectionViewModel, FragmentBluetoothConnectionBinding>() {
    private var adapter = BluetoothNearbyListItemAdapter()
    override val layoutResourceId: Int = R.layout.fragment_bluetooth_connection
    override val viewModel: BluetoothConnectionViewModel by viewModel()

    companion object {
        private const val REQUEST_CONNECT_DEVICE_INSECURE = 102
        private const val REQUEST_ENABLE_BT = 103
        private const val REQUEST_ACCESS_COARSE_LOCATION = 104
        private const val REQUEST_EXTERNAL_STORAGE = 104
        private lateinit var bluetoothAdapter: BluetoothAdapter
        private lateinit var bluetoothDiscoveryBroadcastReceiver: BroadcastReceiver
        private const val ACCESS_FINE_LOCATION = 1
        lateinit var bluetoothDeviceAddress: String
    }

    override fun bindViewVariables(viewBinding: FragmentBluetoothConnectionBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ManualControlNavigationEvent -> navigateToToolControl()
        else -> Unit
    }

    private fun navigateToToolControl() {
        navigationController?.navigate(R.id.action_bluetoothConnectionFragment_to_manualControlFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothDiscoveryBroadcastReceiver = BluetoothDiscoveryBroadcastReceiver(
            viewModel::onBluetoothDeviceFound,
            viewModel::onBluetoothDeviceConnected,
            viewModel::onBluetoothDeviceDisconnected
        )
        refresh_nearby_devices
        setupRecycler()
        viewModel.discoveredDevices.observe(this::addToDiscoveredDevice)
        viewModel.showSnackEvent.observe(this::showToast)
        checkPermissions()
        setupReceiver()
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_ACCESS_COARSE_LOCATION
            )
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN),
                REQUEST_ENABLE_BT
            )
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN),
                REQUEST_CONNECT_DEVICE_INSECURE
            )
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    private fun setupRecycler() {
        nearby_device_list.adapter = adapter
        nearby_device_list.layoutManager = LinearLayoutManager(requireActivity())
        refresh_nearby_devices.setOnRefreshListener {
            startDiscovery()
            refresh_nearby_devices.isRefreshing = false
        }
        adapter.selectedItem = { it -> connectWithSelectedDevice(it) }
    }

    private fun connectWithSelectedDevice(deviceInfo: BluetoothNearbyListItemViewModel) {
        bluetoothDeviceAddress = deviceInfo.bluetoothAddress
        viewModel.connectWithSelectedDevice(bluetoothDeviceAddress)
    }

    private fun setupReceiver() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                ACCESS_FINE_LOCATION
            )
            return
        }
        startDiscovery()
    }

    private fun startDiscovery() {
        registerMyReceiver(BluetoothDiscoveryBroadcastReceiver.FILTER)
        bluetoothAdapter.startDiscovery()
    }

    private fun registerMyReceiver(filter: IntentFilter) {
        requireContext().registerReceiver(bluetoothDiscoveryBroadcastReceiver, filter)
    }

    private fun addToDiscoveredDevice(deviceInfo: List<BluetoothNearbyListItemViewModel>) {
        adapter.items = deviceInfo
    }

}


