package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.databinding.ListItemWeatherSettingsBinding
import kotlinx.android.synthetic.main.list_item_weather_settings.view.activation_switch
import kotlinx.android.synthetic.main.list_item_weather_settings.view.condition
import kotlinx.android.synthetic.main.list_item_weather_settings.view.current_value
import kotlinx.android.synthetic.main.list_item_weather_settings.view.value
import kotlinx.android.synthetic.main.list_item_weather_settings.view.weather_icon

class WeatherSettingsListItemAdapter(
    val onConditionPickerValueChanged: (condition: String, itemPosition: Int) -> Unit,
    val onValuePickerValueChanged: (value: Int, itemPosition: Int) -> Unit,
    val onItemSwitchParametrChange: (position: Int, isActivated: Boolean) -> Unit
) : RecyclerView.Adapter<WeatherSettingsListItemViewHolder>() {

    var items = listOf<WeatherSettingsListItemViewModel>()
        set(value) {
            DiffUtil.calculateDiff(
                WeatherSettingListItemDiffUtil(
                    items,
                    value
                )
            ).also {
                field = value
            }.dispatchUpdatesTo(this)
        }
    var isItemActive = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherSettingsListItemViewHolder =
        WeatherSettingsListItemViewHolder(
            ListItemWeatherSettingsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: WeatherSettingsListItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.weather_icon.setImageResource(item.weatherAttributeIcon)
        holder.itemView.value.minValue = item.minimumPickerValue
        holder.itemView.value.maxValue = item.maximumPickerValue
        holder.itemView.current_value.text = item.currentValue
        holder.itemView.condition.minValue = 0
        holder.itemView.condition.value = 1
        holder.itemView.condition.maxValue = 2
        holder.itemView.condition.displayedValues = item.conditionArray

        holder.itemView.condition.setOnValueChangedListener { _, _, value ->
            onConditionPickerValueChanged(item.conditionArray[value], position)
        }

        holder.itemView.value.setOnValueChangedListener { _, _, newVal ->
            onValuePickerValueChanged(newVal, position)
        }

        holder.itemView.activation_switch.setOnCheckedChangeListener { _, isChecked ->
            isItemActive = isChecked
            onItemSwitchParametrChange(
                position,
                isItemActive
            )
        }
    }
}