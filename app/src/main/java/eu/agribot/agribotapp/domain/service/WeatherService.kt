package eu.agribot.agribotapp.domain.service

import android.annotation.SuppressLint
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.communication.crashlystic.CrashlysticService
import eu.agribot.agribotapp.communication.restapi.RestApiService
import eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.taskdetails.TimePatterns
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.WeatherUnit
import eu.agribot.agribotapp.ui.fragment.weather.forecast.ForecastWeatherViewModel
import eu.agribot.agribotapp.ui.fragment.weather.forecast.chart.ChartWeatherData
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class WeatherService(
    private val restApiService: RestApiService,
    private val resourcesProvider: ResourcesProvider,
    private val crashlysticService: CrashlysticService
) {

    fun getForecastData(
        onGotForecastWeatherData: (forecastList: List<ForecastWeatherViewModel>) -> Unit,
        onGotChartValuesData: (List<ChartWeatherData>) -> Unit
    ) {
        if (isWeatherActually()) {
            restApiService.getOpenWeatherDataFromPersistent()
                ?.hourly?.let {
                    onGotChartValuesData(it.map {
                        ChartWeatherData(
                            timestamp = it.dt.toFloat(),
                            temperatureValue = it.temp.toFloat()
                        )
                    })
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
        } else {
            restApiService.getOpenWeatherDataFromApi(onGotWeatherResponse = { weatherItem ->
                weatherItem.hourly?.let {
                    onGotChartValuesData(it.map {
                        ChartWeatherData(
                            timestamp = it.dt.toFloat(),
                            temperatureValue = it.temp.toFloat()
                        )
                    })
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
    }

    private fun isWeatherActually(): Boolean {
        return if (restApiService.getOpenWeatherDataFromPersistent() != null) {
            val weatherDataCalendarDate = Calendar.getInstance(Locale.ENGLISH)
            weatherDataCalendarDate.timeInMillis =
                restApiService.getOpenWeatherDataFromPersistent()?.hourly!![THE_OLDEST_WEATHER_FORECAST_ELEMENT_POSITION].dt.toLong() * TIMESTAMP_CONV_VALUE
            val currentCalendarDate = Calendar.getInstance(Locale.ENGLISH)
            currentCalendarDate.timeInMillis = Timestamp(System.currentTimeMillis()).time
            weatherDataCalendarDate.add(Calendar.HOUR, TWO_HOURS)
            return weatherDataCalendarDate.after(currentCalendarDate)
        } else false
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateFromTimestamp(s: Int): String? {
        return try {
            val sdf = SimpleDateFormat(TimePatterns.DATE_TIME.pattern)
            val netDate = Date(s * TIMESTAMP_CONV_VALUE)
            sdf.format(netDate)
        } catch (e: Exception) {
            crashlysticService.recordException(
                CrashlysticService.RegisterExceptionModel(
                    "Convert Date Exception ",
                    e
                )
            )
            e.toString()
        }
    }

    companion object {
        const val TWO_HOURS = 2

        const val TIMESTAMP_CONV_VALUE = 1000L

        const val THE_OLDEST_WEATHER_FORECAST_ELEMENT_POSITION = 0
    }
}
