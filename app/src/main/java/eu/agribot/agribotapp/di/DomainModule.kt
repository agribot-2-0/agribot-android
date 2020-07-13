package eu.agribot.agribotapp.di

import eu.agribot.agribotapp.domain.error.AuthenticationErrorHandler
import eu.agribot.agribotapp.domain.service.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

/**
 * Koin module used to provide domain layer dependencies
 */
val domainModule = module {

    single {
        PathManagementService(
            firebaseService = get(),
            persistentStorage = get()
        )
    }

    single {
        RemoteControlService(
            bluetoothService = get(),
            persistentStorage = get()
        )
    }

    single {
        AuthenticationService(
            firebaseService = get(),
            authenticationErrorHandler = get()
        )
    }

    single {
        AuthenticationErrorHandler(
            resourcesProvider = get { parametersOf(androidContext()) })
    }

    single {
        BluetoothConnectionService(
            bluetoothService = get()
        )
    }

    single {
        BluetoothUtils(
            bluetoothService = get()
        )
    }


}
