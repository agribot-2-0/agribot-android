package eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.databinding.ListItemCloudfilesBinding
import kotlinx.android.synthetic.main.list_item_cloudfiles.view.*

class CloudFileListItemAdapter(val onClickSelectedItem: (CloudFileListItemViewModel) -> Unit) :
    RecyclerView.Adapter<CloudFileListItemViewHolder>() {
    val initializationItemPosition = -1
    var selectedItemPosition = initializationItemPosition

    var items = listOf<CloudFileListItemViewModel>()
        set(value) {
            DiffUtil.calculateDiff(
                CloudFilesDiffUtil(
                    items,
                    value
                )
            ).also {
                field = value
            }.dispatchUpdatesTo(this)
        }

    var selectedItem: ((CloudFileListItemViewModel) -> Unit)? = onClickSelectedItem

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CloudFileListItemViewHolder =
        CloudFileListItemViewHolder(
            ListItemCloudfilesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = items.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CloudFileListItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.itemView.card_list_file.setBackgroundResource(R.color.white)
        if (selectedItemPosition == initializationItemPosition && item.isFileSelected) {
            holder.itemView.card_list_file.setBackgroundResource(R.color.base_green_lighter)
        } else if (selectedItemPosition == position) {
            holder.itemView.card_list_file.setBackgroundResource(R.color.base_green_lighter)
        }

        holder.itemView.setOnClickListener {
            selectedItem?.invoke(item)
            selectedItemPosition = holder.adapterPosition
            notifyDataSetChanged()
        }
    }
}
