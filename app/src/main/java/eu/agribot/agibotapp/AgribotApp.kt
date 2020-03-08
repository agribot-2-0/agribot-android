package eu.agribot.agibotapp

import android.app.Application
import eu.agribot.agribotapp.AgribotAppInitializer

/**
 * Base aplication class
 * Calls to library initialization
 */
class AgribotApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AgribotAppInitializer(this).prepareLibraries()
    }
}
