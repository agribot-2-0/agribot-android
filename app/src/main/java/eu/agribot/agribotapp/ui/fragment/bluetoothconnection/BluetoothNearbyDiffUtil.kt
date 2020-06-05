package eu.agribot.agribotapp.ui.fragment.bluetoothconnection

import androidx.recyclerview.widget.DiffUtil

class BluetoothNearbyDiffUtil(
    private val oldList: List<BluetoothNearbyListItemViewModel>,
    private val newList: List<BluetoothNearbyListItemViewModel>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].bluetoothAddress == newList[newItemPosition].bluetoothAddress
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].bluetoothAddress == newList[newItemPosition].bluetoothAddress
    }

}