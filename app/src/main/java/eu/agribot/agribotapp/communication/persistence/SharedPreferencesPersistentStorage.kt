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

    private val robotSerialNumberSP: SharedPreferences by lazy {
        context.getSharedPreferences(
            PersistentDataDetails.ROBOT_SERIAL_NUMBER.persistentName,
            Context.MODE_PRIVATE
        )
    }

    override fun saveVideoStreamUrl(url: String) {
        videoStreamSP.edit()
            .putString(PersistentDataDetails.VIDEOSTREAM.data, url)
            .apply()
    }

    override fun getVideoStreamUrl(): String =
        videoStreamSP.getString(
            PersistentDataDetails.VIDEOSTREAM.data,
            PersistentDataDetails.VIDEOSTREAM.defaultValue
        ) ?: ""

    override fun saveRobotSerialNumber(serial: String) {
        robotSerialNumberSP.edit()
            .putString(PersistentDataDetails.ROBOT_SERIAL_NUMBER.data, serial)
            .apply()
    }

    override fun getRobotSerialNumber(): String =
        robotSerialNumberSP.getString(
            PersistentDataDetails.ROBOT_SERIAL_NUMBER.data,
            PersistentDataDetails.ROBOT_SERIAL_NUMBER.defaultValue
        ) ?: ""
}
