package eu.agribot.agribotapp

import android.content.Context
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.core.FlipperClient
import com.facebook.soloader.SoLoader
import eu.agribot.agibotapp.BuildConfig
import eu.agribot.agibotapp.base.BaseAppInitializer
import org.koin.core.inject

/**
 * Debug application initializer
 */
class AgribotAppInitializer(private val context: Context) : BaseAppInitializer(context) {

    /*
     Prepare libraries used in debug mode
     */
    override fun prepareBuildVariantLibraries() {
        prepareFlipper()
    }

    /*
     Flipper is injected as a koin dependency
     Because we want to use single instance of it everywhere, and
     It can rely on some other injectable components
     Like database references or OkHttp clients
     */
    private fun prepareFlipper() {
        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(context)) {
            SoLoader.init(context, false)
            val flipperInstance: FlipperClient by inject()
            flipperInstance.start()
        }
    }
}
