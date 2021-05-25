package eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.currentstate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.UserRoleType
import eu.agribot.agribotapp.domain.model.todomain.DiagnosticData
import eu.agribot.agribotapp.domain.model.todomain.UserRole
import eu.agribot.agribotapp.domain.service.AppStorageService
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateViewModel
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.items.RobotStateItems
import eu.agribot.agribotapp.ui.fragment.manualcontrol.recycler.actionbottombar.ActionID
import eu.agribot.agribotapp.ui.fragment.manualcontrol.recycler.actionbottombar.ManualActionsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale

class ManualControlCurrentStateViewModel(
    private val manualModeService: ManualModeService,
    private val resourcesProvider: ResourcesProvider,
    private val appStorageService: AppStorageService
) : BaseViewModel() {
    val diagnosticDataList: MutableLiveData<List<RobotStateViewModel>> =
        MutableLiveData(listOf())
    val toastMessage: MutableLiveEvent<String> = MutableLiveEvent()
    val actionsList: MutableLiveData<List<ManualActionsViewModel>> = MutableLiveData(listOf())

    fun onActionClicked(actionID: Int) {
        when (actionID) {
            ActionID.RUN_ENGINE.action -> {
                startStopEngine()
            }

            ActionID.OVERRIDE_MODE.action -> {
                onTTControlButtonClicked()
            }
            ActionID.SEND_MANUAL_INIT_MESSAGE.action -> {
                onManualModeButtonCLicked()
            }
            ActionID.SAFE_MODE.action -> {
                onSafeButtonClicked()
            }
        }
    }

    private fun onSafeButtonClicked() {
        manualModeService.sendSafeModeMessage()
    }

    private fun onManualModeButtonCLicked() {
        manualModeService.sendManualModeInitMessage()
    }

    private fun createActionsList(withAdmin: Boolean) {
        if (withAdmin) {
            actionsList.value = listOf(
                ManualActionsViewModel(
                    ActionID.RUN_ENGINE.action,
                    resourcesProvider.getString(R.string.start_stop_engine)
                ),
                ManualActionsViewModel(
                    ActionID.OVERRIDE_MODE.action,
                    resourcesProvider.getString(R.string.override_button)
                ),
                ManualActionsViewModel(
                    ActionID.SEND_MANUAL_INIT_MESSAGE.action,
                    resourcesProvider.getString(R.string.manual_mode_button)
                ),
                ManualActionsViewModel(
                    ActionID.SAFE_MODE.action,
                    resourcesProvider.getString(R.string.safe_mode_button)
                )
            )
        } else {
            actionsList.value = listOf(
                ManualActionsViewModel(
                    ActionID.RUN_ENGINE.action,
                    resourcesProvider.getString(R.string.start_stop_engine)
                )
            )
        }
    }

    fun getStreamUrl(): String = appStorageService.getStreamUrl()

    fun prepareDiagnosticData() {
        diagnosticDataList.value = listOf(
            RobotStateViewModel(
                RobotStateItems.MANUAL_CONTROL_VOLTAGE.ID,
                resourcesProvider.getString(R.string.no_data_status),
                resourcesProvider.getString(R.string.volt)
            ),

            RobotStateViewModel(
                RobotStateItems.MANUAL_CONTROL_ENGINE_ROTATIONS.ID,
                resourcesProvider.getString(R.string.no_data_status),
                resourcesProvider.getString(R.string.rpm)

            ),
            RobotStateViewModel(
                RobotStateItems.MANUAL_CONTROL_ENGINE_TORQUE.ID,
                resourcesProvider.getString(R.string.no_data_status),
                resourcesProvider.getString(R.string.percent)
            ),

            RobotStateViewModel(
                RobotStateItems.MANUAL_CONTROL_FUEL_LEVEL.ID,
                resourcesProvider.getString(R.string.no_data_status),
                resourcesProvider.getString(R.string.percent)

            ),
            RobotStateViewModel(
                RobotStateItems.MANUAL_CONTROL_FUEL_RATE.ID,
                resourcesProvider.getString(R.string.no_data_status),
                resourcesProvider.getString(R.string.liter_per_hour)

            ),
            RobotStateViewModel(
                RobotStateItems.MANUAL_CONTROL_OIL_TEMPERATURE.ID,
                resourcesProvider.getString(R.string.no_data_status),
                resourcesProvider.getString(R.string.celcius_degree)
            )
        )
    }

    @ExperimentalCoroutinesApi
    fun getDiagnosticData() = viewModelScope.launch {
        manualModeService.observeDiagnosticData().collect { diagnosticData ->
            if (diagnosticData != null) {
                logDiagnosticData(diagnosticData)
            }
            if (diagnosticData != null) {

                if (diagnosticData.batteryVoltage != INVALID_DECIMAL_NUMBER) {
                    addToDiagnosticDataList(
                        RobotStateViewModel(
                            RobotStateItems.MANUAL_CONTROL_VOLTAGE.ID,
                            String.format("%.1f", diagnosticData.batteryVoltage),
                            resourcesProvider.getString(R.string.volt)
                        )
                    )
                }
                if (diagnosticData.engineRotationsPerMinute != INVALID_INTEGER_NUMBER) {
                    addToDiagnosticDataList(
                        RobotStateViewModel(
                            RobotStateItems.MANUAL_CONTROL_ENGINE_ROTATIONS.ID,
                            diagnosticData.engineRotationsPerMinute.toString(),
                            resourcesProvider.getString(R.string.rpm)

                        )
                    )
                }
                if (diagnosticData.torqueOfEngine != INVALID_INTEGER_NUMBER) {
                    addToDiagnosticDataList(
                        RobotStateViewModel(
                            RobotStateItems.MANUAL_CONTROL_ENGINE_TORQUE.ID,
                            diagnosticData.torqueOfEngine.toString(),
                            resourcesProvider.getString(R.string.percent)
                        )
                    )
                }

                if (diagnosticData.fuelLevel != INVALID_INTEGER_NUMBER) {
                    addToDiagnosticDataList(
                        RobotStateViewModel(
                            RobotStateItems.MANUAL_CONTROL_FUEL_LEVEL.ID,
                            diagnosticData.fuelLevel.toString(),
                            resourcesProvider.getString(R.string.liter_per_hour)

                        )
                    )
                }

                if (diagnosticData.engineFuelRate != INVALID_DECIMAL_NUMBER) {
                    addToDiagnosticDataList(
                        RobotStateViewModel(
                            RobotStateItems.MANUAL_CONTROL_FUEL_RATE.ID,
                            String.format("%.1f", diagnosticData.engineFuelRate),
                            resourcesProvider.getString(R.string.liter_per_hour)

                        )
                    )
                }
                if (diagnosticData.hydraulicOilTemperature != INVALID_INTEGER_NUMBER) {
                    addToDiagnosticDataList(
                        RobotStateViewModel(
                            RobotStateItems.MANUAL_CONTROL_OIL_TEMPERATURE.ID,
                            diagnosticData.hydraulicOilTemperature.toString(),
                            resourcesProvider.getString(R.string.celcius_degree)

                        )
                    )
                }
            }
        }
    }

    private fun logDiagnosticData(diagnosticData: DiagnosticData) {
        Timber.d(
            String.format(
                resourcesProvider.getString(R.string.diagnostic_data_log),
                diagnosticData.torqueOfEngine.toString(),
                diagnosticData.engineFuelRate,
                diagnosticData.fuelLevel.toString(),
                diagnosticData.engineRotationsPerMinute.toString(),
                diagnosticData.batteryVoltage,
                diagnosticData.hydraulicOilTemperature
            )
        )
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

    private fun startStopEngine() = viewModelScope.launch {
        manualModeService.startStopEngine()
    }

    private fun onTTControlButtonClicked() {
        manualModeService.onTTControlEvent(true)
    }

    fun getUserRole() {
        manualModeService.getUserRole(
            onGotUserRole = this::onGotUserRole,
            onDatabaseError = this::onDatabaseError
        )
    }

    private fun onGotUserRole(userRole: UserRole) {
        when (userRole.role) {
            UserRoleType.USER.name.toLowerCase(Locale.ROOT) -> {
                createActionsList(false)
            }
            UserRoleType.ADMIN.name.toLowerCase(Locale.ROOT) -> {
                createActionsList(true)
            }
        }
    }

    private fun onDatabaseError(error: String) {
        Timber.e(error)
    }

    companion object {

        const val INVALID_INTEGER_NUMBER = -1

        const val INVALID_DECIMAL_NUMBER = 0.0
    }
}
