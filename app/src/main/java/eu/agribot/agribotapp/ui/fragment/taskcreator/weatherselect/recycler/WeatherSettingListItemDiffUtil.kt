package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler

import androidx.recyclerview.widget.DiffUtil

class WeatherSettingListItemDiffUtil(
    private val oldList: List<WeatherSettingsListItemViewModel>,
    private val newList: List<WeatherSettingsListItemViewModel>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].currentValue == newList[newItemPosition].currentValue
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}
