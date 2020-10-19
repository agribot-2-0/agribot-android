package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskToolsNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.FirstRobotWeatherStationHumidity
import eu.agribot.agribotapp.domain.model.FirstRobotWeatherStationTemperature
import eu.agribot.agribotapp.domain.model.FirstRobotWeatherStationWind
import eu.agribot.agribotapp.domain.model.cdb.FirstRobotWeatherStationPressure
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.WeatherSettingsListItemViewModel

class CreatorWeatherSelectViewModel(
    private val cloudDatabaseService: CloudDatabaseService,
    private val taskCreatorDataManager: TaskCreatorDataManager,
    private val resourcesProvider: ResourcesProvider
) :
    BaseViewModel() {
    val weatherAttributeList: MutableLiveData<List<WeatherSettingsListItemViewModel>> =
        MutableLiveData(
            listOf()
        )
    private val actualTemperature: MutableLiveData<String> = MutableLiveData()
    private val actualHumidity: MutableLiveData<String> = MutableLiveData()
    private val actualWind: MutableLiveData<String> = MutableLiveData()
    private val actualPressure: MutableLiveData<String> = MutableLiveData()
    val backButtonEvent: MutableLiveEvent<Boolean> = MutableLiveEvent()


    fun onConditionChanged(condition: String, itemPosition: Int) {
        Log.e("condition", "condition: " + condition + "position: " + itemPosition)
    }

    fun onValueChanged(value: Int, itemPosition: Int) {
        Log.e("value", "value: " + value + "position: " + itemPosition)

    }

    fun onSwitchParametrChange(position: Int, isActivated: Boolean) {
        Log.e("switch", "position: " + position + "activate: " + isActivated)

    }


    private fun addToWeatherAttributeList(
        @DrawableRes weatherAttributeIcon: Int,
        conditionArray: Array<String>,
        minimumPickerValue: Int,
        maximumPickerValue: Int,
        currentValue: String
    ) {
        weatherAttributeList.value = listOf(
            WeatherSettingsListItemViewModel(
                weatherAttributeIcon,
                conditionArray,
                minimumPickerValue,
                maximumPickerValue,
                currentValue
            ),
            *weatherAttributeList.value!!.toTypedArray()
        )
    }

    fun createWeatherParametersList() {
        val iconResId = listOf(
            R.drawable.ic_celsius,
            R.drawable.ic_humidity,
            R.drawable.ic_pressure,
            R.drawable.ic_wind
        )
        val conditionSymbols = arrayOf("<", ">", "=")
        val minimumPickerValue = listOf(
            PickersMinMaxValues.MIN_TEMP_VALUE.value,
            PickersMinMaxValues.MIN_HUMIDITY_VALUE.value,
            PickersMinMaxValues.MIN_PRESSURE_VALUE.value, PickersMinMaxValues.MIN_WIND_VALUE.value
        )
        val maximumPickerValue = listOf(
            PickersMinMaxValues.MAX_TEMP_VALUE.value,
            PickersMinMaxValues.MAX_HUMIDITY_VALUE.value,
            PickersMinMaxValues.MAX_PRESSURE_VALUE.value,
            PickersMinMaxValues.MAX_WIND_VALUE.value
        )

        val currentValue = listOf(
            actualTemperature.value.toString(),
            actualHumidity.value.toString(),
            actualPressure.value.toString(),
            actualWind.value.toString()
        )
        for (itemPosition in 0..3) {
            addToWeatherAttributeList(
                iconResId[itemPosition],
                conditionSymbols,
                minimumPickerValue[itemPosition],
                maximumPickerValue[itemPosition],
                currentValue[itemPosition]
            )
        }
    }

    fun onPreviousButtonClicked() {
        backButtonEvent.postValue(true)
    }

    fun getRobotDataValue() {
        getFirstRobotTemperature()
        getFirstRobotHumidity()
        getFirstRobotPressure()
        getFirstRobotWind()
        createWeatherParametersList()
    }

    private fun getFirstRobotHumidity() {
        cloudDatabaseService.getFirstRobotWeatherStationHumidity(
            this::onFirstRobotHumiditySucess,
            this::onSampleDataError
        )
    }

    private fun getFirstRobotTemperature() {
        cloudDatabaseService.getFirstRobotWeatherStationTemperature(
            this::onFirstRobotTemperatureSucess,
            this::onSampleDataError
        )
    }

    private fun getFirstRobotWind() {
        cloudDatabaseService.getFirstRobotWeatherStationWind(
            this::onFirstRobotWindSucess,
            this::onSampleDataError
        )
    }

    private fun getFirstRobotPressure() {
        cloudDatabaseService.getFirstRobotWeatherStationPressure(
            this::onFirstRobotPressureSucess,
            this::onSampleDataError
        )
    }

    private fun onSampleDataError(sampleString: String) {
    }

    private fun onFirstRobotHumiditySucess(firstRobotWeatherStationHumidity: FirstRobotWeatherStationHumidity) {
        actualHumidity.value = firstRobotWeatherStationHumidity.value.toString() + "%"
    }

    private fun onFirstRobotTemperatureSucess(firstRobotWeatherStationTemperature: FirstRobotWeatherStationTemperature) {
        Log.e("xd", firstRobotWeatherStationTemperature.temperature.toString())
        actualTemperature.value =
            firstRobotWeatherStationTemperature.temperature.toString() + " °C"
    }

    private fun onFirstRobotWindSucess(firstRobotWeatherStationWind: FirstRobotWeatherStationWind) {
        actualWind.value = firstRobotWeatherStationWind.speed.toString() + " km/h"
    }

    private fun onFirstRobotPressureSucess(firstRobotWeatherStationPressure: FirstRobotWeatherStationPressure) {
        actualPressure.value = firstRobotWeatherStationPressure.value.toString() + " hPa"
    }


    fun onNextButtonClicked() {
        navigationEvent.postValue(
            CreatorTaskToolsNavigationEvent()
        )
    }
}
