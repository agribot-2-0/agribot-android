package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskToolsNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.FirstRobotWeatherStationHumidity
import eu.agribot.agribotapp.domain.model.WeatherSelectDataModel
import eu.agribot.agribotapp.domain.model.cdb.FirstRobotWeatherStationPressure
import eu.agribot.agribotapp.domain.model.cdb.FirstRobotWeatherStationTemperature
import eu.agribot.agribotapp.domain.model.cdb.FirstRobotWeatherStationWind
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.WeatherSettingsListItemViewModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.weatherattributes.WeatherAttribute

class CreatorWeatherSelectViewModel(
    private val cloudDatabaseService: CloudDatabaseService,
    private val taskCreatorDataManager: TaskCreatorDataManager,
    private val resourcesProvider: ResourcesProvider
) :
    BaseViewModel() {
    val toastEvent : MutableLiveEvent<String> = MutableLiveEvent()
    val weatherAttributeList: MutableLiveData<List<WeatherSettingsListItemViewModel>> =
        MutableLiveData(
            listOf()
        )
    private val actualTemperature: MutableLiveData<String> = MutableLiveData()
    private val actualHumidity: MutableLiveData<String> = MutableLiveData()
    private val actualWind: MutableLiveData<String> = MutableLiveData()
    private val actualPressure: MutableLiveData<String> = MutableLiveData()
    private val initializationCurrentValue = ""
    private val conditionSymbols = arrayOf(
        PickerSymbols.PICKER_CONDITION_LESS.symbol,
        PickerSymbols.PICKER_CONDITION_BIGGER.symbol,
        PickerSymbols.PICKER_CONDITION_EQUAL.symbol
    )

    fun onWeatherAttributeChanged(weatherAttribute: WeatherAttribute) {
        when (weatherAttribute.attributeID) {
            WeatherItemNumber.TEMPERATURE_ITEM.itemNumber -> {
                if (weatherAttribute.isWeatherActive) {
                    taskCreatorDataManager.onUpdateTemperatureValue(weatherAttribute.weatherValue.toString())
                    taskCreatorDataManager.onUpdateTemperatureCondition(
                        changeConditionSymbolToText(
                            weatherAttribute.weatherCondition
                        )
                    )
                } else {
                    taskCreatorDataManager.onUpdateTemperatureValue(PickerSymbols.PICKER_DEFAULT_VALUE.symbol)
                    taskCreatorDataManager.onUpdateTemperatureCondition(PickerSymbols.PICKER_DEFAULT_VALUE.symbol)
                }
            }
            WeatherItemNumber.WIND_ITEM.itemNumber -> {
                if (weatherAttribute.isWeatherActive) {
                    taskCreatorDataManager.onUpdatewindValue(weatherAttribute.weatherValue.toString())
                    taskCreatorDataManager.onUpdatewindCondition(
                        changeConditionSymbolToText(
                            weatherAttribute.weatherCondition
                        )
                    )
                } else {
                    taskCreatorDataManager.onUpdatewindValue(PickerSymbols.PICKER_DEFAULT_VALUE.symbol)
                    taskCreatorDataManager.onUpdatewindCondition(PickerSymbols.PICKER_DEFAULT_VALUE.symbol)
                }
            }
            WeatherItemNumber.HUMIDITY_ITEM.itemNumber -> {
                if (weatherAttribute.isWeatherActive) {
                    taskCreatorDataManager.onUpdatehumidityValue(weatherAttribute.weatherValue.toString())
                    taskCreatorDataManager.onUpdatehumidityCondition(
                        changeConditionSymbolToText(
                            weatherAttribute.weatherCondition
                        )
                    )
                } else {
                    taskCreatorDataManager.onUpdatehumidityValue(PickerSymbols.PICKER_DEFAULT_VALUE.symbol)
                    taskCreatorDataManager.onUpdatehumidityCondition(PickerSymbols.PICKER_DEFAULT_VALUE.symbol)
                }
            }
            WeatherItemNumber.PRESSURE_ITEM.itemNumber -> {
                if (weatherAttribute.isWeatherActive) {
                    taskCreatorDataManager.onUpdatepressureValue(weatherAttribute.weatherValue.toString())
                    taskCreatorDataManager.onUpdatepressureCondition(
                        changeConditionSymbolToText(
                            weatherAttribute.weatherCondition
                        )
                    )
                } else {
                    taskCreatorDataManager.onUpdatepressureValue(PickerSymbols.PICKER_DEFAULT_VALUE.symbol)
                    taskCreatorDataManager.onUpdatepressureCondition(PickerSymbols.PICKER_DEFAULT_VALUE.symbol)
                }
            }
        }
    }

    private fun changeConditionSymbolToText(symbol: String): String {
        return when (symbol) {
            PickerSymbols.PICKER_CONDITION_BIGGER.symbol -> PickerSymbols.CLOUD_DATA_CONDITION_MORE.symbol
            PickerSymbols.PICKER_CONDITION_LESS.symbol -> PickerSymbols.CLOUD_DATA_CONDITION_LESS.symbol
            PickerSymbols.PICKER_CONDITION_EQUAL.symbol -> PickerSymbols.CLOUD_DATA_CONDITION_EQUAL.symbol
            else -> PickerSymbols.PICKER_DEFAULT_VALUE.symbol
        }
    }

    private fun addToWeatherAttributeList(
        weatherSelectDataModel: WeatherSelectDataModel
    ) {
        val isAttributeAlreadyAdded = weatherAttributeList.value!!.any { arrtibute ->
            arrtibute.id == weatherSelectDataModel.id
        }

        if (!isAttributeAlreadyAdded)
            weatherAttributeList.value = listOf(
                WeatherSettingsListItemViewModel(
                    weatherSelectDataModel.id,
                    weatherSelectDataModel.weatherAttributeIcon,
                    weatherSelectDataModel.conditionArray,
                    weatherSelectDataModel.minimumPickerValue,
                    weatherSelectDataModel.maximumPickerValue,
                    weatherSelectDataModel.currentValue,
                    weatherSelectDataModel.isSwitchActive,
                    weatherSelectDataModel.settedValue,
                    weatherSelectDataModel.settedCondition
                ),
                *weatherAttributeList.value!!.toTypedArray()
            )
        else {
            weatherAttributeList.value = weatherAttributeList.value!!.map { oldAttribute ->
                if (oldAttribute.id == weatherSelectDataModel.id) {
                    WeatherSettingsListItemViewModel(
                        weatherSelectDataModel.id,
                        weatherSelectDataModel.weatherAttributeIcon,
                        weatherSelectDataModel.conditionArray,
                        weatherSelectDataModel.minimumPickerValue,
                        weatherSelectDataModel.maximumPickerValue,
                        weatherSelectDataModel.currentValue,
                        weatherSelectDataModel.isSwitchActive,
                        weatherSelectDataModel.settedValue,
                        weatherSelectDataModel.settedCondition
                    )
                } else {
                    oldAttribute
                }
            }
        }
    }

    private fun createWeatherParametersList() {

        val temperatureModel =
            WeatherSelectDataModel(
                id = WeatherItemNumber.TEMPERATURE_ITEM.itemNumber,
                conditionArray = conditionSymbols,
                weatherAttributeIcon = R.drawable.ic_thermometer,
                currentValue = initializationCurrentValue,
                minimumPickerValue = PickersMinMaxValues.MIN_TEMP_VALUE.value,
                maximumPickerValue = PickersMinMaxValues.MAX_TEMP_VALUE.value,
                isSwitchActive = taskCreatorDataManager.getTemperatureValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                settedValue = if (taskCreatorDataManager.getTemperatureValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    (taskCreatorDataManager.getTemperatureValue()
                        .toInt())
                } else {
                    PickersMinMaxValues.NO_WEATHER_VALUE_SYMBOL.value
                },
                settedCondition = if (taskCreatorDataManager.getTemperatureValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    taskCreatorDataManager.getTemperatureCondition()
                } else {
                    PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                }
            )

        val windModel =
            WeatherSelectDataModel(
                id = WeatherItemNumber.WIND_ITEM.itemNumber,
                conditionArray = conditionSymbols,
                weatherAttributeIcon = R.drawable.ic_wind,
                currentValue = initializationCurrentValue,
                minimumPickerValue = PickersMinMaxValues.MIN_WIND_VALUE.value,
                maximumPickerValue = PickersMinMaxValues.MAX_WIND_VALUE.value,
                isSwitchActive = taskCreatorDataManager.getWindValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                settedValue = if (taskCreatorDataManager.getWindValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                ) {
                    (taskCreatorDataManager.getWindValue()
                        .toInt())
                } else {
                    PickersMinMaxValues.NO_WEATHER_VALUE_SYMBOL.value
                },
                settedCondition = if (taskCreatorDataManager.getWindCondition() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                ) {
                    taskCreatorDataManager.getWindCondition()
                } else {
                    PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                }
            )

        val pressureModel =
            WeatherSelectDataModel(
                id = WeatherItemNumber.PRESSURE_ITEM.itemNumber,
                conditionArray = conditionSymbols,
                weatherAttributeIcon = R.drawable.ic_pressure,
                currentValue = initializationCurrentValue,
                minimumPickerValue = PickersMinMaxValues.MIN_PRESSURE_VALUE.value,
                maximumPickerValue = PickersMinMaxValues.MAX_PRESSURE_VALUE.value,
                isSwitchActive = taskCreatorDataManager.getPressureValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                settedValue = if (taskCreatorDataManager.getPressureValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    (taskCreatorDataManager.getPressureValue()
                        .toInt())
                } else {
                    PickersMinMaxValues.NO_WEATHER_VALUE_SYMBOL.value
                },
                settedCondition = if (taskCreatorDataManager.getPressureValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    taskCreatorDataManager.getPressureCondition()
                } else {
                    PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                }

            )

        val humidityModel =
            WeatherSelectDataModel(
                id = WeatherItemNumber.HUMIDITY_ITEM.itemNumber,
                conditionArray = conditionSymbols,
                weatherAttributeIcon = R.drawable.ic_humidity,
                currentValue = initializationCurrentValue,
                minimumPickerValue = PickersMinMaxValues.MIN_HUMIDITY_VALUE.value,
                maximumPickerValue = PickersMinMaxValues.MAX_HUMIDITY_VALUE.value,
                isSwitchActive = taskCreatorDataManager.getHumidityValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                settedValue = if (taskCreatorDataManager.getHumidityValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    (taskCreatorDataManager.getHumidityValue()
                        .toInt())
                } else {
                    PickersMinMaxValues.NO_WEATHER_VALUE_SYMBOL.value
                },
                settedCondition = if (taskCreatorDataManager.getHumidityValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    taskCreatorDataManager.getHumidityCondition()
                } else {
                    PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                }
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
            this::postErrorMessage
        )
    }

    private fun getFirstRobotTemperature() {
        cloudDatabaseService.getFirstRobotWeatherStationTemperature(
            this::onGetFirstRobotTemperatureSucess,
            this::postErrorMessage
        )
    }

    private fun getFirstRobotWind() {
        cloudDatabaseService.getFirstRobotWeatherStationWind(
            this::onGetFirstRobotWindSucess,
            this::postErrorMessage
        )
    }

    private fun getFirstRobotPressure() {
        cloudDatabaseService.getFirstRobotWeatherStationPressure(
            this::onGetFirstRobotPressureSucess,
            this::postErrorMessage
        )
    }

    private fun postErrorMessage(errorText: String) {
        toastEvent.postValue(errorText)
    }

    private fun onGetFirstRobotTemperatureSucess(firstRobotWeatherStationTemperature: FirstRobotWeatherStationTemperature) {
        actualTemperature.value = firstRobotWeatherStationTemperature.value.toString() + WeatherUnit.TEMPERATURE_UNIT.unit
        addToWeatherAttributeList(
            WeatherSelectDataModel(
                id = WeatherItemNumber.TEMPERATURE_ITEM.itemNumber,
                conditionArray = conditionSymbols,
                weatherAttributeIcon = R.drawable.ic_thermometer,
                currentValue = actualTemperature.value.toString(),
                minimumPickerValue = PickersMinMaxValues.MIN_TEMP_VALUE.value,
                maximumPickerValue = PickersMinMaxValues.MAX_TEMP_VALUE.value,
                isSwitchActive = taskCreatorDataManager.getTemperatureValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                settedValue = if (taskCreatorDataManager.getTemperatureValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    (taskCreatorDataManager.getTemperatureValue()
                        .toInt())
                } else {
                    -1
                },
                settedCondition = if (taskCreatorDataManager.getTemperatureValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    taskCreatorDataManager.getTemperatureCondition()
                } else {
                    PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                }
            )
        )
    }

    private fun onGetFirstRobotWindSucess(firstRobotWeatherStationWind: FirstRobotWeatherStationWind) {
        actualWind.value = firstRobotWeatherStationWind.speed_value.toString() + WeatherUnit.WIND_UNIT.unit
        addToWeatherAttributeList(
            WeatherSelectDataModel(
                id = WeatherItemNumber.WIND_ITEM.itemNumber,
                conditionArray = conditionSymbols,
                weatherAttributeIcon = R.drawable.ic_wind,
                currentValue = actualWind.value.toString(),
                minimumPickerValue = PickersMinMaxValues.MIN_WIND_VALUE.value,
                maximumPickerValue = PickersMinMaxValues.MAX_WIND_VALUE.value,
                isSwitchActive = taskCreatorDataManager.getWindValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                settedValue = if (taskCreatorDataManager.getWindValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    (taskCreatorDataManager.getWindValue()
                        .toInt())
                } else {
                    PickersMinMaxValues.NO_WEATHER_VALUE_SYMBOL.value
                },
                settedCondition = if (taskCreatorDataManager.getWindValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    taskCreatorDataManager.getWindCondition()
                } else {
                    PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                }

            )
        )
    }

    private fun onGetFirstRobotPressureSucess(firstRobotWeatherStationPressure: FirstRobotWeatherStationPressure) {
        actualPressure.value = firstRobotWeatherStationPressure.value.toString() + WeatherUnit.PRESSURE_UNIT.unit
        addToWeatherAttributeList(
            WeatherSelectDataModel(
                id = WeatherItemNumber.PRESSURE_ITEM.itemNumber,
                conditionArray = conditionSymbols,
                weatherAttributeIcon = R.drawable.ic_pressure,
                currentValue = actualPressure.value.toString(),
                minimumPickerValue = PickersMinMaxValues.MIN_PRESSURE_VALUE.value,
                maximumPickerValue = PickersMinMaxValues.MAX_PRESSURE_VALUE.value,
                isSwitchActive = taskCreatorDataManager.getPressureValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                settedValue = if (taskCreatorDataManager.getPressureValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    (taskCreatorDataManager.getPressureValue()
                        .toInt())
                } else {
                    PickersMinMaxValues.NO_WEATHER_VALUE_SYMBOL.value
                },
                settedCondition = if (taskCreatorDataManager.getPressureValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    taskCreatorDataManager.getPressureCondition()
                } else {
                    PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                }

            )
        )
    }

    private fun onGetFirstRobotHumiditySucess(firstRobotWeatherStationHumidity: FirstRobotWeatherStationHumidity) {
        actualHumidity.value = firstRobotWeatherStationHumidity.value.toString() + WeatherUnit.HUMIDITY_UNIT.unit
        addToWeatherAttributeList(
            WeatherSelectDataModel(
                id = WeatherItemNumber.HUMIDITY_ITEM.itemNumber,
                conditionArray = conditionSymbols,
                weatherAttributeIcon = R.drawable.ic_humidity,
                currentValue = actualHumidity.value.toString(),
                minimumPickerValue = PickersMinMaxValues.MIN_HUMIDITY_VALUE.value,
                maximumPickerValue = PickersMinMaxValues.MAX_HUMIDITY_VALUE.value,
                isSwitchActive = taskCreatorDataManager.getHumidityValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                settedValue = if (taskCreatorDataManager.getHumidityValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    (taskCreatorDataManager.getHumidityValue()
                        .toInt())
                } else {
                    PickersMinMaxValues.NO_WEATHER_VALUE_SYMBOL.value
                },
                settedCondition = if (taskCreatorDataManager.getHumidityValue() != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    taskCreatorDataManager.getHumidityCondition()
                } else {
                    PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                }
            )
        )
    }

    fun onNextButtonClicked() {
        navigationEvent.postValue(
            CreatorTaskToolsNavigationEvent()
        )
    }
}
