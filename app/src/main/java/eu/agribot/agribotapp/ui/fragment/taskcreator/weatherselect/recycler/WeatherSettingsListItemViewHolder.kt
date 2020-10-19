package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler

import eu.agribot.agribotapp.base.BaseViewHolder
import eu.agribot.agribotapp.databinding.ListItemWeatherSettingsBinding

class WeatherSettingsListItemViewHolder(viewBinding: ListItemWeatherSettingsBinding) :
    BaseViewHolder<WeatherSettingsListItemViewModel, ListItemWeatherSettingsBinding>(
        viewBinding
    ) {
    override fun bind(viewModel: WeatherSettingsListItemViewModel) {
        viewBinding.viewModel = viewModel
        viewBinding.executePendingBindings()
    }
}
