package eu.agribot.agribotapp.ui.fragment.weather

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationHumidity
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationPressure
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationTemperature
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationWind
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService

class WeatherViewModel(
    private val cloudDatabaseService: CloudDatabaseService
) : BaseViewModel() {
    val toastEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val weatherStationWindSpeed: MutableLiveData<String> = MutableLiveData()
    val weatherStationTemperature: MutableLiveData<String> = MutableLiveData()
    val weatherStationPressure: MutableLiveData<String> = MutableLiveData()
    val weatherStationHumidity: MutableLiveData<String> = MutableLiveData()

    fun prepareWeatherStationData() {
        getRobotHumidity()
        getRobotTemperature()
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
        weatherStationHumidity.value = robotWeatherStationHumidity.value.toString()
    }

    private fun onGetRobotTemperatureSucess(robotWeatherStationTemperature: RobotWeatherStationTemperature) {
        weatherStationTemperature.value = robotWeatherStationTemperature.value.toString()
    }

    private fun onGetRobotWindSucess(robotWeatherStationWind: RobotWeatherStationWind) {
        weatherStationWindSpeed.value = robotWeatherStationWind.speed_value.toString()
    }

    private fun onGetRobotPressureSucess(robotWeatherStationPressure: RobotWeatherStationPressure) {
        weatherStationPressure.value = robotWeatherStationPressure.value.toString()
    }

    private fun postErrorMessage(errorText: String) {
        toastEvent.postValue(errorText)
    }
}
