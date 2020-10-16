package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect

import android.util.Log
import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskToolsNavigationEvent
import eu.agribot.agribotapp.domain.model.FirstRobotWeatherStationHumidity
import eu.agribot.agribotapp.domain.model.FirstRobotWeatherStationTemperature
import eu.agribot.agribotapp.domain.model.FirstRobotWeatherStationWind
import eu.agribot.agribotapp.domain.model.TaskDetails
import eu.agribot.agribotapp.domain.model.cdb.FirstRobotWeatherStationPressure
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.TaskDetailsElements
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.CONDITION_EQUAL
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.CONDITION_LESS
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.CONDITION_MORE
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.PICKER_CONDITION_BIGGER
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.PICKER_CONDITION_EQUAL
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.PICKER_CONDITION_LESS
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.PICKER_HUMIDITY_CONDITION
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.PICKER_HUMIDITY_VALUE
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.PICKER_PRESSURE_CONDITION
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.PICKER_PRESSURE_VALUE
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.PICKER_TEMPERATURE_CONDITION
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.PICKER_TEMPERATURE_VALUE
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.PICKER_WIND_CONDITION
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.PICKER_WIND_VALUE
import timber.log.Timber

class CreatorWeatherSelectViewModel(
    private val cloudDatabaseService: CloudDatabaseService,
    private val taskCreatorDataManager: TaskCreatorDataManager
) :
    BaseViewModel() {
    val pickerValues :  MutableLiveData<PickerValuesData> = MutableLiveData()
    val actualTemperature: MutableLiveData<String> = MutableLiveData()
    val actualHumidity: MutableLiveData<String> = MutableLiveData()
    val actualWind: MutableLiveData<String> = MutableLiveData()
    val actualPressure: MutableLiveData<String> = MutableLiveData()


    fun checkIsWeatherSelected(){
        if (taskCreatorDataManager.getTemperatureValue() != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement){
            Log.e("xd", taskCreatorDataManager.getTemperatureValue())
            Log.e("xd", taskCreatorDataManager.getTemperatureCondition())
        }
        if (taskCreatorDataManager.getHumidityValue() != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement){
            Log.e("xd", taskCreatorDataManager.getHumidityValue())
            Log.e("xd", taskCreatorDataManager.getHumidityCondition())
        }
        if (taskCreatorDataManager.getWindValue() != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement){
            Log.e("xd", taskCreatorDataManager.getWindValue())
            Log.e("xd", taskCreatorDataManager.getWindCondition())
        }
        if (taskCreatorDataManager.getPressureValue() != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement){
            Log.e("xd", taskCreatorDataManager.getPressureValue())
            Log.e("xd", taskCreatorDataManager.getPressureValue())
        }
    }

    fun getRobotDataValue() {
        getFirstRobotTemperature()
        getFirstRobotHumidity()
        getFirstRobotPressure()
        getFirstRobotWind()
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
        Timber.e(firstRobotWeatherStationHumidity.toString())
        actualHumidity.value = firstRobotWeatherStationHumidity.value.toString() + "%"
    }

    private fun onFirstRobotTemperatureSucess(firstRobotWeatherStationTemperature: FirstRobotWeatherStationTemperature) {
        actualTemperature.value =
            firstRobotWeatherStationTemperature.temperature.toString() + " °C"
    }

    private fun onFirstRobotWindSucess(firstRobotWeatherStationWind: FirstRobotWeatherStationWind) {
        actualWind.value = firstRobotWeatherStationWind.speed.toString() + " km/h"
    }

    private fun onFirstRobotPressureSucess(firstRobotWeatherStationPressure: FirstRobotWeatherStationPressure) {
        actualPressure.value = firstRobotWeatherStationPressure.value.toString() + " hPa"
    }

    fun onWeatherParametrsChanged(pickerNumber: Int, pickerValue: Int) {
        when (pickerNumber) {
            PICKER_TEMPERATURE_CONDITION -> {
                when (pickerValue) {
                    PICKER_CONDITION_LESS -> {
                        taskCreatorDataManager.onUpdateTemperatureCondition(CONDITION_LESS)
                    }
                    PICKER_CONDITION_BIGGER -> {
                        taskCreatorDataManager.onUpdateTemperatureCondition(CONDITION_MORE)
                    }
                    PICKER_CONDITION_EQUAL -> {
                        taskCreatorDataManager.onUpdateTemperatureCondition(CONDITION_EQUAL)
                    }
                }
            }
            PICKER_TEMPERATURE_VALUE -> {
                taskCreatorDataManager.onUpdateTemperatureValue(pickerValue.toString())
            }
            PICKER_HUMIDITY_CONDITION -> {
                when (pickerValue) {
                    PICKER_CONDITION_LESS -> {
                        taskCreatorDataManager.onUpdatehumidityCondition(CONDITION_LESS)
                    }
                    PICKER_CONDITION_BIGGER -> {
                        taskCreatorDataManager.onUpdatehumidityCondition(CONDITION_MORE)
                    }
                    PICKER_CONDITION_EQUAL -> {
                        taskCreatorDataManager.onUpdatehumidityCondition(CONDITION_EQUAL)
                    }
                }
            }
            PICKER_HUMIDITY_VALUE -> {
                taskCreatorDataManager.onUpdatehumidityValue(pickerValue.toString())
            }
            PICKER_WIND_CONDITION -> {
                when (pickerValue) {
                    PICKER_CONDITION_LESS -> {
                        taskCreatorDataManager.onUpdatewindCondition(CONDITION_LESS)
                    }
                    PICKER_CONDITION_BIGGER -> {
                        taskCreatorDataManager.onUpdatewindCondition(CONDITION_MORE)
                    }

                    PICKER_CONDITION_EQUAL -> {
                        taskCreatorDataManager.onUpdatewindCondition(CONDITION_EQUAL)
                    }
                }
            }
            PICKER_WIND_VALUE -> {
                taskCreatorDataManager.onUpdatewindValue(pickerValue.toString())
            }
            PICKER_PRESSURE_CONDITION -> {
                when (pickerValue) {
                    PICKER_CONDITION_LESS -> {
                        taskCreatorDataManager.onUpdatepressureCondition(CONDITION_LESS)
                    }
                    PICKER_CONDITION_BIGGER -> {
                        taskCreatorDataManager.onUpdatepressureCondition(CONDITION_MORE)
                    }
                    PICKER_CONDITION_EQUAL -> {
                        taskCreatorDataManager.onUpdatepressureCondition(CONDITION_EQUAL)
                    }
                }
            }
            PICKER_PRESSURE_VALUE -> {
                taskCreatorDataManager.onUpdatepressureValue(pickerValue.toString())
            }
        }
    }

    fun onNextButtonClicked() {
        navigationEvent.postValue(
            CreatorTaskToolsNavigationEvent()
        )
    }
}
