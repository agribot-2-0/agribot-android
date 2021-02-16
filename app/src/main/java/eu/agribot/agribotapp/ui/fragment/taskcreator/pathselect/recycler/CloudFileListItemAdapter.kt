package eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.END
import androidx.recyclerview.widget.ItemTouchHelper.START
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.databinding.ListItemCloudfilesBinding
import kotlinx.android.synthetic.main.list_item_cloudfiles.view.card_list_file
import java.util.Collections

class CloudFileListItemAdapter(onClickSelectedItem: (CloudFileListItemViewModel) -> Unit) :
    RecyclerView.Adapter<CloudFileListItemViewHolder>() {
    private val initializationItemPosition = -1
    private var selectedItemPosition = initializationItemPosition

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

    val simpleItemTouchCallback =
        object : ItemTouchHelper.SimpleCallback(
            // [1] The allowed directions for moving (drag-and-drop) items
            UP or DOWN or START or END,
            // [2] The allowed directions for swiping items
            0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                Log.e("xd", "on move")
                // [3] Do something when an item is moved

                val adapter = recyclerView.adapter
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition

                // [4] Keep up-to-date the underlying data set
                Collections.swap(items, from, to)
                // [5] Tell the adapter to switch the 2 items
                adapter?.notifyItemMoved(from, to)

                return true
            }

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {

                Log.e("xd", "dir:" + direction)
                // [6] Do something when an item is swiped
            }
        }

    private var selectedItem: ((CloudFileListItemViewModel) -> Unit)? = onClickSelectedItem

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
