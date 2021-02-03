package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolsummary.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.databinding.ListItemRfidDataBinding
import kotlinx.android.synthetic.main.list_item_rfid_data.view.cardview_rfid_data_item

class ReceivedRfidDataAdapter(onItemCLicked: (ReceivedRfidDataViewModel) -> Unit) :
    RecyclerView.Adapter<ReceivedRfidDataViewHolder>() {
    private var selectedItemPosition = -1

    var items = listOf<ReceivedRfidDataViewModel>()
        set(value) {
            DiffUtil.calculateDiff(
                ReceivedRfidDataDiffUtill(
                    items,
                    value
                )
            ).also {
                field = value
            }.dispatchUpdatesTo(this)
        }

    private var selectedItem: ((ReceivedRfidDataViewModel) -> Unit)? = onItemCLicked

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedRfidDataViewHolder =
        ReceivedRfidDataViewHolder(
            ListItemRfidDataBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        )

    override fun getItemCount(): Int = items.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ReceivedRfidDataViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.itemView.cardview_rfid_data_item.setBackgroundResource(R.color.white)
        if (selectedItemPosition == position) {
            holder.itemView.cardview_rfid_data_item.setBackgroundResource(R.color.base_green_lighter)
        }
        holder.itemView.setOnClickListener {
            selectedItem?.invoke(item)
            selectedItemPosition = holder.adapterPosition
            notifyDataSetChanged()
        }
    }
}
