package eu.agribot.agribotapp.ui.fragment.manualcontrol

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.TTControlNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.AppStorageService
import eu.agribot.agribotapp.domain.service.InternetConnectionService
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.domain.service.joystick.JoysticFilter
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateViewModel
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.items.RobotStateItems
import eu.agribot.agribotapp.ui.fragment.manualcontrol.recycler.actionbottombar.ActionID
import eu.agribot.agribotapp.ui.fragment.manualcontrol.recycler.actionbottombar.ManualActionsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ManualControlViewModel(
    private val manualModeService: ManualModeService,
    private val resourcesProvider: ResourcesProvider,
    private val appStorageService: AppStorageService,
    private val internetConnectionService: InternetConnectionService
) : BaseViewModel() {

    companion object {
        const val COUNTER_INITIALIZATION_VALUE = 0
        const val COUNTER_INCREMENT_VALUE = 1
    }

    val diagnosticDataList: MutableLiveData<List<RobotStateViewModel>> =
        MutableLiveData(listOf())

    val toastMessage: MutableLiveEvent<String> = MutableLiveEvent()
    val actionsList: MutableLiveData<List<ManualActionsViewModel>> = MutableLiveData(listOf())
    private val joystickMessageCounter: MutableLiveData<Int> = MutableLiveData()

    fun onActionClicked(actionID: Int) {
        when (actionID) {
            ActionID.WAKEUP.action -> {
                onWakeupButtonCLicked(onError = this::onWakeupError)
            }

            ActionID.RUN_ENGINE.action -> {
                startStopEngine()
            }

            ActionID.OVERRIDE_MODE.action -> {
                onTTControlButtonClicked()
            }
        }
    }

    fun createActionsList() {
        actionsList.value = listOf(
            ManualActionsViewModel(
                ActionID.WAKEUP.action,
                resourcesProvider.getString(R.string.wakeup_button)
            ),
            ManualActionsViewModel(
                ActionID.RUN_ENGINE.action,
                resourcesProvider.getString(R.string.start_stop_engine)
            ),
            ManualActionsViewModel(
                ActionID.OVERRIDE_MODE.action,
                resourcesProvider.getString(R.string.override_button)
            )
        )
    }

    fun onToolControlClicked() {
        navigationEvent.postValue(
            ToolControlNavigationEvent()
        )
    }

    fun getStreamUrl(): String = appStorageService.getStreamUrl()

    fun onJoystickParametrChange(angel: Int, strength: Int) = viewModelScope.launch {
        if (getJoystickMessageCounter() % JoysticFilter.JOYSTICK_FILTER_VALUE.value == COUNTER_INITIALIZATION_VALUE) {
            manualModeService.onManualSteeringJoystickParametrChanged(angel, strength)
        }
    }

    @ExperimentalCoroutinesApi
    fun setDiagnosticData() = viewModelScope.launch {
        manualModeService.observeDiagnosticData().collect { diagnosticData ->
            if (diagnosticData != null) {
                addToDiagnosticDataList(
                    RobotStateViewModel(
                        RobotStateItems.MANUAL_CONTROL_VOLTAGE.ID,
                        diagnosticData.batteryVoltage,
                        resourcesProvider.getString(R.string.volt)
                    )
                )
                addToDiagnosticDataList(
                    RobotStateViewModel(
                        RobotStateItems.MANUAL_CONTROL_ENGINE_ROTATIONS.ID,
                        diagnosticData.engineRotationsPerMinute.toString(),
                        resourcesProvider.getString(R.string.rpm)

                    )
                )
                addToDiagnosticDataList(
                    RobotStateViewModel(
                        RobotStateItems.MANUAL_CONTROL_ENGINE_TORQUE.ID,
                        diagnosticData.torqueOfEngine.toString(),
                        resourcesProvider.getString(R.string.percent)
                    )
                )
                addToDiagnosticDataList(
                    RobotStateViewModel(
                        RobotStateItems.FUEL_LEVEL.ID,
                        diagnosticData.fuelLevel.toString(),
                        resourcesProvider.getString(R.string.percent)

                    )
                )
                addToDiagnosticDataList(
                    RobotStateViewModel(
                        RobotStateItems.MANUAL_CONTROL_FUEL_RATE.ID,
                        diagnosticData.engineFuelRate,
                        resourcesProvider.getString(R.string.liter_per_hour)

                    )
                )
                addToDiagnosticDataList(
                    RobotStateViewModel(
                        RobotStateItems.MANUAL_CONTROL_OIL_TEMPERATURE.ID,
                        diagnosticData.fuelTemperature.toString(),
                        resourcesProvider.getString(R.string.celcius_degree)
                    )
                )
            } else {
                resourcesProvider.getString(R.string.no_data_status).let { noData ->
                    addToDiagnosticDataList(
                        RobotStateViewModel(
                            RobotStateItems.MANUAL_CONTROL_VOLTAGE.ID,
                            noData,
                            resourcesProvider.getString(R.string.volt)
                        )
                    )
                    addToDiagnosticDataList(
                        RobotStateViewModel(
                            RobotStateItems.MANUAL_CONTROL_ENGINE_ROTATIONS.ID,
                            noData,
                            resourcesProvider.getString(R.string.rpm)

                        )
                    )
                    addToDiagnosticDataList(
                        RobotStateViewModel(
                            RobotStateItems.MANUAL_CONTROL_ENGINE_TORQUE.ID,
                            noData,
                            resourcesProvider.getString(R.string.percent)
                        )
                    )
                    addToDiagnosticDataList(
                        RobotStateViewModel(
                            RobotStateItems.MANUAL_CONTROL_FUEL_LEVEL.ID,
                            noData,
                            resourcesProvider.getString(R.string.percent)

                        )
                    )
                    addToDiagnosticDataList(
                        RobotStateViewModel(
                            RobotStateItems.MANUAL_CONTROL_FUEL_RATE.ID,
                            noData,
                            resourcesProvider.getString(R.string.liter_per_hour)

                        )
                    )
                    addToDiagnosticDataList(
                        RobotStateViewModel(
                            RobotStateItems.MANUAL_CONTROL_OIL_TEMPERATURE.ID,
                            noData,
                            resourcesProvider.getString(R.string.celcius_degree)
                        )
                    )
                }
            }
        }
    }

    private fun addToDiagnosticDataList(diagnosticData: RobotStateViewModel) {
        val isAttributeAlreadyAdded = diagnosticDataList.value!!.any { arrtibute ->
            arrtibute.stateItemId == diagnosticData.stateItemId
        }
        if (!isAttributeAlreadyAdded)
            diagnosticDataList.value = listOf(
                *diagnosticDataList.value!!.toTypedArray(),
                RobotStateViewModel(
                    diagnosticData.stateItemId,
                    diagnosticData.stateValue,
                    diagnosticData.stateUnit
                )

            )
        else {
            diagnosticDataList.value = diagnosticDataList.value!!.map { oldAttribute ->
                if (oldAttribute.stateItemId == diagnosticData.stateItemId) {
                    RobotStateViewModel(
                        oldAttribute.stateItemId,
                        diagnosticData.stateValue,
                        oldAttribute.stateUnit
                    )
                } else {
                    oldAttribute
                }
            }
        }
    }

    private fun getJoystickMessageCounter(): Int {
        if (joystickMessageCounter.value == null || joystickMessageCounter.value == JoysticFilter.JOYSTICK_COUNTER_MAX_VALUE.value) {
            joystickMessageCounter.value = COUNTER_INITIALIZATION_VALUE
        } else {
            joystickMessageCounter.value = joystickMessageCounter.value!! + COUNTER_INCREMENT_VALUE
        }
        return joystickMessageCounter.value!!
    }

    private fun onWakeupButtonCLicked(onError: (error: String) -> Unit) = viewModelScope.launch {
        internetConnectionService.connectWithAgribot(onConnectionError = onError)
    }

    private fun onWakeupError(errorMessage: String) {
        toastMessage.postValue(errorMessage)
    }

    private fun startStopEngine() = viewModelScope.launch {
        manualModeService.startStopEngine()
    }

    private fun onTTControlButtonClicked() {
        navigationEvent.postValue(
            TTControlNavigationEvent()
        )
    }
}
