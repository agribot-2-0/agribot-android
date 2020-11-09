package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.databinding.ListItemWeatherSettingsBinding
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.PickerSymbols
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.PickersMinMaxValues
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.WeatherItemNumber
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.weatherattributes.WeatherAttribute
import kotlinx.android.synthetic.main.list_item_weather_settings.view.activation_switch
import kotlinx.android.synthetic.main.list_item_weather_settings.view.condition
import kotlinx.android.synthetic.main.list_item_weather_settings.view.currentValue
import kotlinx.android.synthetic.main.list_item_weather_settings.view.value
import kotlinx.android.synthetic.main.list_item_weather_settings.view.weather_icon

class WeatherSettingsListItemAdapter(
    val onWeatherAttributeChanged: (weatherAttribute: WeatherAttribute) -> Unit
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

    @SuppressLint("SupportAnnotationUsage")
    override fun onBindViewHolder(holder: WeatherSettingsListItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        @DrawableRes
        val imageSrc: Int = when (item.id) {
            WeatherItemNumber.WIND_ITEM.itemNumber -> {
                R.drawable.ic_wind
            }
            WeatherItemNumber.PRESSURE_ITEM.itemNumber -> {
                R.drawable.ic_pressure
            }
            WeatherItemNumber.HUMIDITY_ITEM.itemNumber -> {
                R.drawable.ic_humidity
            }
            WeatherItemNumber.TEMPERATURE_ITEM.itemNumber -> {
                R.drawable.ic_thermometer
            }

            else -> -1
        }

        holder.itemView.weather_icon.setImageResource(imageSrc)

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
                PickersMinMaxValues.MORE_POSITION.value
            }
        }
        holder.itemView.currentValue.text = item.currentValue
        holder.itemView.condition.minValue = PickersMinMaxValues.LESS_POSITION.value
        holder.itemView.condition.maxValue = PickersMinMaxValues.EQUAL_POSITION.value
        holder.itemView.condition.displayedValues = item.conditionArray
        holder.itemView.condition.value = condition
        var isSwitchActive = item.isSwitchActive
        holder.itemView.activation_switch.isChecked = isSwitchActive

        var itemValue = if (item.settedValue != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
            item.settedValue.toInt()
        } else {
            (item.minimumPickerValue + (item.maximumPickerValue - item.minimumPickerValue) / 2)
        }

        holder.itemView.value.minValue = item.minimumPickerValue
        holder.itemView.value.maxValue = item.maximumPickerValue
        holder.itemView.value.value = itemValue

        holder.itemView.activation_switch.setOnCheckedChangeListener { _, isChecked ->
            isSwitchActive = isChecked
            if (isChecked) {
                onWeatherAttributeChanged(
                    WeatherAttribute(
                        attributeID = item.id,
                        weatherValue = itemValue.toString(),
                        weatherCondition = item.conditionArray[condition],
                        isWeatherItemActive = isSwitchActive
                    )
                )
            } else {
                onWeatherAttributeChanged(
                    WeatherAttribute(
                        attributeID = item.id,
                        weatherValue = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                        weatherCondition = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                        isWeatherItemActive = isSwitchActive
                    )
                )
            }
        }

        holder.itemView.condition.setOnValueChangedListener { _, _, conditionValue ->
            condition = conditionValue
            onWeatherAttributeChanged(
                WeatherAttribute(
                    attributeID = item.id,
                    weatherValue = itemValue.toString(),
                    weatherCondition = item.conditionArray[condition],
                    isWeatherItemActive = isSwitchActive
                )
            )
        }

        holder.itemView.value.setOnValueChangedListener { _, _, newVal ->
            itemValue = newVal
            onWeatherAttributeChanged(
                WeatherAttribute(
                    attributeID = item.id,
                    weatherValue = itemValue.toString(),
                    weatherCondition = item.conditionArray[condition],
                    isWeatherItemActive = isSwitchActive
                )
            )
        }
    }
}
