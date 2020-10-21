package eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.databinding.TaskListItemBinding
import kotlinx.android.synthetic.main.task_list_item.view.*

class TaskListItemAdapter(val onClickSelectedItem: (TaskListItemViewModel) -> Unit) :
    RecyclerView.Adapter<TaskListItemViewHolder>() {
    var selectedItemPosition = -1

    var items = listOf<TaskListItemViewModel>()
        set(value) {
            DiffUtil.calculateDiff(
                TaskListItemDiffUtill(
                    items,
                    value
                )
            ).also {
                field = value
            }.dispatchUpdatesTo(this)
        }

    var selectedItem: ((TaskListItemViewModel) -> Unit)? = onClickSelectedItem

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListItemViewHolder =
        TaskListItemViewHolder(
            TaskListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TaskListItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.cardview_task_item.setCardBackgroundColor(Color.WHITE)
        if (selectedItemPosition == position) {
            holder.itemView.cardview_task_item.setCardBackgroundColor(Color.GREEN)
        }
        holder.itemView.setOnClickListener {
            selectedItem?.invoke(item)
            holder.itemView.cardview_task_item.setCardBackgroundColor(Color.WHITE)
            selectedItemPosition = holder.adapterPosition
            notifyDataSetChanged()
        }
    }
}
