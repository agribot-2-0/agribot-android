package eu.agribot.agribotapp.ui.fragment.bluetoothconnection.recycler

import eu.agribot.agribotapp.base.BaseViewModel

data class BluetoothNearbyListItemViewModel(
    val bluetoothName: String,
    val bluetoothAddress: String
) : BaseViewModel()
