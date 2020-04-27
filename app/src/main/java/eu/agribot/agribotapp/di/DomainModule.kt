package eu.agribot.agribotapp.di

import eu.agribot.agribotapp.domain.service.LoginService
import eu.agribot.agribotapp.domain.service.PathManagementService
import eu.agribot.agribotapp.domain.service.RemoteControlService
import eu.agribot.agribotapp.domain.service.SignupService
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
        SignupService(
            firebaseService = get()
        )
    }

    single {
        LoginService(
            firebaseService = get()
        )
    }
}
