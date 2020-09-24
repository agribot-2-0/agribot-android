package eu.agribot.agribotapp.di

import com.google.firebase.auth.FirebaseAuth
import eu.agribot.agribotapp.communication.bluetooth.basic.connection.AndroidBluetoothConnectionManager
import eu.agribot.agribotapp.communication.bluetooth.basic.connection.BluetoothConnectionManager
import eu.agribot.agribotapp.communication.bluetooth.error.connection.AndroidBluetoothConnectionErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.error.connection.BluetoothConnectionErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.error.message.receive.AndroidBluetoothMessageReceiveErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.error.message.receive.BluetoothMessageReceiveErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.error.message.write.AndroidBluetoothMessageWriteErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.error.message.write.BluetoothMessageWriteErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.basic.connection.message.receivedmessage.AndroidReceivedBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.connection.message.receivedmessage.ReceivedBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.connection.message.writemessage.AndroidWriteBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.connection.message.writemessage.WriteBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.connection.socket.AndroidBluetoothSocketManager
import eu.agribot.agribotapp.communication.bluetooth.basic.connection.socket.BluetoothSocketManager
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

    single<BluetoothConnectionManager> {
        AndroidBluetoothConnectionManager(
            bluetoothConnectionErrorHandler = get(),
            bluetoothReceivedBluetoothMessageManager = get(),
            bluetoothSocketManager = get()
        )
    }

    single<BluetoothSocketManager> { AndroidBluetoothSocketManager() }

    single<ReceivedBluetoothMessageManager> {
        AndroidReceivedBluetoothMessageManager(
            receiveErrorHandler = get(),
            writeBluetoothMessageManager = get(),
            bluetoothSocketManager = get()
        )
    }

    single<WriteBluetoothMessageManager> {
        AndroidWriteBluetoothMessageManager(
            bluetoothMessageWriteErrorHandler = get(),
            bluetoothSocketManager = get()
        )
    }

    single<FirebaseService> {
        AndroidFirebaseService(
            firebaseAuth = FirebaseAuth.getInstance()
        )
    }

    single<PersistentStorage> {
        SharedPreferencesPersistentStorage(
            context = androidContext()
        )
    }

    single<BluetoothMessageWriteErrorHandler> {
        AndroidBluetoothMessageWriteErrorHandler()
    }

    single<BluetoothMessageReceiveErrorHandler>
    {
        AndroidBluetoothMessageReceiveErrorHandler()
    }

    single<BluetoothConnectionErrorHandler>
    {
        AndroidBluetoothConnectionErrorHandler()
    }
}
