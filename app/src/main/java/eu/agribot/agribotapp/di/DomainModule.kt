package eu.agribot.agribotapp.di

import eu.agribot.agribotapp.domain.error.AuthenticationErrorHandler
<<<<<<< HEAD
import eu.agribot.agribotapp.domain.service.AuthenticationService
import eu.agribot.agribotapp.domain.service.BluetoothConnectionService
import eu.agribot.agribotapp.domain.service.BluetoothUtils
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.domain.service.PathManagementService
import eu.agribot.agribotapp.domain.service.RemoteControlService
=======
import eu.agribot.agribotapp.domain.service.*
>>>>>>> 0bd8dbd... static code analyze fix
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
<<<<<<< HEAD
            bluetoothService = get()
=======
            bluetoothConnectionService = get(),
            diagnosticDataService = get(),
            agribotErrorHandler = get()
>>>>>>> 0bd8dbd... static code analyze fix
        )
    }

    single {
        ManualModeService(
            bluetoothUtils = get()
        )
    }
<<<<<<< HEAD
=======

    single {
        ToolControlService(
            bluetoothUtils = get()
        )
    }

    single {
        AutonomusModeService(
            bluetoothUtils = get()
        )
    }

    single { DiagnosticDataService() }

}



