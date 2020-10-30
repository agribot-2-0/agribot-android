package eu.agribot.agribotapp.di

import eu.agribot.agribotapp.domain.error.AuthenticationErrorHandler
import eu.agribot.agribotapp.domain.error.CloudStorageErrorHandler
import eu.agribot.agribotapp.domain.permission.AndroidPermissionManager
import eu.agribot.agribotapp.domain.permission.PermissionManager
import eu.agribot.agribotapp.domain.service.AuthenticationService
import eu.agribot.agribotapp.domain.service.AutonomyService
import eu.agribot.agribotapp.domain.service.BluetoothConnectionService
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.domain.service.PathManagementService
import eu.agribot.agribotapp.domain.service.RemoteControlService
import eu.agribot.agribotapp.domain.service.TTControlService
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.CloudStorageService
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager
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
            bluetoothConnectionManager = get(),
            bluetoothConnectionErrorHandler = get(),
            bluetoothMessageWriteErrorHandler = get(),
            bluetoothMessageReceiveErrorHandler = get(),
            bluetoothWriteBluetoothMessageManager = get()
        )
    }

    single {
        ManualModeService(
            manualSteeringJoystickManager = get(),
            writeBluetoothMessageManager = get(),
            receivedBluetoothMessageManager = get(),
            messageCreator = get()
        )
    }

    single { ManualSteeringJoystickManager(messageCreator = get()) }

    single {
        TTControlService(horizontalJoystickManager = get(), verticalJoystickManager = get())
    }

    single {
        TTControlVerticalJoystickManager(messageCreator = get())
    }

    single { TTControlHorizontalJoystickManager(messageCreator = get()) }

    single { AutonomyService(bluetoothConnectionService = get()) }

    single<PermissionManager> {
        AndroidPermissionManager(
            context = get {
                parametersOf(
                    androidContext()
                )
            }
        )
    }

    single {
        CloudDatabaseService(
            cloudDatabase = get(),
            cloudStorageErrorHandler = get()
        )
    }

    single {
        TaskCreatorDataManager()
    }

    single {
        ToolDataManager()
    }

    single {
        CloudStorageService(
            firestorageService = get(),
            authenticationService = get(),
            cloudStorageErrorHandler = get()
        )
    }

    single {
        CloudStorageErrorHandler(
            resourcesProvider = get {
                parametersOf(
                    androidContext()
                )
            }
        )
    }
}
