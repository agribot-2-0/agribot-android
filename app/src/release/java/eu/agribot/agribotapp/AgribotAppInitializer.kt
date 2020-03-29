package eu.agribot.agribotapp

import android.content.Context
import eu.agribot.agribotapp.base.BaseAppInitializer

/**
 * Release application initializer
 */
class AgribotAppInitializer(context: Context) : BaseAppInitializer(context) {

    override fun prepareBuildVariantLibraries() {
        // TODO: Set release libraries here
        // For Example: Crash logging, Analytics
    }
}
