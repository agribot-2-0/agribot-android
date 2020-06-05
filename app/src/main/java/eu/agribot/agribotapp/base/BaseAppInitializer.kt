package eu.agribot.agribotapp.base

import android.content.Context
import eu.agribot.agribotapp.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

/**
 * Base initializer class
 * Used to load and configure libraries
 * Subclassed on each build variant to load
 * Build sepcific libraries
 */
abstract class BaseAppInitializer(private val context: Context) : KoinComponent {

    /**
     * Loads all libraries used by project
     * In both Base and subclassed initializers
     */
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
                appModule,
                communicationModule,
                domainModule,
                viewModelModule,
                qaModule
            )
        }
    }

    private fun prepareTimber() {
        Timber.plant(Timber.DebugTree())
    }
}
