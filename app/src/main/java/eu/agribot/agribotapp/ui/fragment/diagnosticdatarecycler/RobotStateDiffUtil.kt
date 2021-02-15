package eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler

import androidx.recyclerview.widget.DiffUtil

class RobotStateDiffUtil(
    private val oldList: List<RobotStateViewModel>,
    private val newList: List<RobotStateViewModel>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].stateItemId == newList[newItemPosition].stateItemId
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}