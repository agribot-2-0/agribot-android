package eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler

import androidx.recyclerview.widget.DiffUtil

class TaskListItemDiffUtill(
    private val oldList: List<TaskListItemViewModel>,
    private val newList: List<TaskListItemViewModel>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].taskName == newList[newItemPosition].taskName
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
