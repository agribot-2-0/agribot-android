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
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.WeatherItemNumber
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.WeatherUnit
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.WeatherSettingsListItemViewModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.weatherattributes.HumidityItem
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.weatherattributes.PressureItem
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.weatherattributes.TemperatureItem
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.weatherattributes.WeatherAttribute
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.weatherattributes.WindItem

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
        prepareWeatherStationData()
        val weatherParametrList =
            listOf(
                TemperatureItem(
                    presetValue = taskCreatorDataManager.taskDetails.temperatureValue,
                    isSwitchActive = (taskCreatorDataManager.taskDetails.temperatureValue != PickerSymbols.PICKER_DEFAULT_VALUE.symbol),
                    presetCondition = taskCreatorDataManager.taskDetails.temperatureCondition
                ).toWeatherDataModel(),

                PressureItem(
                    presetValue = taskCreatorDataManager.taskDetails.pressureValue,
                    isSwitchActive = (taskCreatorDataManager.taskDetails.pressureValue != PickerSymbols.PICKER_DEFAULT_VALUE.symbol),
                    presetCondition = taskCreatorDataManager.taskDetails.pressureCondition
                ).toWeatherDataModel(),

                WindItem(
                    presetValue = taskCreatorDataManager.taskDetails.windValue,
                    isSwitchActive = (taskCreatorDataManager.taskDetails.windValue != PickerSymbols.PICKER_DEFAULT_VALUE.symbol),
                    presetCondition = taskCreatorDataManager.taskDetails.windCondition
                ).toWeatherDataModel(),

                HumidityItem(
                    presetValue = taskCreatorDataManager.taskDetails.humidityValue,
                    isSwitchActive = (taskCreatorDataManager.taskDetails.humidityValue != PickerSymbols.PICKER_DEFAULT_VALUE.symbol),
                    presetCondition = taskCreatorDataManager.taskDetails.humidityCondition
                ).toWeatherDataModel()
            )

        for (weatherItem in weatherParametrList) {
            addToWeatherAttributeList(weatherItem)
        }
    }

    private fun prepareWeatherStationData() {
        weatherAttributeList.value = listOf()
        getRobotTemperature()
        getRobotHumidity()
        getRobotWind()
        getRobotPressure()
    }

    fun onWeatherAttributeChanged(weatherAttribute: WeatherAttribute) {
        when (weatherAttribute.attributeID) {
            WeatherItemNumber.HUMIDITY_ITEM.itemNumber -> {
                if (!weatherAttribute.isWeatherItemActive) {
                    updateHumidityAttribute(
                        PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                        PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                    )
                } else {
                    updateHumidityAttribute(
                        weatherAttribute.weatherValue,
                        changeConditionSymbolToText(
                            weatherAttribute.weatherCondition
                        )
                    )
                }
            }
            WeatherItemNumber.WIND_ITEM.itemNumber -> {
                if (!weatherAttribute.isWeatherItemActive) {
                    updateWindAttribute(
                        PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                        PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                    )
                } else {
                    updateWindAttribute(
                        weatherAttribute.weatherValue,
                        changeConditionSymbolToText(
                            weatherAttribute.weatherCondition
                        )
                    )
                }
            }
            WeatherItemNumber.PRESSURE_ITEM.itemNumber -> {
                if (!weatherAttribute.isWeatherItemActive) {
                    updatePressureAttribute(
                        PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                        PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                    )
                } else {
                    updatePressureAttribute(
                        weatherAttribute.weatherValue,
                        changeConditionSymbolToText(
                            weatherAttribute.weatherCondition
                        )
                    )
                }
            }
            WeatherItemNumber.TEMPERATURE_ITEM.itemNumber -> {
                if (!weatherAttribute.isWeatherItemActive) {
                    updateTemperatureAttribute(
                        PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
                        PickerSymbols.PICKER_DEFAULT_VALUE.symbol
                    )
                } else {
                    updateTemperatureAttribute(
                        weatherAttribute.weatherValue,
                        changeConditionSymbolToText(
                            weatherAttribute.weatherCondition
                        )
                    )
                }
            }
        }
    }

    private fun updateHumidityAttribute(value: String, condition: String) {
        taskCreatorDataManager.onUpdatehumidityValue(value)
        taskCreatorDataManager.onUpdatehumidityCondition(condition)
    }

    private fun updateWindAttribute(value: String, condition: String) {
        taskCreatorDataManager.onUpdatewindValue(value)
        taskCreatorDataManager.onUpdatewindCondition(condition)
    }

    private fun updatePressureAttribute(value: String, condition: String) {
        taskCreatorDataManager.onUpdatepressureValue(value)
        taskCreatorDataManager.onUpdatepressureCondition(condition)
    }

    private fun updateTemperatureAttribute(value: String, condition: String) {
        taskCreatorDataManager.onUpdateTemperatureValue(value)
        taskCreatorDataManager.onUpdateTemperatureCondition(condition)
    }

    private fun getRobotHumidity() {
        cloudDatabaseService.getRobotWeatherStationHumidity(
            this::onGetRobotHumiditySucess,
            this::postErrorMessage
        )
    }

    private fun onGetRobotHumiditySucess(weatherStationHumidity: RobotWeatherStationHumidity) {
        addToWeatherAttributeList(
            HumidityItem(
                isSwitchActive = (taskCreatorDataManager.getTaskParemeters().humidityValue != PickerSymbols.PICKER_DEFAULT_VALUE.symbol),
                currentValue = weatherStationHumidity.value.toString() + WeatherUnit.HUMIDITY_UNIT.unit
            ).toWeatherDataModel()
        )
    }

    private fun getRobotTemperature() {
        cloudDatabaseService.getRobotWeatherStationTemperature(
            this::onGetRobotTemperatureSucess,
            this::postErrorMessage
        )
    }

    private fun onGetRobotTemperatureSucess(weatherStationTemperature: RobotWeatherStationTemperature) {
        addToWeatherAttributeList(
            TemperatureItem(
                isSwitchActive = (taskCreatorDataManager.getTaskParemeters().temperatureValue != PickerSymbols.PICKER_DEFAULT_VALUE.symbol),
                currentValue = weatherStationTemperature.value.toString() + WeatherUnit.TEMPERATURE_UNIT.unit
            ).toWeatherDataModel()
        )
    }

    private fun getRobotWind() {
        cloudDatabaseService.getRobotWeatherStationWind(
            this::onGetRobotWindSucess,
            this::postErrorMessage
        )
    }

    private fun onGetRobotWindSucess(weatherStationWind: RobotWeatherStationWind) {
        addToWeatherAttributeList(
            WindItem(
                isSwitchActive = (taskCreatorDataManager.getTaskParemeters().windValue != PickerSymbols.PICKER_DEFAULT_VALUE.symbol),
                currentValue = weatherStationWind.speedValue.toString() + WeatherUnit.WIND_UNIT.unit
            ).toWeatherDataModel()
        )
    }

    private fun getRobotPressure() {
        cloudDatabaseService.getRobotWeatherStationPressure(
            this::onGetRobotPressureSucess,
            this::postErrorMessage
        )
    }

    private fun onGetRobotPressureSucess(weatherStationPressure: RobotWeatherStationPressure) {
        addToWeatherAttributeList(
            PressureItem(
                isSwitchActive = (taskCreatorDataManager.getTaskParemeters().pressureValue != PickerSymbols.PICKER_DEFAULT_VALUE.symbol),
                currentValue = weatherStationPressure.value.toString() + WeatherUnit.PRESSURE_UNIT.unit
            ).toWeatherDataModel()
        )
    }

    private fun postErrorMessage(errorText: String) {
        toastEvent.postValue(errorText)
    }

    private fun addToWeatherAttributeList(
        weatherSelectDataModel: WeatherSelectDataModel
    ) { val isAttributeAlreadyAdded = weatherAttributeList.value!!.any { arrtibute ->
            arrtibute.id == weatherSelectDataModel.id
        }
        if (!isAttributeAlreadyAdded)
            weatherAttributeList.value = listOf(
                *weatherAttributeList.value!!.toTypedArray(),
                        WeatherSettingsListItemViewModel(
                    weatherSelectDataModel.id,
                    weatherSelectDataModel.conditionArray,
                    weatherSelectDataModel.minimumPickerValue,
                    weatherSelectDataModel.maximumPickerValue,
                    weatherSelectDataModel.currentValue,
                    weatherSelectDataModel.isSwitchActive,
                    weatherSelectDataModel.presetValue,
                    weatherSelectDataModel.presetCondition
                )
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
                        weatherSelectDataModel.isSwitchActive,
                        oldAttribute.settedValue,
                        oldAttribute.presetCondition
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
