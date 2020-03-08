package eu.agribot.agribotapp.di

import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.core.FlipperClient
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.navigation.NavigationFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import org.koin.dsl.module

/**
 * Koin module related to
 * Quality assurence libraries
 */
val qaModule = module {
    single<FlipperClient> {
        AndroidFlipperClient.getInstance(get()).apply {
            addPlugin(get<InspectorFlipperPlugin>())
            addPlugin(get<NavigationFlipperPlugin>())
            addPlugin(get<SharedPreferencesFlipperPlugin>())
            addPlugin(get<CrashReporterPlugin>())
        }
    }

    single {
        InspectorFlipperPlugin(get(), DescriptorMapping.withDefaults())
    }

    single {
        NavigationFlipperPlugin.getInstance()
    }

    single {
        SharedPreferencesFlipperPlugin(get())
    }

    single {
        CrashReporterPlugin.getInstance()
    }
}
