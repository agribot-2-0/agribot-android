package eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.toolparameters

import androidx.recyclerview.widget.DiffUtil

class ListToolParamDiffUtill(
    private val oldList: List<ListToolParamViewModel>,
    private val newList: List<ListToolParamViewModel>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].toolParamID == newList[newItemPosition].toolParamID
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
