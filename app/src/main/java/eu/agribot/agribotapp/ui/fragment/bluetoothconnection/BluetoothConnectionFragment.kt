package eu.agribot.agribotapp.ui.fragment.bluetoothconnection

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentBluetoothConnectionBinding
import kotlinx.android.synthetic.main.fragment_bluetooth_connection.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class BluetoothConnectionFragment :
    BaseFragment<BluetoothConnectionViewModel, FragmentBluetoothConnectionBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_bluetooth_connection
    override val viewModel: BluetoothConnectionViewModel by viewModel()

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetooothPairedDevices: Set<BluetoothDevice>
    val adapter = BluetoothNearbyListItemAdapter()
    companion object {
        private const val ACCESS_FINE_LOCATION = 1
    }

    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            handleBtAction(intent)
        }
    }

    override fun bindViewVariables(viewBinding: FragmentBluetoothConnectionBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        pairedDeviceList()
        setUpBroadcastReceiver()
        nearby_device_list.layoutManager = LinearLayoutManager(requireActivity())
        nearby_device_list.adapter = adapter
        refresh_nearby_devices.setOnRefreshListener { startDiscovery()
        refresh_nearby_devices.isRefreshing = false}
    }

    private fun pairedDeviceList() {
        bluetooothPairedDevices = bluetoothAdapter.bondedDevices
        val list: ArrayList<String> = ArrayList()

        if (bluetooothPairedDevices.isNotEmpty()) {
            for (device: BluetoothDevice in bluetooothPairedDevices) {
                list.add("Device name: " + device.name + "Device Adress: " + device.address)
            }
        } else {
            showSnack(requireView(), "No Paired Bluetooth Device Avaliable")
        }

       // device_list.adapter = adapter

    }

    private fun setUpBroadcastReceiver() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                ACCESS_FINE_LOCATION
            )
            return
        }
        startDiscovery()
    }

    private fun startDiscovery() {
        Timber.e("refresh")
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        requireActivity().registerReceiver(mReceiver, filter)
        bluetoothAdapter.startDiscovery()
    }

    fun handleBtAction(intent: Intent) {
        val action = intent.action

        if (BluetoothDevice.ACTION_FOUND == action) {
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            if (device != null) {
                addToDiscoveredDevice(listOf(BluetoothNearbyListItemViewModel(device.name,device.address)))
            } else {
                showSnack(requireView(), "No Bluetooth Device Avaliable")
            }

        }
    }


    private fun addToDiscoveredDevice(deviceInfo: List<BluetoothNearbyListItemViewModel>) {
        Timber.e("add" + deviceInfo.toString())
        adapter.items = deviceInfo
        adapter.notifyDataSetChanged()
    }



    private fun showSnack(view: View, text: String) {
        val snackbar = Snackbar.make(
            view, text,
            Snackbar.LENGTH_LONG
        ).setAction("Action", null)
        snackbar.setActionTextColor(Color.BLUE)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.LTGRAY)
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.BLUE)
        textView.textSize = 28f
        snackbar.show()
    }

}



