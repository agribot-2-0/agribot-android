package eu.agribot.agribotapp.di

import com.google.firebase.auth.FirebaseAuth
import eu.agribot.agribotapp.communication.crashlystic.AndroidCrashlysticService
import eu.agribot.agribotapp.communication.crashlystic.CrashlysticService
import eu.agribot.agribotapp.communication.bluetooth.basic.connection.AndroidBluetoothConnectionManager
import eu.agribot.agribotapp.communication.bluetooth.basic.connection.BluetoothConnectionManager
import eu.agribot.agribotapp.communication.bluetooth.basic.connection.socket.BluetoothSocketManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.heartbeat.HeartbeatManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.AndroidReceivedBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ReceivedBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.heartbeat.AndroidHeartbeatManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.rfid.AndroidRfidMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.rfid.RfidMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.AndroidWriteBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.WriteBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.messagecreator.AndroidBluetoothMessageCreator
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.messagecreator.BluetoothMessageCreator
import eu.agribot.agribotapp.communication.bluetooth.basic.socket.AndroidBluetoothSocketManager
import eu.agribot.agribotapp.communication.bluetooth.error.connection.AndroidBluetoothConnectionErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.error.connection.BluetoothConnectionErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.error.message.receive.AndroidBluetoothMessageReceiveErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.error.message.receive.BluetoothMessageReceiveErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.error.message.write.AndroidBluetoothMessageWriteErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.error.message.write.BluetoothMessageWriteErrorHandler
import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabase
import eu.agribot.agribotapp.communication.database.clouddatabase.FirebaseDatabase
import eu.agribot.agribotapp.communication.database.firebase.AndroidFirebaseService
import eu.agribot.agribotapp.communication.database.firebase.FirebaseService
import eu.agribot.agribotapp.communication.database.firestorage.AndroidFirestorageService
import eu.agribot.agribotapp.communication.database.firestorage.FirestorageService
import eu.agribot.agribotapp.communication.persistence.PersistentStorage
import eu.agribot.agribotapp.communication.persistence.SharedPreferencesPersistentStorage
import eu.agribot.agribotapp.communication.restapi.AndroidRestApiService
import eu.agribot.agribotapp.communication.restapi.RestApiService
import eu.agribot.agribotapp.communication.tcpconnection.AndroidTcpConnectionManager
import eu.agribot.agribotapp.communication.tcpconnection.TcpConnectionManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

/**
 * Koin module used to provide communication layer dependencies
 */
val communicationModule = module {

    single<CrashlysticService> {
        AndroidCrashlysticService(
            authenticationService = get(),
            resourcesProvider = get {
                parametersOf(
                    androidContext()
                )
            })
    }

    single<TcpConnectionManager> {
        AndroidTcpConnectionManager(
            crashlysticService = get()
        )
    }

    single<BluetoothConnectionManager> {
        AndroidBluetoothConnectionManager(
            bluetoothConnectionErrorHandler = get(),
            bluetoothReceivedBluetoothMessageManager = get(),
            bluetoothSocketManager = get(),
            crashlysticService = get()
        )
    }

    single<BluetoothMessageCreator> {
        AndroidBluetoothMessageCreator(bluetoothMessageManager = get())
    }

    single<BluetoothSocketManager> { AndroidBluetoothSocketManager() }

    single<HeartbeatManager> {
        AndroidHeartbeatManager(
            bluetoothWriteBluetoothMessageManager = get()

        )
    }

    single<ReceivedBluetoothMessageManager> {
        AndroidReceivedBluetoothMessageManager(
            receiveErrorHandler = get(),
            writeBluetoothMessageManager = get(),
            bluetoothSocketManager = get(),
            heartbeatManager = get(),
            rfidMessageManager = get(),
            crashlysticService = get()
        )
    }

    single<RfidMessageManager> {
        AndroidRfidMessageManager()
    }

    single<WriteBluetoothMessageManager> {
        AndroidWriteBluetoothMessageManager(
            bluetoothMessageWriteErrorHandler = get(),
            bluetoothSocketManager = get(),
            crashlysticService = get()
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

    single<CloudDatabase> {
        FirebaseDatabase(
            persistentStorage = get(),
            crashlysticService = get()
        )
    }

    single<FirestorageService> {
        AndroidFirestorageService(
            firebaseService = get(),
            crashlysticService = get()
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

    single<RestApiService>
    {
        AndroidRestApiService(
            resourcesProvider = get {
                parametersOf(
                    androidContext()
                )
            },
            persistentStorage = get()
        )
    }
}
