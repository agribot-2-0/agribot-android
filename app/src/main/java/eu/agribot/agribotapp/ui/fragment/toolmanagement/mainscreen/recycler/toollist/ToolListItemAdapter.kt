package eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.toollist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.databinding.ListItemToolBinding
import kotlinx.android.synthetic.main.list_item_tool.view.cardview_tool_item
import kotlinx.android.synthetic.main.task_list_item.view.*

class ToolListItemAdapter(private val onClickSelectedItem: (ToolListItemViewModel) -> Unit) :
    RecyclerView.Adapter<ToolListItemViewHolder>() {
    var selectedItemPosition = -1

    var items = listOf<ToolListItemViewModel>()
        set(value) {
            DiffUtil.calculateDiff(
                ToolListItemDiffUtill(
                    items,
                    value
                )
            ).also {
                field = value
            }.dispatchUpdatesTo(this)
        }

    var selectedItem: ((ToolListItemViewModel) -> Unit)? = onClickSelectedItem

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolListItemViewHolder =
        ToolListItemViewHolder(
                ListItemToolBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        )

    override fun getItemCount(): Int = items.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ToolListItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.itemView.cardview_tool_item.setBackgroundResource(R.color.white)
        if (selectedItemPosition == position) {
            holder.itemView.cardview_tool_item.setBackgroundResource(R.color.base_green_lighter)
        }
        holder.itemView.setOnClickListener {
            selectedItem?.invoke(item)
            selectedItemPosition = holder.adapterPosition
            notifyDataSetChanged()
        }
    }
}
