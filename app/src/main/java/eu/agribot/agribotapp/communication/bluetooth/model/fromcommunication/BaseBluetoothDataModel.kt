package eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication

interface BaseBluetoothDataModel<T> {
    fun toDomainModel(): T
}
