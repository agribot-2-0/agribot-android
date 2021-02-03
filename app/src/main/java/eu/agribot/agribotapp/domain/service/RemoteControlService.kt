package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.bluetooth.basic.connection.BluetoothConnectionManager
import eu.agribot.agribotapp.communication.persistence.PersistentStorage

class RemoteControlService(
    private val bluetoothConnectionManager: BluetoothConnectionManager,
    private val persistentStorage: PersistentStorage
)
