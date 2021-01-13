package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.persistence.PersistentStorage

class AppStorageService(private val persistentStorage: PersistentStorage) {

    fun getStreamUrl(): String = persistentStorage.getVideoStreamUrl()

    fun saveStreamUrl(newUrl: String) {
        persistentStorage.saveVideoStreamUrl(newUrl)
    }

    fun getLastUsedBluetoothAddress(): String =
        persistentStorage.getLastUsedDeviceBluetoothAddress()

    fun saveLastUsedBluetoothAddress(bluetoothAddress: String) {
        persistentStorage.saveLastUsedDeviceBluetoothAddress(bluetoothAddress)
    }
}
