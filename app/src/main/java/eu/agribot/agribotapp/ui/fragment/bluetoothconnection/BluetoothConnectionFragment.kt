package eu.agribot.agribotapp.ui.fragment.bluetoothconnection

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentBluetoothConnectionBinding
import eu.agribot.agribotapp.ui.fragment.bluetoothconnection.recycler.BluetoothNearbyListItemAdapter
import eu.agribot.agribotapp.ui.fragment.bluetoothconnection.recycler.BluetoothNearbyListItemViewModel
import kotlinx.android.synthetic.main.fragment_bluetooth_connection.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.IOException
import java.util.UUID

class BluetoothConnectionFragment :
    BaseFragment<BluetoothConnectionViewModel, FragmentBluetoothConnectionBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_bluetooth_connection
    override val viewModel: BluetoothConnectionViewModel by viewModel()
    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            handleBtAction(intent)
        }
    }
    private var adapter = BluetoothNearbyListItemAdapter()

    companion object {
        private lateinit var bluetoothAdapter: BluetoothAdapter
        private const val ACCESS_FINE_LOCATION = 1
        lateinit var bluetoothDeviceAddress: String
        var bluetoothSocket: BluetoothSocket? = null
        var AGRI_UUID_SECURE: UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
        var isBluetoothConnected: Boolean = false
    }

    override fun bindViewVariables(viewBinding: FragmentBluetoothConnectionBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        setUpBroadcastReceiver()
        setupRecycler()
        viewModel.discoveredDevices.observe(this::addToDiscoveredDevice)
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
        ConnectToDevice(requireContext()).execute()
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
        registerReceiver(filter)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        bluetoothAdapter.startDiscovery()
    }

    private fun registerReceiver(filter: IntentFilter) {
        requireActivity().registerReceiver(mReceiver, filter)
    }

    fun handleBtAction(intent: Intent) {
        val action = intent.action

        if (BluetoothDevice.ACTION_FOUND == action) {

            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            if (device != null) {
                if (device.name != null) {
                    viewModel.onBluetoothDeviceFound(device.name, device.address)
                } else {
                    viewModel.onBluetoothDeviceFound("No name", device.address)
                }
            } else {
                showSnack(requireView(), "No Bluetooth Device Avaliable")
            }
        }
    }

    private fun addToDiscoveredDevice(deviceInfo: List<BluetoothNearbyListItemViewModel>) {
        adapter.items = deviceInfo
    }

    private fun showSnack(view: View, text: String) {
        val snackbar = Snackbar.make(
            view, text,
            Snackbar.LENGTH_LONG
        ).setAction("Action", null)
        snackbar.setActionTextColor(Color.BLUE)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.GREEN)
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 28f
        snackbar.show()
    }

    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>() {
        private var connectSuccess: Boolean = true
        private val context: Context = c

        override fun doInBackground(vararg p0: Void?): String? {
            try {
                if (bluetoothSocket == null || !isBluetoothConnected) {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = bluetoothAdapter.getRemoteDevice(
                        bluetoothDeviceAddress
                    )
                    bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(
                        AGRI_UUID_SECURE
                    )
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    bluetoothSocket!!.connect()
                }
            } catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!connectSuccess) {
                Toast.makeText(context, "couldn't connect", Toast.LENGTH_LONG).show()
            } else {
                isBluetoothConnected = true
                Toast.makeText(context, "CONNECTED", Toast.LENGTH_LONG).show()
            }
        }
    }
}
