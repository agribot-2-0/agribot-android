package eu.agribot.agribotapp.ui.fragment.weather.forecast

import eu.agribot.agribotapp.base.BaseViewHolder
import eu.agribot.agribotapp.databinding.ListItemWeatherForecastBinding


class ForecastWeatherViewHolder(viewBinding: ListItemWeatherForecastBinding) :
    BaseViewHolder<ForecastWeatherViewModel, ListItemWeatherForecastBinding>(
        viewBinding
    ) {
    override fun bind(viewModel: ForecastWeatherViewModel) {
        viewBinding.viewModel = viewModel
        viewBinding.executePendingBindings()
    }
}


