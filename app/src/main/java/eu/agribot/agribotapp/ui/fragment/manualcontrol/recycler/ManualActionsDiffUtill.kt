package eu.agribot.agribotapp.ui.fragment.manualcontrol.recycler

import androidx.recyclerview.widget.DiffUtil

class ManualActionsDiffUtill(
    private val oldList: List<ManualActionsViewModel>,
    private val newList: List<ManualActionsViewModel>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].actionID == newList[newItemPosition].actionID
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
