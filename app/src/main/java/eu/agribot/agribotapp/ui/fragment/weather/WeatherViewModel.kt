package eu.agribot.agribotapp.ui.fragment.weather

import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.Entry
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationHumidity
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationPressure
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationTemperature
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationWind
import eu.agribot.agribotapp.domain.service.WeatherService
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateViewModel
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.items.RobotStateItems
import eu.agribot.agribotapp.ui.fragment.taskcreator.TaskDetailsElements
import eu.agribot.agribotapp.ui.fragment.weather.forecast.ForecastWeatherViewModel

class WeatherViewModel(
    private val cloudDatabaseService: CloudDatabaseService,
    private val weatherService: WeatherService,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {
    val currentTemperature: MutableLiveData<String> = MutableLiveData()
    val currentWind: MutableLiveData<String> = MutableLiveData()
    val currentHumidity: MutableLiveData<String> = MutableLiveData()
    val currentWeatherImage: MutableLiveData<String> = MutableLiveData()
    val temperatureValuesChartList: MutableLiveData<List<Entry>> = MutableLiveData(listOf())
    val isChartValuesReady : MutableLiveEvent<Boolean> = MutableLiveEvent()

    val weatherState: MutableLiveData<List<RobotStateViewModel>> = MutableLiveData(listOf())
    val toastEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val forecastWeatherItems: MutableLiveData<List<ForecastWeatherViewModel>> =
        MutableLiveData(listOf())

    fun prepareWeatherStationData() {
        setEmptyWeatherList()
        getRobotTemperature()
        getRobotHumidity()
        getRobotWind()
        getRobotPressure()
    }

    private fun setEmptyWeatherList() {
        weatherState.value = listOf(
            RobotStateViewModel(
                RobotStateItems.TEMPERATURE.ID,
                DEFAULT_WEATHER_VALUE,
                TaskDetailsElements.TEMPERATURE_UNIT.detailsElement
            ),

            RobotStateViewModel(
                RobotStateItems.HUMIDITY.ID,
                DEFAULT_WEATHER_VALUE,
                TaskDetailsElements.PERCENT.detailsElement
            ),

            RobotStateViewModel(
                RobotStateItems.WIND.ID,
                DEFAULT_WEATHER_VALUE,
                TaskDetailsElements.SPEED_UNIT.detailsElement
            ),

            RobotStateViewModel(
                RobotStateItems.PRESSURE.ID,
                DEFAULT_WEATHER_VALUE,
                TaskDetailsElements.PRESSURE_UNIT.detailsElement
            )

        )
    }

    private fun getRobotHumidity() {
        cloudDatabaseService.getRobotWeatherStationHumidity(
            this::onGetRobotHumiditySucess,
            this::postErrorMessage
        )
    }

    private fun getRobotTemperature() {
        cloudDatabaseService.getRobotWeatherStationTemperature(
            this::onGetRobotTemperatureSucess,
            this::postErrorMessage
        )
    }

    private fun getRobotWind() {
        cloudDatabaseService.getRobotWeatherStationWind(
            this::onGetRobotWindSucess,
            this::postErrorMessage
        )
    }

    private fun getRobotPressure() {
        cloudDatabaseService.getRobotWeatherStationPressure(
            this::onGetRobotPressureSucess,
            this::postErrorMessage
        )
    }

    private fun onGetRobotHumiditySucess(robotWeatherStationHumidity: RobotWeatherStationHumidity) {
        addParameterToRobotDataStateList(
            RobotStateViewModel(
                RobotStateItems.HUMIDITY.ID,
                approximateParam(
                    robotWeatherStationHumidity.value,
                    robotWeatherStationHumidity.accuracy
                ),
                TaskDetailsElements.PERCENT.detailsElement

            )
        )
    }

    private fun onGetRobotTemperatureSucess(robotWeatherStationTemperature: RobotWeatherStationTemperature) {
        addParameterToRobotDataStateList(
            RobotStateViewModel(
                RobotStateItems.TEMPERATURE.ID,
                approximateParam(
                    robotWeatherStationTemperature.value,
                    robotWeatherStationTemperature.accuracy
                ),
                TaskDetailsElements.TEMPERATURE_UNIT.detailsElement

            )
        )
    }

    private fun onGetRobotWindSucess(robotWeatherStationWind: RobotWeatherStationWind) {
        addParameterToRobotDataStateList(
            RobotStateViewModel(
                RobotStateItems.WIND.ID,
                approximateParam(
                    robotWeatherStationWind.speed_value,
                    robotWeatherStationWind.speed_accuracy
                ),
                TaskDetailsElements.SPEED_UNIT.detailsElement
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

    private fun onGetRobotPressureSucess(robotWeatherStationPressure: RobotWeatherStationPressure) {
        addParameterToRobotDataStateList(
            RobotStateViewModel(
                RobotStateItems.PRESSURE.ID,
                approximateParam(
                    robotWeatherStationPressure.value,
                    robotWeatherStationPressure.accuracy
                ),
                TaskDetailsElements.PRESSURE_UNIT.detailsElement
            )
        )
    }

    private fun postErrorMessage(errorText: String) {
        toastEvent.postValue(errorText)
    }

    private fun addParameterToRobotDataStateList(robotStateParameter: RobotStateViewModel) {
        val isAttributeAlreadyAdded = weatherState.value!!.any { arrtibute ->
            arrtibute.stateItemId == robotStateParameter.stateItemId
        }
        if (!isAttributeAlreadyAdded)
            weatherState.value = listOf(
                RobotStateViewModel(
                    robotStateParameter.stateItemId,
                    robotStateParameter.stateValue,
                    robotStateParameter.stateUnit
                ),
                *weatherState.value!!.toTypedArray()
            )
        else {
            weatherState.value = weatherState.value!!.map { oldAttribute ->
                if (oldAttribute.stateItemId == robotStateParameter.stateItemId) {
                    RobotStateViewModel(
                        oldAttribute.stateItemId,
                        robotStateParameter.stateValue,
                        oldAttribute.stateUnit
                    )
                } else {
                    oldAttribute
                }
            }
        }
    }

    fun getData() {
        weatherService.getCurrentData(onGotForecastWeatherData = this::addForecastItemsToList)
    }

    private fun addForecastItemsToList(items: List<ForecastWeatherViewModel>) {
        forecastWeatherItems.value = items.slice(1 until items.size)
        currentTemperature.value = items[CURRENT_FORCAST_WEATHER_INDEX].temperature
        currentHumidity.value = items[CURRENT_FORCAST_WEATHER_INDEX].humidity
        currentWind.value = items[CURRENT_FORCAST_WEATHER_INDEX].wind
        currentWeatherImage.value = items[CURRENT_FORCAST_WEATHER_INDEX].imageNumber
            temperatureValuesChartList.value =
                items.map { Entry(it.timestamp.toFloat(), chainToDigit(it.temperature)) }
        isChartValuesReady.postValue(true)
    }

    private fun chainToDigit(data: String): Float {
        return try {
            data.toFloat()
        } catch (exception: NumberFormatException) {
            return 0F
        }
    }

    companion object {
        const val DEFAULT_WEATHER_VALUE = "*"
        const val CURRENT_FORCAST_WEATHER_INDEX = 0
    }
}
