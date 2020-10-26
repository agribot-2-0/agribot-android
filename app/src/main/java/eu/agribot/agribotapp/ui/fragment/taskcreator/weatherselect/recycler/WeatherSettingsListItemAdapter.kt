package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.databinding.ListItemWeatherSettingsBinding
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerSymbols
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickersMinMaxValues
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.weatherattributes.WeatherAttribute
import kotlinx.android.synthetic.main.list_item_weather_settings.view.*

class WeatherSettingsListItemAdapter(
    val onWeatherAttributeChanged: (weatherAttribute: WeatherAttribute) -> Unit
) : RecyclerView.Adapter<WeatherSettingsListItemViewHolder>() {
    val errorValue = -1
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

    fun updateList(newList: List<WeatherSettingsListItemViewModel>) {
        val diffResult = DiffUtil.calculateDiff(WeatherSettingListItemDiffUtil(this.items, newList))
        diffResult.dispatchUpdatesTo(this)
    }

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
        var condition = when (item.settedCondition) {
            PickerSymbols.CLOUD_DATA_CONDITION_LESS.symbol -> {
                PickersMinMaxValues.LESS_POSITION.value
            }

            PickerSymbols.CLOUD_DATA_CONDITION_MORE.symbol -> {
                PickersMinMaxValues.MORE_POSITION.value
            }

            PickerSymbols.CLOUD_DATA_CONDITION_EQUAL.symbol -> {
                PickersMinMaxValues.EQUAL_POSITION.value
            }
            else -> {
                errorValue
            }
        }
        holder.itemView.condition.minValue = PickersMinMaxValues.LESS_POSITION.value
        holder.itemView.condition.maxValue = PickersMinMaxValues.EQUAL_POSITION.value
        holder.itemView.condition.displayedValues = item.conditionArray
        holder.itemView.condition.value = condition

        var isSwitchActive = item.isSwitchActive
        holder.itemView.activation_switch.isChecked = isSwitchActive

        var itemValue = if (item.settedValue !=errorValue) {
            holder.itemView.value.value
        } else {
            (item.minimumPickerValue + (item.maximumPickerValue - item.minimumPickerValue) / 2)
        }
        holder.itemView.value.minValue = item.minimumPickerValue
        holder.itemView.value.maxValue = item.maximumPickerValue
        holder.itemView.value.value = itemValue

        holder.itemView.activation_switch.setOnCheckedChangeListener { _, isChecked ->
            isSwitchActive = isChecked
            onWeatherAttributeChanged(
                WeatherAttribute(
                    attributeID = item.id,
                    weatherValue = itemValue,
                    weatherCondition = item.conditionArray[condition],
                    isWeatherActive = isChecked
                )
            )
        }

        holder.itemView.condition.setOnValueChangedListener { _, _, conditionValue ->
            condition = conditionValue
            onWeatherAttributeChanged(
                WeatherAttribute(
                    attributeID = item.id,
                    weatherValue = itemValue,
                    weatherCondition = item.conditionArray[condition],
                    isWeatherActive = isSwitchActive
                )
            )
        }

        holder.itemView.value.setOnValueChangedListener { _, _, newVal ->
            itemValue = newVal
            onWeatherAttributeChanged(
                WeatherAttribute(
                    attributeID = item.id,
                    weatherValue = itemValue,
                    weatherCondition = item.conditionArray[condition],
                    isWeatherActive = isSwitchActive
                )
            )
        }
    }
}
