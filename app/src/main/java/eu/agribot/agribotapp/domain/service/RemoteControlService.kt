package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.bluetooth.basic.connection.BluetoothConnectionManager
import eu.agribot.agribotapp.communication.persistence.PersistentStorage
import timber.log.Timber

class RemoteControlService(
    private val bluetoothConnectionManager: BluetoothConnectionManager,
    private val persistentStorage: PersistentStorage
) {

    // TODO: Remove sample method
    fun sampleRemoteControlAction() {
        persistentStorage.getExampleEntry()
        Timber.d("Sample method called")
    }
}
