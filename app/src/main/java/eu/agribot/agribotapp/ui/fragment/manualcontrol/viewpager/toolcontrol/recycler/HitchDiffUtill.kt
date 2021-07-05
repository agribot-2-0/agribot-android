package eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.recycler

import androidx.recyclerview.widget.DiffUtil

class HitchDiffUtill(
    private val oldList: List<HitchListItemViewModel>,
    private val newList: List<HitchListItemViewModel>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].toolName == newList[newItemPosition].toolName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
