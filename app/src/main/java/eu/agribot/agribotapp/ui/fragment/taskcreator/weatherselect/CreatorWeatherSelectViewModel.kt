package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskToolsNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.FirstRobotWeatherStationHumidity
import eu.agribot.agribotapp.domain.model.WeatherSelectDataModel
import eu.agribot.agribotapp.domain.model.cdb.FirstRobotWeatherStationPressure
import eu.agribot.agribotapp.domain.model.cdb.FirstRobotWeatherStationTemperature
import eu.agribot.agribotapp.domain.model.cdb.FirstRobotWeatherStationWind
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
    private val temperatureItemActive: MutableLiveData<Boolean> = MutableLiveData()
    private val windItemActive: MutableLiveData<Boolean> = MutableLiveData()
    private val humidityItemActive: MutableLiveData<Boolean> = MutableLiveData()
    private val pressureItemActive: MutableLiveData<Boolean> = MutableLiveData()

    val actualTemperature: MutableLiveData<String> = MutableLiveData()
    val actualHumidity: MutableLiveData<String> = MutableLiveData()
    val actualWind: MutableLiveData<String> = MutableLiveData()
    val actualPressure: MutableLiveData<String> = MutableLiveData()
    val backButtonEvent: MutableLiveEvent<Boolean> = MutableLiveEvent()

    fun onConditionChanged(condition: String, itemPosition: Int) {
        when (itemPosition) {
            WeatherItemNumber.WIND_ITEM.itemNumber -> {
                when (condition) {
                    PickerSymbols.PICKER_CONDITION_BIGGER.symbol -> {
                        taskCreatorDataManager.onUpdatewindCondition(PickerSymbols.CLOUD_DATA_CONDITION_MORE.symbol)
                    }
                    PickerSymbols.PICKER_CONDITION_LESS.symbol -> {
                        taskCreatorDataManager.onUpdatewindCondition(PickerSymbols.CLOUD_DATA_CONDITION_LESS.symbol)
                    }
                    PickerSymbols.PICKER_CONDITION_EQUAL.symbol -> {
                        taskCreatorDataManager.onUpdatewindCondition(PickerSymbols.CLOUD_DATA_CONDITION_EQUAL.symbol)
                    }
                }
            }
            WeatherItemNumber.PRESSURE_ITEM.itemNumber -> {
                when (condition) {
                    PickerSymbols.PICKER_CONDITION_BIGGER.symbol -> {
                        taskCreatorDataManager.onUpdatepressureCondition(PickerSymbols.CLOUD_DATA_CONDITION_MORE.symbol)
                    }
                    PickerSymbols.PICKER_CONDITION_LESS.symbol -> {
                        taskCreatorDataManager.onUpdatepressureCondition(PickerSymbols.CLOUD_DATA_CONDITION_LESS.symbol)
                    }
                    PickerSymbols.PICKER_CONDITION_EQUAL.symbol -> {
                        taskCreatorDataManager.onUpdatepressureCondition(PickerSymbols.CLOUD_DATA_CONDITION_EQUAL.symbol)
                    }
                }
            }
            WeatherItemNumber.HUMIDITY_ITEM.itemNumber -> {
                when (condition) {
                    PickerSymbols.PICKER_CONDITION_BIGGER.symbol -> {
                        taskCreatorDataManager.onUpdatehumidityCondition(PickerSymbols.CLOUD_DATA_CONDITION_MORE.symbol)
                    }
                    PickerSymbols.PICKER_CONDITION_LESS.symbol -> {
                        taskCreatorDataManager.onUpdatehumidityCondition(PickerSymbols.CLOUD_DATA_CONDITION_LESS.symbol)
                    }
                    PickerSymbols.PICKER_CONDITION_EQUAL.symbol -> {
                        taskCreatorDataManager.onUpdatehumidityCondition(PickerSymbols.CLOUD_DATA_CONDITION_EQUAL.symbol)
                    }
                }
            }
            WeatherItemNumber.TEMPERATURE_ITEM.itemNumber -> {
                when (condition) {
                    PickerSymbols.PICKER_CONDITION_BIGGER.symbol -> {
                        taskCreatorDataManager.onUpdateTemperatureCondition(PickerSymbols.CLOUD_DATA_CONDITION_MORE.symbol)
                    }
                    PickerSymbols.PICKER_CONDITION_LESS.symbol -> {
                        taskCreatorDataManager.onUpdateTemperatureCondition(PickerSymbols.CLOUD_DATA_CONDITION_LESS.symbol)
                    }
                    PickerSymbols.PICKER_CONDITION_EQUAL.symbol -> {
                        taskCreatorDataManager.onUpdateTemperatureCondition(PickerSymbols.CLOUD_DATA_CONDITION_EQUAL.symbol)
                    }
                }
            }
        }
    }

    fun onValueChanged(value: Int, itemPosition: Int) {
        when (itemPosition) {
            WeatherItemNumber.WIND_ITEM.itemNumber -> {
                if (windItemActive.value == true) {
                    taskCreatorDataManager.onUpdatewindValue(value.toString())
                } else {
                    taskCreatorDataManager.onUpdatewindValue(PickerSymbols.PICKER_DEFAULT_VALUE.symbol)
                }
            }
            WeatherItemNumber.PRESSURE_ITEM.itemNumber -> {
                if (pressureItemActive.value == true) {
                    taskCreatorDataManager.onUpdatewindValue(value.toString())
                    taskCreatorDataManager.onUpdatepressureValue(value.toString())
                } else {
                    taskCreatorDataManager.onUpdatepressureValue(PickerSymbols.PICKER_DEFAULT_VALUE.symbol)
                }
            }
            WeatherItemNumber.HUMIDITY_ITEM.itemNumber -> {
                if (humidityItemActive.value == true) {
                    taskCreatorDataManager.onUpdatewindValue(value.toString())
                    taskCreatorDataManager.onUpdatehumidityValue(value.toString())
                } else {
                    taskCreatorDataManager.onUpdatewindValue(PickerSymbols.PICKER_DEFAULT_VALUE.symbol)
                }
            }
            WeatherItemNumber.TEMPERATURE_ITEM.itemNumber -> {
                if (temperatureItemActive.value == true) {
                    taskCreatorDataManager.onUpdateTemperatureValue(value.toString())
                } else {
                    taskCreatorDataManager.onUpdateTemperatureValue(PickerSymbols.PICKER_DEFAULT_VALUE.symbol)
                }
            }
        }
    }

    fun onSwitchParametrChange(position: Int, isActivated: Boolean) {
        when (position) {
            WeatherItemNumber.WIND_ITEM.itemNumber -> {
                windItemActive.value = isActivated
            }
            WeatherItemNumber.PRESSURE_ITEM.itemNumber -> {
                pressureItemActive.value = isActivated
            }
            WeatherItemNumber.HUMIDITY_ITEM.itemNumber -> {
                humidityItemActive.value = isActivated
            }
            WeatherItemNumber.TEMPERATURE_ITEM.itemNumber -> {
                temperatureItemActive.value = isActivated
            }
        }
    }

    private fun addToWeatherAttributeList(
        weatherSelectDataModel: WeatherSelectDataModel
    ) {
        val isWeatherAttributeAlreadyAdded = weatherAttributeList.value!!.any {
            it.weatherAttributeIcon == weatherSelectDataModel.weatherAttributeIcon
        }
        if (!isWeatherAttributeAlreadyAdded) {
            weatherAttributeList.value = listOf(
                WeatherSettingsListItemViewModel(
                    weatherSelectDataModel.weatherAttributeIcon,
                    weatherSelectDataModel.conditionArray,
                    weatherSelectDataModel.minimumPickerValue,
                    weatherSelectDataModel.maximumPickerValue,
                    weatherSelectDataModel.currentValue,
                ),
                *weatherAttributeList.value!!.toTypedArray()
            )
        }
    }

    private fun createWeatherParametersList() {
        val conditionSymbols = arrayOf("<", ">", "=")

        val temperatureModel = WeatherSelectDataModel(
            conditionArray = conditionSymbols,
            weatherAttributeIcon = R.drawable.ic_celsius,
            currentValue = actualTemperature.value.toString(),
            minimumPickerValue = PickersMinMaxValues.MIN_TEMP_VALUE.value,
            maximumPickerValue = PickersMinMaxValues.MAX_TEMP_VALUE.value
        )

        val windModel = WeatherSelectDataModel(
            conditionArray = conditionSymbols,
            weatherAttributeIcon = R.drawable.ic_wind,
            currentValue = actualWind.value.toString(),
            minimumPickerValue = PickersMinMaxValues.MIN_WIND_VALUE.value,
            maximumPickerValue = PickersMinMaxValues.MAX_WIND_VALUE.value
        )

        val pressureModel = WeatherSelectDataModel(
            conditionArray = conditionSymbols,
            weatherAttributeIcon = R.drawable.ic_pressure,
            currentValue = actualPressure.value.toString(),
            minimumPickerValue = PickersMinMaxValues.MIN_PRESSURE_VALUE.value,
            maximumPickerValue = PickersMinMaxValues.MAX_PRESSURE_VALUE.value
        )
        val humidityModel = WeatherSelectDataModel(
            conditionArray = conditionSymbols,
            weatherAttributeIcon = R.drawable.ic_humidity,
            currentValue = actualHumidity.value.toString(),
            minimumPickerValue = PickersMinMaxValues.MIN_HUMIDITY_VALUE.value,
            maximumPickerValue = PickersMinMaxValues.MAX_HUMIDITY_VALUE.value
        )

        for (i in 0..3) {
            addToWeatherAttributeList(
                arrayOf(
                    temperatureModel,
                    windModel,
                    pressureModel,
                    humidityModel
                )[i]
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
            this::onGetFirstRobotHumiditySucess,
            this::onSampleDataError
        )
    }

    private fun getFirstRobotTemperature() {
        cloudDatabaseService.getFirstRobotWeatherStationTemperature(
            this::onGetFirstRobotTemperatureSucess,
            this::onSampleDataError
        )
    }

    private fun getFirstRobotWind() {
        cloudDatabaseService.getFirstRobotWeatherStationWind(
            this::onGetFirstRobotWindSucess,
            this::onSampleDataError
        )
    }

    private fun getFirstRobotPressure() {
        cloudDatabaseService.getFirstRobotWeatherStationPressure(
            this::onGetFirstRobotPressureSucess,
            this::onSampleDataError
        )
    }

    private fun onSampleDataError(sampleString: String) {
    }

    private fun onGetFirstRobotHumiditySucess(firstRobotWeatherStationHumidity: FirstRobotWeatherStationHumidity) {
        actualHumidity.value = firstRobotWeatherStationHumidity.value.toString() + "%"
        createWeatherParametersList()

    }

    private fun onGetFirstRobotTemperatureSucess(firstRobotWeatherStationTemperature: FirstRobotWeatherStationTemperature) {
        actualTemperature.value =
            firstRobotWeatherStationTemperature.value.toString() + " °C"
        createWeatherParametersList()

    }

    private fun onGetFirstRobotWindSucess(firstRobotWeatherStationWind: FirstRobotWeatherStationWind) {
        actualWind.value = firstRobotWeatherStationWind.speed_value.toString() + " km/h"
        createWeatherParametersList()

    }

    private fun onGetFirstRobotPressureSucess(firstRobotWeatherStationPressure: FirstRobotWeatherStationPressure) {
        actualPressure.value = firstRobotWeatherStationPressure.value.toString() + " hPa"
        createWeatherParametersList()

    }

    fun onNextButtonClicked() {
        navigationEvent.postValue(
            CreatorTaskToolsNavigationEvent()
        )
    }
}
