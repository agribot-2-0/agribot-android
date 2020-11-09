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
        getCurrentWeatherAttributes()
        val weatherParametrList =
            listOf(TemperatureItem(
                presetValue = taskCreatorDataManager.taskDetails.temperature_value,
                isSwitchActive = (taskCreatorDataManager.taskDetails.temperature_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol),
                settedCondition = taskCreatorDataManager.taskDetails.temperature_condition
            ).toWeatherDataModel(), PressureItem(
                presetValue = taskCreatorDataManager.taskDetails.pressure_value,
                isSwitchActive = (taskCreatorDataManager.taskDetails.pressure_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol),
                settedCondition = taskCreatorDataManager.taskDetails.pressure_condition
            ).toWeatherDataModel(), WindItem(
                presetValue = taskCreatorDataManager.taskDetails.wind_value,
                isSwitchActive = (taskCreatorDataManager.taskDetails.wind_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol),
                settedCondition = taskCreatorDataManager.taskDetails.wind_condition
            ).toWeatherDataModel(), HumidityItem(
                presetValue = taskCreatorDataManager.taskDetails.humidity_value,
                isSwitchActive = (taskCreatorDataManager.taskDetails.humidity_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol),
                settedCondition = taskCreatorDataManager.taskDetails.humidity_condition
            ).toWeatherDataModel())

        for (weatherItem in weatherParametrList) {
            addToWeatherAttributeList(weatherItem)
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
                if (!weatherAttribute.isWeatherItemActive) {
                    taskCreatorDataManager.onUpdatehumidityValue(PickerSymbols.PICKER_DEFAULT_VALUE.symbol)
                }
                taskCreatorDataManager.onUpdatehumidityValue(weatherAttribute.weatherValue)
                taskCreatorDataManager.onUpdatehumidityCondition(
                    changeConditionSymbolToText(
                        weatherAttribute.weatherCondition
                    )
                )
            }
            WeatherItemNumber.WIND_ITEM.itemNumber -> {
                if (!weatherAttribute.isWeatherItemActive) {
                    taskCreatorDataManager.onUpdatewindValue(PickerSymbols.PICKER_DEFAULT_VALUE.symbol)
                }
                taskCreatorDataManager.onUpdatewindValue(weatherAttribute.weatherValue)
                taskCreatorDataManager.onUpdatewindCondition(
                    changeConditionSymbolToText(
                        weatherAttribute.weatherCondition
                    )
                )
            }
            WeatherItemNumber.PRESSURE_ITEM.itemNumber -> {
                if (!weatherAttribute.isWeatherItemActive) {
                    taskCreatorDataManager.onUpdatepressureValue(PickerSymbols.PICKER_DEFAULT_VALUE.symbol)
                }
                taskCreatorDataManager.onUpdatepressureValue(weatherAttribute.weatherValue)
                taskCreatorDataManager.onUpdatepressureCondition(
                    changeConditionSymbolToText(
                        weatherAttribute.weatherCondition
                    )
                )
            }
            WeatherItemNumber.TEMPERATURE_ITEM.itemNumber -> {
                if (!weatherAttribute.isWeatherItemActive) {
                    taskCreatorDataManager.onUpdateTemperatureValue(PickerSymbols.PICKER_DEFAULT_VALUE.symbol)
                }
                taskCreatorDataManager.onUpdateTemperatureValue(weatherAttribute.weatherValue)
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
            HumidityItem(
                isSwitchActive = (taskCreatorDataManager.getTaskParemeters().humidity_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol),
                currentValue = weatherStationHumidity.value.toString() + WeatherUnit.HUMIDITY_UNIT.unit
            ).toWeatherDataModel()
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
            TemperatureItem(
                isSwitchActive = (taskCreatorDataManager.getTaskParemeters().temperature_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol),
                currentValue = weatherStationTemperature.value.toString() + WeatherUnit.TEMPERATURE_UNIT.unit
            ).toWeatherDataModel()
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
            WindItem(
                isSwitchActive = (taskCreatorDataManager.getTaskParemeters().wind_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol),
                currentValue = weatherStationWind.speed_value.toString() + WeatherUnit.WIND_UNIT.unit
            ).toWeatherDataModel()
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
            PressureItem(
                isSwitchActive = (taskCreatorDataManager.getTaskParemeters().pressure_value != PickerSymbols.PICKER_DEFAULT_VALUE.symbol),
                currentValue = weatherStationPressure.value.toString() + WeatherUnit.PRESSURE_UNIT.unit
            ).toWeatherDataModel()
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
                        weatherSelectDataModel.isSwitchActive,
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
