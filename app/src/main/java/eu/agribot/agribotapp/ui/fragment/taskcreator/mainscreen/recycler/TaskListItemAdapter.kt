package eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.databinding.TaskListItemBinding

class TaskListItemAdapter(val onClickSelectedItem: (TaskListItemViewModel) -> Unit) :
    RecyclerView.Adapter<TaskListItemViewHolder>() {

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
        holder.itemView.setOnClickListener {
            selectedItem?.invoke(item)
        }
    }
}
