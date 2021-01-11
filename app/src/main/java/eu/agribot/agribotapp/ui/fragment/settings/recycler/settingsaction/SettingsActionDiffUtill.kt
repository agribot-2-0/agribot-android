package eu.agribot.agribotapp.ui.fragment.settings.recycler.settingsaction

import androidx.recyclerview.widget.DiffUtil

class SettingsActionDiffUtill(
    private val oldList: List<SettingsActionViewModel>,
    private val newList: List<SettingsActionViewModel>
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
