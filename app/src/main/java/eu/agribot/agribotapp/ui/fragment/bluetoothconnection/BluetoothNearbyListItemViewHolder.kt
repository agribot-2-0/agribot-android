package eu.agribot.agribotapp.ui.fragment.bluetoothconnection

import eu.agribot.agribotapp.base.BaseViewHolder
import eu.agribot.agribotapp.databinding.ListItemNearbyBluetoothDevicesBinding


class BluetoothNearbyListItemViewHolder(viewBinding: ListItemNearbyBluetoothDevicesBinding) :
    BaseViewHolder<BluetoothNearbyListItemViewModel, ListItemNearbyBluetoothDevicesBinding>(viewBinding) {
    override fun bind(viewModel: BluetoothNearbyListItemViewModel) {
        viewBinding.viewModel = viewModel
        viewBinding.executePendingBindings()
    }


}