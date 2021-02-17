package eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.R
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

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: TaskListItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.itemView.tableTaskListItem.setBackgroundResource(R.color.white)
        if (selectedItemPosition == position) {
            holder.itemView.tableTaskListItem.setBackgroundResource(R.color.base_green_lighter)
        }
        holder.itemView.setOnClickListener {
            selectedItem?.invoke(item)
            selectedItemPosition = holder.adapterPosition
            notifyDataSetChanged()
        }
    }
}
