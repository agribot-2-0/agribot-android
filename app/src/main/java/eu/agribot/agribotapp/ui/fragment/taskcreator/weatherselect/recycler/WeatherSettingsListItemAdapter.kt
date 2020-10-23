package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.databinding.ListItemWeatherSettingsBinding
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerSymbols
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickersMinMaxValues
import kotlinx.android.synthetic.main.list_item_weather_settings.view.*


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
        holder.itemView.value.minValue = item.minimumPickerValue
        holder.itemView.value.maxValue = item.maximumPickerValue


        holder.itemView.condition.minValue = PickersMinMaxValues.LESS_POSITION.value
        holder.itemView.condition.maxValue = PickersMinMaxValues.EQUAL_POSITION.value
        holder.itemView.condition.displayedValues = item.conditionArray

        when (item.settedCondition) {
            PickerSymbols.CLOUD_DATA_CONDITION_LESS.symbol -> {
                holder.itemView.condition.value = PickersMinMaxValues.LESS_POSITION.value
            }

            PickerSymbols.CLOUD_DATA_CONDITION_MORE.symbol -> {
                holder.itemView.condition.value = PickersMinMaxValues.MORE_POSITION.value
            }

            PickerSymbols.CLOUD_DATA_CONDITION_EQUAL.symbol -> {
                holder.itemView.condition.value = PickersMinMaxValues.EQUAL_POSITION.value
            }
            else -> {
                holder.itemView.condition.value = 1
            }
        }

        if (item.settedValue != -1) {
            holder.itemView.value.value = item.settedValue
        } else {
            holder.itemView.value.value = (item.minimumPickerValue + item.maximumPickerValue) / 2
        }



        holder.itemView.activation_switch.isChecked = item.isSwitchActive

        holder.itemView.condition.setOnValueChangedListener { _, _, value ->
            onConditionPickerValueChanged(item.conditionArray[value], position)
        }

        holder.itemView.value.setOnValueChangedListener { _, _, newVal ->
            onValuePickerValueChanged(newVal, position)
        }

        holder.itemView.activation_switch.setOnCheckedChangeListener { _, isChecked ->
            onItemSwitchParametrChange(
                item.id,
                isChecked
            )
        }
    }
}
