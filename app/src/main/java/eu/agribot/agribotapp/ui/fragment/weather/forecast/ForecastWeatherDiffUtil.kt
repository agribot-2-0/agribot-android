package eu.agribot.agribotapp.ui.fragment.weather.forecast

import androidx.recyclerview.widget.DiffUtil

class ForecastWeatherDiffUtil(
    private val oldList: List<ForecastWeatherViewModel>,
    private val newList: List<ForecastWeatherViewModel>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].date == newList[newItemPosition].date
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
