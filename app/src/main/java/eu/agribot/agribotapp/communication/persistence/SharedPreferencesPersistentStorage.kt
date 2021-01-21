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
}
