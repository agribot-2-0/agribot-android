package eu.agribot.agribotapp.communication.bluetooth.locale

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import eu.agribot.agribotapp.communication.bluetooth.model.FoundBluetoothDevice

class BluetoothDiscoveryBroadcastReceiver(
    val onDeviceFound: (foundBluetoothDevice: FoundBluetoothDevice) -> Unit,
    val onDeviceConnected: () -> Unit,
    val onDeviceDisconnected: () -> Unit
) : BroadcastReceiver() {

    companion object {
        val FILTER = IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
            addAction(BluetoothAdapter.EXTRA_CONNECTION_STATE)
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        val foundBluetoothDevice: FoundBluetoothDevice
        if (device != null) {
            foundBluetoothDevice = if (device.name != null) {
                FoundBluetoothDevice(device.name, device.address)
            } else {
                FoundBluetoothDevice("No name", device.address)
            }
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> onDeviceFound(foundBluetoothDevice)
                BluetoothDevice.ACTION_ACL_CONNECTED -> onDeviceConnected()
                BluetoothDevice.ACTION_ACL_DISCONNECTED -> onDeviceDisconnected()
            }
        }
    }
}