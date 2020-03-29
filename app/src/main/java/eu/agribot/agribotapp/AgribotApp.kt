package eu.agribot.agribotapp

import android.app.Application

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
