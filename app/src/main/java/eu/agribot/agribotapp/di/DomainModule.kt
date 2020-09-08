package eu.agribot.agribotapp.di

import eu.agribot.agribotapp.domain.error.AgribotErrorHandler
import eu.agribot.agribotapp.domain.error.AuthenticationErrorHandler
import eu.agribot.agribotapp.domain.service.*
import eu.agribot.agribotapp.domain.service.joystick.manualmode.ManualSteeringJoystickManager
import eu.agribot.agribotapp.domain.service.joystick.ttcontroljoystick.TTControlHorizontalJoystickManager
import eu.agribot.agribotapp.domain.service.joystick.ttcontroljoystick.TTControlVerticalJoystickManager
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
            bluetoothConnectionManager = get(),
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
            resourcesProvider = get {
                parametersOf(
                    androidContext()
                )
            }
        )
    }

    single {
        BluetoothConnectionService(
            bluetoothConnectionManager = get()
        )
    }

    single {
        ManualModeService(
            bluetoothMessageManager = get(),
            manualSteeringJoystickManager = get()
        )
    }

    single { ManualSteeringJoystickManager(writeBluetoothMessageManager = get()) }


    single { AgribotErrorHandler() }

    single {
        TTControlService(horizontalJoystickManager = get(), verticalJoystickManager = get())
    }

    single {
        TTControlVerticalJoystickManager(bluetoothMessageWriteManager = get())
    }

    single { TTControlHorizontalJoystickManager(bluetoothMessageWriteManager = get()) }
}
