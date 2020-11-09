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
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.PickerSymbols
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.PickersMinMaxValues
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.WeatherItemNumber
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.WeatherUnit
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

    fun prepareWeatherAttributeListItem() {
        getCurrentWeatherAttributes()

        val temperatureitem = WeatherSelectDataModel(
            id = WeatherItemNumber.TEMPERATURE_ITEM.itemNumber,
            minimumPickerValue = PickersMinMaxValues.MIN_TEMP_VALUE.value,
            maximumPickerValue = PickersMinMaxValues.MAX_TEMP_VALUE.value,
            isSwitchActive = taskCreatorDataManager.getTaskParemeters().temperature_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
            settedValue = if (taskCreatorDataManager.getTaskParemeters().temperature_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                taskCreatorDataManager.getTaskParemeters().temperature_value.toInt()
            } else {
                PickersMinMaxValues.DEFAULT_TEMP_VALUE.value
            },
            settedCondition = if (taskCreatorDataManager.getTaskParemeters().temperature_condition != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                taskCreatorDataManager.getTaskParemeters().temperature_condition
            } else {
                PickerSymbols.PICKER_CONDITION_BIGGER.symbol
            }
        )

        val pressureItem = WeatherSelectDataModel(
            id = WeatherItemNumber.PRESSURE_ITEM.itemNumber,
            minimumPickerValue = PickersMinMaxValues.MIN_PRESSURE_VALUE.value,
            maximumPickerValue = PickersMinMaxValues.MAX_PRESSURE_VALUE.value,
            isSwitchActive = taskCreatorDataManager.getTaskParemeters().pressure_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol,

            settedValue = if (taskCreatorDataManager.getTaskParemeters().pressure_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                taskCreatorDataManager.getTaskParemeters().pressure_value.toInt()
            } else {
                PickersMinMaxValues.DEFAULT_PRESSURE_VALUE.value
            },
            settedCondition = if (taskCreatorDataManager.getTaskParemeters().pressure_condition != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                taskCreatorDataManager.getTaskParemeters().pressure_condition
            } else {
                PickerSymbols.PICKER_CONDITION_BIGGER.symbol
            }

        )
        val windItem = WeatherSelectDataModel(
            id = WeatherItemNumber.WIND_ITEM.itemNumber,
            minimumPickerValue = PickersMinMaxValues.MIN_WIND_VALUE.value,
            maximumPickerValue = PickersMinMaxValues.MAX_WIND_VALUE.value,
            isSwitchActive = taskCreatorDataManager.getTaskParemeters().wind_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol,

            settedValue = if (taskCreatorDataManager.getTaskParemeters().wind_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                taskCreatorDataManager.getTaskParemeters().wind_value.toInt()
            } else {
                PickersMinMaxValues.DEFAULT_WIND_VALUE.value
            },
            settedCondition = if (taskCreatorDataManager.getTaskParemeters().wind_condition != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                taskCreatorDataManager.getTaskParemeters().wind_condition
            } else {
                PickerSymbols.PICKER_CONDITION_BIGGER.symbol
            }

        )
        val humidityitem = WeatherSelectDataModel(
            id = WeatherItemNumber.HUMIDITY_ITEM.itemNumber,
            minimumPickerValue = PickersMinMaxValues.MIN_HUMIDITY_VALUE.value,
            maximumPickerValue = PickersMinMaxValues.MAX_HUMIDITY_VALUE.value,
            isSwitchActive = taskCreatorDataManager.getTaskParemeters().humidity_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
            settedValue = if (taskCreatorDataManager.getTaskParemeters().humidity_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                taskCreatorDataManager.getTaskParemeters().humidity_value.toInt()
            } else {
                PickersMinMaxValues.DEFAULT_HUMIDITY_VALUE.value
            },
            settedCondition = if (taskCreatorDataManager.getTaskParemeters().humidity_condition != PickerSymbols.PICKER_DEFAULT_VALUE.symbol) {
                taskCreatorDataManager.getTaskParemeters().humidity_condition
            } else {
                PickerSymbols.PICKER_CONDITION_BIGGER.symbol
            }
        )

        val listItem =
            listOf(temperatureitem, pressureItem, windItem, humidityitem)
        for (x in 0..3) {
            addToWeatherAttributeList(listItem[x])
        }
    }

    private fun getCurrentWeatherAttributes() {
        getFirstRobotHumidity()
        getFirstRobotTemperature()
        getFirstRobotWind()
        getFirstRobotPressure()
    }

    fun onWeatherAttributeChanged(weatherAttribute: WeatherAttribute) {
        when (weatherAttribute.attributeID) {
            WeatherItemNumber.HUMIDITY_ITEM.itemNumber -> {
                taskCreatorDataManager.onUpdatehumidityValue(weatherAttribute.weatherValue.toString())
                taskCreatorDataManager.onUpdatehumidityCondition(
                    changeConditionSymbolToText(
                        weatherAttribute.weatherCondition
                    )
                )
            }
            WeatherItemNumber.WIND_ITEM.itemNumber -> {
                taskCreatorDataManager.onUpdatewindValue(weatherAttribute.weatherValue.toString())
                taskCreatorDataManager.onUpdatewindCondition(
                    changeConditionSymbolToText(
                        weatherAttribute.weatherCondition
                    )
                )
            }
            WeatherItemNumber.PRESSURE_ITEM.itemNumber -> {
                taskCreatorDataManager.onUpdatepressureValue(weatherAttribute.weatherValue.toString())
                taskCreatorDataManager.onUpdatepressureCondition(
                    changeConditionSymbolToText(
                        weatherAttribute.weatherCondition
                    )
                )
            }
            WeatherItemNumber.TEMPERATURE_ITEM.itemNumber -> {
                taskCreatorDataManager.onUpdateTemperatureValue(weatherAttribute.weatherValue.toString())
                taskCreatorDataManager.onUpdateTemperatureCondition(
                    changeConditionSymbolToText(
                        weatherAttribute.weatherCondition
                    )
                )
            }
        }
    }

    private fun getFirstRobotHumidity() {
        cloudDatabaseService.getRobotWeatherStationHumidity(
            this::onGetFirstRobotHumiditySucess,
            this::postErrorMessage
        )
    }

    private fun onGetFirstRobotHumiditySucess(weatherStationHumidity: RobotWeatherStationHumidity) {
        addToWeatherAttributeList(
            WeatherSelectDataModel(
                id = WeatherItemNumber.HUMIDITY_ITEM.itemNumber,
                currentValue = weatherStationHumidity.value.toString() + WeatherUnit.HUMIDITY_UNIT.unit
            )
        )
    }

    private fun getFirstRobotTemperature() {
        cloudDatabaseService.getRobotWeatherStationTemperature(
            this::onGetFirstRobotTemperatureSucess,
            this::postErrorMessage
        )
    }

    private fun onGetFirstRobotTemperatureSucess(weatherStationTemperature: RobotWeatherStationTemperature) {
        addToWeatherAttributeList(
            WeatherSelectDataModel(
                id = WeatherItemNumber.TEMPERATURE_ITEM.itemNumber,
                currentValue = weatherStationTemperature.value.toString() + WeatherUnit.TEMPERATURE_UNIT.unit
            )

        )
    }

    private fun getFirstRobotWind() {
        cloudDatabaseService.getRobotWeatherStationWind(
            this::onGetFirstRobotWindSucess,
            this::postErrorMessage
        )
    }

    private fun onGetFirstRobotWindSucess(weatherStationWind: RobotWeatherStationWind) {
        addToWeatherAttributeList(
            WeatherSelectDataModel(
                id = WeatherItemNumber.WIND_ITEM.itemNumber,
                currentValue = weatherStationWind.speed_value.toString() + WeatherUnit.WIND_UNIT.unit
            )
        )
    }

    private fun getFirstRobotPressure() {
        cloudDatabaseService.getRobotWeatherStationPressure(
            this::onGetFirstRobotPressureSucess,
            this::postErrorMessage
        )
    }

    private fun onGetFirstRobotPressureSucess(weatherStationPressure: RobotWeatherStationPressure) {
        addToWeatherAttributeList(
            WeatherSelectDataModel(
                id = WeatherItemNumber.PRESSURE_ITEM.itemNumber,
                currentValue = weatherStationPressure.value.toString() + WeatherUnit.PRESSURE_UNIT.unit
            )
        )
    }

    private fun postErrorMessage(errorText: String) {
        toastEvent.postValue(errorText)
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
                        oldAttribute.id,
                        oldAttribute.conditionArray,
                        oldAttribute.minimumPickerValue,
                        oldAttribute.maximumPickerValue,
                        weatherSelectDataModel.currentValue,
                        oldAttribute.isSwitchActive,
                        oldAttribute.settedValue,
                        oldAttribute.settedCondition
                    )
                } else {
                    oldAttribute
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

    fun onNextButtonClicked() {
        navigationEvent.postValue(
            CreatorTaskToolsNavigationEvent()
        )
    }
}
