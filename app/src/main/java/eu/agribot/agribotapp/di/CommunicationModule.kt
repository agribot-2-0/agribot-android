package eu.agribot.agribotapp.di

import eu.agribot.agribotapp.communication.bluetooth.AndroidBluetoothService
import eu.agribot.agribotapp.communication.bluetooth.BluetoothService
import eu.agribot.agribotapp.communication.firebase.AndroidFirebaseService
import eu.agribot.agribotapp.communication.firebase.FirebaseService
import eu.agribot.agribotapp.communication.persistence.PersistentStorage
import eu.agribot.agribotapp.communication.persistence.SharedPreferencesPersistentStorage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Koin module used to provide communication layer dependencies
 */
val communicationModule = module {

    single<BluetoothService> {
        AndroidBluetoothService()
    }

    single<FirebaseService> {
        AndroidFirebaseService()
    }

    single<PersistentStorage> {
        SharedPreferencesPersistentStorage(
            context = androidContext()
        )
    }
}
