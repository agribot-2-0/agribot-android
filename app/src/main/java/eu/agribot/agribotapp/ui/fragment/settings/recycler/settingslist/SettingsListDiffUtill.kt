package eu.agribot.agribotapp.ui.fragment.settings.recycler.settingslist

import androidx.recyclerview.widget.DiffUtil

class SettingsListDiffUtill(
    private val oldList: List<SettingsListViewModel>,
    private val newList: List<SettingsListViewModel>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].settingID == newList[newItemPosition].settingID
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
