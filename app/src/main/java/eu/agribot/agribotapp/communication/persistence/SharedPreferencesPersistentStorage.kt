package eu.agribot.agribotapp.communication.persistence

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesPersistentStorage(context: Context) : PersistentStorage {

    private val videoStreamSP: SharedPreferences by lazy {
        context.getSharedPreferences(
            PersistentDataDetails.VIDEOSTREAM.persistentName,
            Context.MODE_PRIVATE
        )
    }

    private val bluetoothAddressSP: SharedPreferences by lazy {
        context.getSharedPreferences(
            PersistentDataDetails.BLUETOOTH_ADDRESS.name,
            Context.MODE_PRIVATE
        )
    }

    override fun saveVideoStreamUrl(url: String) {
        videoStreamSP.edit()
            .putString(PersistentDataDetails.VIDEOSTREAM.data, url)
            .apply()
    }

    override fun getVideoStreamUrl(): String =
        videoStreamSP.getString(PersistentDataDetails.VIDEOSTREAM.data, "rtmp://192.168.0.139/live") ?: ""

    override fun saveLastUsedDeviceBluetoothAddress(url: String) {
        bluetoothAddressSP.edit()
            .putString(PersistentDataDetails.BLUETOOTH_ADDRESS.data, url)
            .apply()
    }

    override fun getLastUsedDeviceBluetoothAddress(): String =
        bluetoothAddressSP.getString(PersistentDataDetails.BLUETOOTH_ADDRESS.data, "") ?: ""
}
