package eu.agribot.agribotapp.communication.persistence

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesPersistentStorage(context: Context) : PersistentStorage {

    companion object {
        const val SP_NAME_APPLICATION = "application_sp"
        const val APPLICATION_EXAMPLE_PERSISTENT_DATA = "example_persistent_data"
    }

    private val applicationSP: SharedPreferences by lazy {
        context.getSharedPreferences(SP_NAME_APPLICATION, Context.MODE_PRIVATE)
    }

    override fun getExampleEntry(): String =
        applicationSP.getString(APPLICATION_EXAMPLE_PERSISTENT_DATA, "") ?: ""

    override fun saveExampleEntry(entry: String) =
        applicationSP.edit()
            .putString(APPLICATION_EXAMPLE_PERSISTENT_DATA, entry)
            .apply()
}
