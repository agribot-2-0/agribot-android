package eu.agribot.agribotapp.ui.fragment.weather

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationHumidity
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationPressure
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationTemperature
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationWind
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateViewModel
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.items.RobotStateItems
import eu.agribot.agribotapp.ui.fragment.taskcreator.TaskDetailsElements

class WeatherViewModel(
    private val cloudDatabaseService: CloudDatabaseService
) : BaseViewModel() {
    val weatherState: MutableLiveData<List<RobotStateViewModel>> = MutableLiveData(listOf())
    val toastEvent: MutableLiveEvent<String> = MutableLiveEvent()

    fun prepareWeatherStationData() {
        getRobotTemperature()
        getRobotHumidity()
        getRobotWind()
        getRobotPressure()
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
                robotWeatherStationHumidity.value.toString(),
                TaskDetailsElements.PERCENT.detailsElement

            )
        )
    }

    private fun onGetRobotTemperatureSucess(robotWeatherStationTemperature: RobotWeatherStationTemperature) {
        addParameterToRobotDataStateList(
            RobotStateViewModel(
                RobotStateItems.TEMPERATURE.ID,
                approximateParam(robotWeatherStationTemperature.value),
                TaskDetailsElements.TEMPERATURE_UNIT.detailsElement

            )
        )
    }

    private fun onGetRobotWindSucess(robotWeatherStationWind: RobotWeatherStationWind) {
        addParameterToRobotDataStateList(
            RobotStateViewModel(
                RobotStateItems.WIND.ID,
                approximateParam(robotWeatherStationWind.speed_value),
                TaskDetailsElements.SPEED_UNIT.detailsElement
            )
        )
    }

    private fun approximateParam(value: Double): String {
        return String.format("%.2f", value)
    }

    private fun onGetRobotPressureSucess(robotWeatherStationPressure: RobotWeatherStationPressure) {
        addParameterToRobotDataStateList(
            RobotStateViewModel(
                RobotStateItems.PRESSURE.ID,
                approximateParam(robotWeatherStationPressure.value),
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
}
