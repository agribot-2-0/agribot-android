package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.communication.restapi.RestApiService
import eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.taskdetails.TimePatterns
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.WeatherUnit
import eu.agribot.agribotapp.ui.fragment.weather.forecast.ForecastWeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

class WeatherService(
    private val restApiService: RestApiService,
    private val resourcesProvider: ResourcesProvider
) {

    fun getCurrentData(onGotForecastWeatherData: (forecastList: List<ForecastWeatherViewModel>) -> Unit) {
        restApiService.getCurrentData(onGotWeatherResponse = { weatherItem ->
            weatherItem.hourly?.let {
                onGotForecastWeatherData(it.map { weatherParam ->
                    ForecastWeatherViewModel(
                        timestamp = weatherParam.dt,
                        date = getDateFromTimestamp(weatherParam.dt)!!,
                        temperature = String.format(
                            resourcesProvider.getString(R.string.celcius_degree_value),
                            weatherParam.temp.toString()
                        ),
                        imageNumber = weatherParam.weather?.get(0)?.icon!!,
                        humidity = String.format(
                            resourcesProvider.getString(R.string.humidity_percent_value),
                            weatherParam.humidity, WeatherUnit.HUMIDITY_UNIT.unit
                        ),
                        wind = String.format(
                            resourcesProvider.getString(R.string.wind_value),
                            weatherParam.windSpeed.toString()
                        )
                    )
                })
            }
        })
    }

    private fun getDateFromTimestamp(s: Int): String? {
        return try {
            val sdf = SimpleDateFormat(TimePatterns.DATE_TIME.pattern)
            val netDate = Date(s * 1000L)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }


}