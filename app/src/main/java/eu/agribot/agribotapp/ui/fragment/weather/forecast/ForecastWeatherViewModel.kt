package eu.agribot.agribotapp.ui.fragment.weather.forecast

import eu.agribot.agribotapp.base.BaseViewModel

data class ForecastWeatherViewModel(
    val timestamp: Int,
    val date: String,
    val temperature: String,
    val imageNumber: String,
    val humidity: String,
    val wind: String
) : BaseViewModel()
