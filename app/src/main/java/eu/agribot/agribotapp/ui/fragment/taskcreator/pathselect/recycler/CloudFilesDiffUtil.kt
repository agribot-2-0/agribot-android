package eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler

import androidx.recyclerview.widget.DiffUtil

class CloudFilesDiffUtil(
    private val oldList: List<CloudFileListItemViewModel>,
    private val newList: List<CloudFileListItemViewModel>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].fileName == newList[newItemPosition].fileName
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
