package eu.agribot.agibotapp.base

import android.content.Context
import eu.agribot.agibotapp.di.qaModule
import eu.agribot.agibotapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

/**
 * Base initializer class
 * Used to load and configure libraries
 * On application start
 */
abstract class BaseAppInitializer(private val context: Context) : KoinComponent {

    fun prepareLibraries() {
        prepareUniversalLibraries()
        prepareBuildVariantLibraries()
    }

    abstract fun prepareBuildVariantLibraries()

    private fun prepareUniversalLibraries() {
        prepareKoin()
        prepareTimber()
    }

    private fun prepareKoin() {
        startKoin {
            androidContext(context)
            androidLogger(Level.DEBUG)
            modules(
                viewModelModule,
                qaModule
            )
        }
    }

    private fun prepareTimber() {
        Timber.plant(Timber.DebugTree())
    }
}
