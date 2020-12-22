package eu.agribot.agribotapp.ui.fragment.bluetoothconnection.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.databinding.ListItemNearbyBluetoothDevicesBinding
import kotlinx.android.synthetic.main.list_item_nearby_bluetooth_devices.view.cardDeviceList
import kotlinx.android.synthetic.main.list_item_nearby_bluetooth_devices.view.connectionStatus
import kotlinx.android.synthetic.main.task_list_item.view.cardview_task_item

class BluetoothNearbyListItemAdapter(val onClickSelectedItem: (BluetoothNearbyListItemViewModel) -> Unit) :
    RecyclerView.Adapter<BluetoothNearbyListItemViewHolder>() {
    var selectedItemPosition = -1

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

        holder.itemView.cardDeviceList.setBackgroundResource(R.color.white)
        holder.itemView.connectionStatus.isVisible = false
        if (selectedItemPosition == position) {
            holder.itemView.connectionStatus.isVisible = true
            holder.itemView.cardDeviceList.setBackgroundResource(R.color.base_green_lighter)
        }

        holder.itemView.setOnClickListener {
            onClickSelectedItem.invoke(item)
            selectedItemPosition = holder.adapterPosition
            notifyDataSetChanged()
        }
    }
}
