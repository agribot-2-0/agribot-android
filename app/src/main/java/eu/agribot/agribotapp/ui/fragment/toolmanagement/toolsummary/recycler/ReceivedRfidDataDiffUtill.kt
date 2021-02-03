package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolsummary.recycler

import androidx.recyclerview.widget.DiffUtil

class ReceivedRfidDataDiffUtill(
    private val oldList: List<ReceivedRfidDataViewModel>,
    private val newList: List<ReceivedRfidDataViewModel>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].rfidTagData == newList[newItemPosition].rfidTagData
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
