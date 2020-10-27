package eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler

import androidx.recyclerview.widget.DiffUtil

class ToolListItemDiffUtill(
    private val oldList: List<ToolListItemViewModel>,
    private val newList: List<ToolListItemViewModel>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].toolName == newList[newItemPosition].toolName
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
