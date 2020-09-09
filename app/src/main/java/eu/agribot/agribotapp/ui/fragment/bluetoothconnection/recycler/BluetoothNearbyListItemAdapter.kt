package eu.agribot.agribotapp.ui.fragment.bluetoothconnection.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.databinding.ListItemNearbyBluetoothDevicesBinding

class BluetoothNearbyListItemAdapter(val onClickSelectedItem: (BluetoothNearbyListItemViewModel) -> Unit) :
    RecyclerView.Adapter<BluetoothNearbyListItemViewHolder>() {

    var items = listOf<BluetoothNearbyListItemViewModel>()
        set(value) {
            DiffUtil.calculateDiff(
                BluetoothNearbyDiffUtil(
                    items,
                    value
                )
            ).also {
                field = value
            }.dispatchUpdatesTo(this)
        }

    var selectedItem: ((BluetoothNearbyListItemViewModel) -> Unit)? = onClickSelectedItem

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

    override fun onBindViewHolder(holder: BluetoothNearbyListItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            selectedItem?.invoke(item)
        }
    }
}
