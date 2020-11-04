package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskToolsNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.WeatherSelectDataModel
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationHumidity
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationPressure
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationTemperature
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationWind
import eu.agribot.agribotapp.domain.model.cdb.TaskDetails
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
    val toastEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val weatherAttributeList: MutableLiveData<List<WeatherSettingsListItemViewModel>> =
        MutableLiveData(
            listOf()
        )
    private val actualTemperature: MutableLiveData<String> = MutableLiveData()
    private val actualHumidity: MutableLiveData<String> = MutableLiveData()
    private val actualWind: MutableLiveData<String> = MutableLiveData()
    private val actualPressure: MutableLiveData<String> = MutableLiveData()
    private val taskDetail: MutableLiveData<TaskDetails> = MutableLiveData()
    private val initializationCurrentValue = ""
    private val conditionSymbols = arrayOf(
        PickerSymbols.PICKER_CONDITION_LESS.symbol,
        PickerSymbols.PICKER_CONDITION_BIGGER.symbol,
        PickerSymbols.PICKER_CONDITION_EQUAL.symbol
    )

    fun getTaskDetail() {
        taskCreatorDataManager.getTaskParemeters(this::onGotTaskDetail)
    }

    private fun onGotTaskDetail(taskDetails: TaskDetails) {
        taskDetail.value = taskDetails
    }

    fun updateWeatherData(taskDetails: TaskDetails) {
        taskCreatorDataManager.updateTaskParameters(taskDetails)
    }

    fun onWeatherAttributeChanged(weatherAttribute: WeatherAttribute) {
        when (weatherAttribute.attributeID) {
            WeatherItemNumber.TEMPERATURE_ITEM.itemNumber -> {
                if (weatherAttribute.isWeatherActive) {
                    updateWeatherData(
                        TaskDetails(
                            temperature_value = weatherAttribute.weatherValue.toString(),
                            temperature_condition = changeConditionSymbolToText(
                                weatherAttribute.weatherCondition
                            )
                        )
                    )
                } else {
                    updateWeatherData(
                        TaskDetails(
                            temperature_value = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                            temperature_condition = PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                        )
                    )
                }
            }
            WeatherItemNumber.WIND_ITEM.itemNumber -> {
                if (weatherAttribute.isWeatherActive) {
                    updateWeatherData(
                        TaskDetails(
                            wind_value = weatherAttribute.weatherValue.toString(),
                            wind_condition = changeConditionSymbolToText(
                                weatherAttribute.weatherCondition
                            )
                        )
                    )
                } else {
                    updateWeatherData(
                        TaskDetails(
                            wind_value = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                            wind_condition = PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                        )
                    )
                }
            }
            WeatherItemNumber.HUMIDITY_ITEM.itemNumber -> {
                if (weatherAttribute.isWeatherActive) {
                    updateWeatherData(
                        TaskDetails(
                            humidity_value = weatherAttribute.weatherValue.toString(),
                            humidity_condition = PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                        )
                    )
                } else {
                    updateWeatherData(
                        TaskDetails(
                            humidity_value = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                            humidity_condition = PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                        )
                    )
                }
            }
            WeatherItemNumber.PRESSURE_ITEM.itemNumber -> {
                if (weatherAttribute.isWeatherActive) {
                    updateWeatherData(
                        TaskDetails(
                            pressure_value = weatherAttribute.weatherValue.toString(),
                            pressure_condition = changeConditionSymbolToText(
                                weatherAttribute.weatherCondition
                            )
                        )
                    )
                } else {
                    updateWeatherData(
                        TaskDetails(
                            pressure_value = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                            pressure_condition = PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                        )
                    )
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
                currentValue = initializationCurrentValue,
                minimumPickerValue = PickersMinMaxValues.MIN_TEMP_VALUE.value,
                maximumPickerValue = PickersMinMaxValues.MAX_TEMP_VALUE.value,
                isSwitchActive = taskDetail.value?.temperature_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                settedValue = if (taskDetail.value?.temperature_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    (taskDetail.value?.temperature_value!!.toInt())
                } else {
                    PickersMinMaxValues.NO_WEATHER_VALUE_SYMBOL.value
                },
                settedCondition = if (taskDetail.value?.temperature_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    taskDetail.value?.temperature_condition.toString()
                } else {
                    PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                }
            )

        val windModel =
            WeatherSelectDataModel(
                id = WeatherItemNumber.WIND_ITEM.itemNumber,
                conditionArray = conditionSymbols,
                currentValue = initializationCurrentValue,
                minimumPickerValue = PickersMinMaxValues.MIN_WIND_VALUE.value,
                maximumPickerValue = PickersMinMaxValues.MAX_WIND_VALUE.value,
                isSwitchActive = taskDetail.value?.wind_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                settedValue = if (taskDetail.value?.wind_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                ) {
                    (taskDetail.value?.wind_value!!.toInt())
                } else {
                    PickersMinMaxValues.NO_WEATHER_VALUE_SYMBOL.value
                },
                settedCondition = if (taskDetail.value?.wind_condition != PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                ) {
                    taskDetail.value?.wind_condition!!
                } else {
                    PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                }
            )

        val pressureModel =
            WeatherSelectDataModel(
                id = WeatherItemNumber.PRESSURE_ITEM.itemNumber,
                conditionArray = conditionSymbols,
                currentValue = initializationCurrentValue,
                minimumPickerValue = PickersMinMaxValues.MIN_PRESSURE_VALUE.value,
                maximumPickerValue = PickersMinMaxValues.MAX_PRESSURE_VALUE.value,
                isSwitchActive = taskDetail.value?.pressure_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                settedValue = if (taskDetail.value?.pressure_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    (taskDetail.value?.pressure_value!!.toInt())
                } else {
                    PickersMinMaxValues.NO_WEATHER_VALUE_SYMBOL.value
                },
                settedCondition = if (taskDetail.value?.pressure_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    taskDetail.value?.pressure_condition!!
                } else {
                    PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                }

            )

        val humidityModel =
            WeatherSelectDataModel(
                id = WeatherItemNumber.HUMIDITY_ITEM.itemNumber,
                conditionArray = conditionSymbols,
                currentValue = initializationCurrentValue,
                minimumPickerValue = PickersMinMaxValues.MIN_HUMIDITY_VALUE.value,
                maximumPickerValue = PickersMinMaxValues.MAX_HUMIDITY_VALUE.value,
                isSwitchActive = taskDetail.value?.humidity_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                settedValue = if (taskDetail.value?.humidity_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    (taskDetail.value?.humidity_value!!.toInt())
                } else {
                    PickersMinMaxValues.NO_WEATHER_VALUE_SYMBOL.value
                },
                settedCondition = if (taskDetail.value?.humidity_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    taskDetail.value?.humidity_condition!!
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

    private fun onGetFirstRobotTemperatureSucess(robotWeatherStationTemperature: RobotWeatherStationTemperature) {
        actualTemperature.value =
            robotWeatherStationTemperature.value.toString() + WeatherUnit.TEMPERATURE_UNIT.unit
        addToWeatherAttributeList(
            WeatherSelectDataModel(
                id = WeatherItemNumber.TEMPERATURE_ITEM.itemNumber,
                conditionArray = conditionSymbols,
                currentValue = actualTemperature.value.toString(),
                minimumPickerValue = PickersMinMaxValues.MIN_TEMP_VALUE.value,
                maximumPickerValue = PickersMinMaxValues.MAX_TEMP_VALUE.value,
                isSwitchActive = taskDetail.value?.temperature_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                settedValue = if (taskDetail.value?.temperature_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    (taskDetail.value?.temperature_value!!
                        .toInt())
                } else {
                    -1
                },
                settedCondition = if (taskDetail.value?.temperature_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    taskDetail.value?.temperature_condition!!
                } else {
                    PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                }
            )
        )
    }

    private fun onGetFirstRobotWindSucess(robotWeatherStationWind: RobotWeatherStationWind) {
        actualWind.value =
            robotWeatherStationWind.speed_value.toString() + WeatherUnit.WIND_UNIT.unit
        addToWeatherAttributeList(
            WeatherSelectDataModel(
                id = WeatherItemNumber.WIND_ITEM.itemNumber,
                conditionArray = conditionSymbols,
                currentValue = actualWind.value.toString(),
                minimumPickerValue = PickersMinMaxValues.MIN_WIND_VALUE.value,
                maximumPickerValue = PickersMinMaxValues.MAX_WIND_VALUE.value,
                isSwitchActive = taskDetail.value?.wind_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                settedValue = if (taskDetail.value?.wind_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    (taskDetail.value?.wind_value!!.toInt())
                } else {
                    PickersMinMaxValues.NO_WEATHER_VALUE_SYMBOL.value
                },
                settedCondition = if (taskDetail.value?.wind_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    taskDetail.value?.wind_condition!!
                } else {
                    PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                }

            )
        )
    }

    private fun onGetFirstRobotPressureSucess(robotWeatherStationPressure: RobotWeatherStationPressure) {
        actualPressure.value =
            robotWeatherStationPressure.value.toString() + WeatherUnit.PRESSURE_UNIT.unit
        addToWeatherAttributeList(
            WeatherSelectDataModel(
                id = WeatherItemNumber.PRESSURE_ITEM.itemNumber,
                conditionArray = conditionSymbols,
                currentValue = actualPressure.value.toString(),
                minimumPickerValue = PickersMinMaxValues.MIN_PRESSURE_VALUE.value,
                maximumPickerValue = PickersMinMaxValues.MAX_PRESSURE_VALUE.value,
                isSwitchActive = taskDetail.value?.pressure_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                settedValue = if (taskDetail.value?.pressure_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    (taskDetail.value?.pressure_value!!.toInt())
                } else {
                    PickersMinMaxValues.NO_WEATHER_VALUE_SYMBOL.value
                },
                settedCondition = if (taskDetail.value?.pressure_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    taskDetail.value?.pressure_condition!!
                } else {
                    PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                }

            )
        )
    }

    private fun onGetFirstRobotHumiditySucess(robotWeatherStationHumidity: RobotWeatherStationHumidity) {
        actualHumidity.value =
            robotWeatherStationHumidity.value.toString() + WeatherUnit.HUMIDITY_UNIT.unit
        addToWeatherAttributeList(
            WeatherSelectDataModel(
                id = WeatherItemNumber.HUMIDITY_ITEM.itemNumber,
                conditionArray = conditionSymbols,
                currentValue = actualHumidity.value.toString(),
                minimumPickerValue = PickersMinMaxValues.MIN_HUMIDITY_VALUE.value,
                maximumPickerValue = PickersMinMaxValues.MAX_HUMIDITY_VALUE.value,
                isSwitchActive = taskDetail.value?.humidity_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                settedValue = if (taskDetail.value?.humidity_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    (taskDetail.value?.humidity_value!!.toInt())
                } else {
                    PickersMinMaxValues.NO_WEATHER_VALUE_SYMBOL.value
                },
                settedCondition = if (taskDetail.value?.humidity_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                    taskDetail.value?.humidity_condition!!
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
