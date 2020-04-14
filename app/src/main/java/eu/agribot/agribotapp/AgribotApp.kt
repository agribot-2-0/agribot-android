package eu.agribot.agribotapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

/**
 * Base aplication class
 * Calls to library initialization
 */
class AgribotApp : Application() {

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreate() {
        super.onCreate()
        AgribotAppInitializer(this).prepareLibraries()
    }
}
