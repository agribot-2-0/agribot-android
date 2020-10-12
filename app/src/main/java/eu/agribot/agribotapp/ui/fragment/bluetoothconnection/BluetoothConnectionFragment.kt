package eu.agribot.agribotapp.ui.fragment.bluetoothconnection

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ManualControlNavigationEvent
import eu.agribot.agribotapp.communication.bluetooth.locale.BluetoothDiscoveryBroadcastReceiver
import eu.agribot.agribotapp.databinding.FragmentBluetoothConnectionBinding
import eu.agribot.agribotapp.ui.fragment.bluetoothconnection.recycler.BluetoothNearbyListItemAdapter
import eu.agribot.agribotapp.ui.fragment.bluetoothconnection.recycler.BluetoothNearbyListItemViewModel
import kotlinx.android.synthetic.main.fragment_bluetooth_connection.nearby_device_list
import kotlinx.android.synthetic.main.fragment_bluetooth_connection.refresh_nearby_devices
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BluetoothConnectionFragment :
    BaseFragment<BluetoothConnectionViewModel, FragmentBluetoothConnectionBinding>() {
    private var adapter =
        BluetoothNearbyListItemAdapter(onClickSelectedItem = this::connectWithSelectedDevice)
    override val layoutResourceId: Int = R.layout.fragment_bluetooth_connection
    override val viewModel: BluetoothConnectionViewModel by viewModel { parametersOf(requireContext()) }

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothDiscoveryBroadcastReceiver: BroadcastReceiver
    private lateinit var bluetoothDeviceAddress: String

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
        viewModel.toastEvent.observe(this::showToast)
        viewModel.checkPermissions(this::startDiscovery)
    }

    private fun setupRecycler() {
        nearby_device_list.adapter = adapter
        nearby_device_list.layoutManager = LinearLayoutManager(requireActivity())
        refresh_nearby_devices.setOnRefreshListener {
            startDiscovery()
            refresh_nearby_devices.isRefreshing = false
        }
    }

    private fun connectWithSelectedDevice(deviceInfo: BluetoothNearbyListItemViewModel) {
        bluetoothDeviceAddress = deviceInfo.bluetoothAddress
        viewModel.connectWithSelectedDevice(bluetoothDeviceAddress)
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
