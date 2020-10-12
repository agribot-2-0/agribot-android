package eu.agribot.agribotapp.communication.bluetooth.locale

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.FoundBluetoothDeviceBD
import eu.agribot.agribotapp.domain.model.todomain.FoundBluetoothDevice

class BluetoothDiscoveryBroadcastReceiver(
    val onDeviceFound: (foundBluetoothDevice: FoundBluetoothDevice) -> Unit,
    val onDeviceConnected: () -> Unit,
    val onDeviceDisconnected: () -> Unit
) : BroadcastReceiver() {
    companion object {
        const val NO_BLUETOOTH_DEVICE_NAME: String = "NO NAME"
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
        val foundBluetoothDeviceBD: FoundBluetoothDeviceBD
        if (device != null) {
            foundBluetoothDeviceBD = if (device.name != null) {
                FoundBluetoothDeviceBD(device.name, device.address)
            } else {
                FoundBluetoothDeviceBD(NO_BLUETOOTH_DEVICE_NAME, device.address)
            }
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> onDeviceFound(foundBluetoothDeviceBD.toDomainModel())
                BluetoothDevice.ACTION_ACL_CONNECTED -> onDeviceConnected()
                BluetoothDevice.ACTION_ACL_DISCONNECTED -> onDeviceDisconnected()
            }
        }
    }
}
