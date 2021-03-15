package eu.agribot.agribotapp.ui.fragment.weather

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationHumidity
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationPressure
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationTemperature
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationWind
import eu.agribot.agribotapp.domain.service.WeatherService
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.WeatherUnit
import eu.agribot.agribotapp.ui.fragment.weather.forecast.chart.ChartWeatherData
import eu.agribot.agribotapp.ui.fragment.weather.forecast.ForecastWeatherViewModel

class WeatherViewModel(
    private val cloudDatabaseService: CloudDatabaseService,
    private val weatherService: WeatherService,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {
    val robotWeatherStationTemperature: MutableLiveData<String> = MutableLiveData()
    val robotWeatherStationWind: MutableLiveData<String> = MutableLiveData()
    val robotWeatherStationHumidity: MutableLiveData<String> = MutableLiveData()
    val robotWeatherStationPressure: MutableLiveData<String> = MutableLiveData()
    val chartWeatherData: MutableLiveData<List<ChartWeatherData>> = MutableLiveData(listOf())
    val currentWeatherImage: MutableLiveData<String> = MutableLiveData()

    val toastEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val forecastWeatherItems: MutableLiveData<List<ForecastWeatherViewModel>> =
        MutableLiveData(listOf())

    fun prepareWeatherStationData() {
        getRobotTemperature()
        getRobotHumidity()
        getRobotWind()
        getRobotPressure()
    }

    private fun getRobotHumidity() {
        cloudDatabaseService.getRobotWeatherStationHumidity(
            this::onGotRobotHumiditySucess,
            this::postErrorMessage
        )
    }

    private fun getRobotTemperature() {
        cloudDatabaseService.getRobotWeatherStationTemperature(
            this::onGotRobotTemperatureSucess,
            this::postErrorMessage
        )
    }

    private fun getRobotWind() {
        cloudDatabaseService.getRobotWeatherStationWind(
            this::onGotRobotWindSucess,
            this::postErrorMessage
        )
    }

    private fun getRobotPressure() {
        cloudDatabaseService.getRobotWeatherStationPressure(
            this::onGotRobotPressureSucess,
            this::postErrorMessage
        )
    }

    private fun onGotRobotHumiditySucess(robotWeatherStationHumidity: RobotWeatherStationHumidity) {
        this.robotWeatherStationHumidity.value = String.format(
            resourcesProvider.getString(R.string.humidity_percent_value),
            approximateParam(
                robotWeatherStationHumidity.value,
                robotWeatherStationHumidity.accuracy
            ), WeatherUnit.HUMIDITY_UNIT.unit
        )
    }

    private fun onGotRobotTemperatureSucess(robotWeatherStationTemperature: RobotWeatherStationTemperature) {
        this.robotWeatherStationTemperature.value =
            robotWeatherStationTemperature.value.toString() + WeatherUnit.TEMPERATURE_UNIT.unit
    }

    private fun onGotRobotWindSucess(robotWeatherStationWind: RobotWeatherStationWind) {
        this.robotWeatherStationWind.value = String.format(
            resourcesProvider.getString(R.string.wind_value),
            approximateParam(
                robotWeatherStationWind.speedValue,
                robotWeatherStationWind.speedAccuracy
            )
        )
    }

    private fun approximateParam(value: Double, accuracy: Double): String {
        return if (accuracy > 0.1 && accuracy < 1) {
            String.format("%.1f", value)
        } else if (accuracy > 0 && accuracy < 0.1) {
            String.format("%.2f", value)
        } else value.toInt().toString()
    }

    private fun onGotRobotPressureSucess(robotWeatherStationPressure: RobotWeatherStationPressure) {
        this.robotWeatherStationPressure.value = String.format(
            resourcesProvider.getString(R.string.pressure_value),
            approximateParam(
                robotWeatherStationPressure.value,
                robotWeatherStationPressure.accuracy
            )
        )
    }

    private fun postErrorMessage(errorText: String) {
        toastEvent.postValue(errorText)
    }

    fun getForecastData() {
        weatherService.getForecastData(onGotForecastWeatherData = this::addForecastItemsToList, onGotChartValuesData = this :: setChartData)
    }

    private fun setChartData(items: List<ChartWeatherData>) {
        chartWeatherData.value = items
    }

    private fun addForecastItemsToList(items: List<ForecastWeatherViewModel>) {
        forecastWeatherItems.value = items.slice(1 until items.size)
        currentWeatherImage.value = items[CURRENT_FORCAST_WEATHER_INDEX].imageNumber
    }

    companion object {
        const val CURRENT_FORCAST_WEATHER_INDEX = 0
    }
}
