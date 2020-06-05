package eu.agribot.agribotapp.ui.fragment.bluetoothconnection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.databinding.ListItemNearbyBluetoothDevicesBinding

class BluetoothNearbyListItemAdapter : RecyclerView.Adapter<BluetoothNearbyListItemViewHolder>(){


    var items = listOf<BluetoothNearbyListItemViewModel>()
        set(value) {
            DiffUtil.calculateDiff(
                BluetoothNearbyDiffUtil(items, value)
            ).also {
                field = value
            }.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BluetoothNearbyListItemViewHolder =
        BluetoothNearbyListItemViewHolder(
            ListItemNearbyBluetoothDevicesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: BluetoothNearbyListItemViewHolder, position: Int) =
        holder.bind(items[position])



}