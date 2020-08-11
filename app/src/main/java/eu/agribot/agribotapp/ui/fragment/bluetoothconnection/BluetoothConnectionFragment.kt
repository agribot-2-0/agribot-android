package eu.agribot.agribotapp.ui.fragment.bluetoothconnection

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.BluetoothConnectionNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentBluetoothConnectionBinding
import eu.agribot.agribotapp.ui.fragment.bluetoothconnection.recycler.BluetoothNearbyListItemAdapter
import eu.agribot.agribotapp.ui.fragment.bluetoothconnection.recycler.BluetoothNearbyListItemViewModel
import kotlinx.android.synthetic.main.fragment_bluetooth_connection.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BluetoothConnectionFragment :
    BaseFragment<BluetoothConnectionViewModel, FragmentBluetoothConnectionBinding>() {

    companion object {
        private const val REQUEST_CONNECT_DEVICE_INSECURE = 102
        private const val REQUEST_ENABLE_BT = 103
        private const val REQUEST_ACCESS_COARSE_LOCATION = 104
        private const val REQUEST_EXTERNAL_STORAGE = 104
        private const val HANDSHAKE_DELAY = 1000L
        private lateinit var bluetoothAdapter: BluetoothAdapter
        private const val ACCESS_FINE_LOCATION = 1
        lateinit var bluetoothDeviceAddress: String
    }

    private var adapter = BluetoothNearbyListItemAdapter()

    override val layoutResourceId: Int = R.layout.fragment_bluetooth_connection
    override val viewModel: BluetoothConnectionViewModel by viewModel()

    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            when {
                BluetoothDevice.ACTION_FOUND == action -> {
                    viewModel.handleBluetoothAction(intent)
                }
                BluetoothDevice.ACTION_ACL_CONNECTED == action -> {
                    showSnack(requireView(), "Connect with Agribot Sucessfull")
                    connectionaction()
                    viewModel.setConnected()
                }
                BluetoothDevice.ACTION_ACL_DISCONNECTED == action -> {
                    showToast("ACTION DISCONNECTED")
                    viewModel.setDisconnected()
                }
            }
        }
    }

    private fun connectionaction() {
        viewModel.registerMessageListener()
        Handler().postDelayed({
            viewModel.sendHandshake()
        }, HANDSHAKE_DELAY)
    }

    override fun bindViewVariables(viewBinding: FragmentBluetoothConnectionBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        setUpBroadcastReceiver()
        setupRecycler()
        checkPermissions()
        viewModel.discoveredDevices.observe(this::addToDiscoveredDevice)
        viewModel.showSnackEvent.observe(this::showToast)
        viewModel.initializeHeartbeatThread()
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is BluetoothConnectionNavigationEvent -> navigateToToolControl()
        else -> Unit
    }

    private fun navigateToToolControl() {
        navigationController?.navigate(R.id.action_bluetoothConnectionFragment_to_manualControlFragment)
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
        showSnack(requireView(), deviceInfo.bluetoothAddress)
        bluetoothDeviceAddress = deviceInfo.bluetoothAddress
        viewModel.connectWithSelectedDevice(bluetoothDeviceAddress)
    }

    private fun setUpBroadcastReceiver() {
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
        val filter = IntentFilter()
        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
        filter.addAction(BluetoothAdapter.EXTRA_CONNECTION_STATE)
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(filter)
        bluetoothAdapter.startDiscovery()
    }

    private fun registerReceiver(filter: IntentFilter) {
        requireActivity().registerReceiver(bluetoothReceiver, filter)
    }

    private fun addToDiscoveredDevice(deviceInfo: List<BluetoothNearbyListItemViewModel>) {
        adapter.items = deviceInfo
    }
}
