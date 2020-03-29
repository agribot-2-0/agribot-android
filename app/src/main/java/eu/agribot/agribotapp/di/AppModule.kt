package eu.agribot.agribotapp.di

import android.content.Context
import eu.agribot.agribotapp.common.dialog.AndroidDialogManager
import eu.agribot.agribotapp.common.dialog.DialogManager
import eu.agribot.agribotapp.common.resources.AndroidResourcesProvider
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

/**
 * Koin module used to provide appwide dependencies
 */
val appModule = module {

    factory<ResourcesProvider> { (context: Context) ->
        AndroidResourcesProvider(
            context = context
        )
    }

    factory<DialogManager> { (context: Context) ->
        AndroidDialogManager(
            context = context,
            resourcesProvider = get { parametersOf(context) }
        )
    }
}
