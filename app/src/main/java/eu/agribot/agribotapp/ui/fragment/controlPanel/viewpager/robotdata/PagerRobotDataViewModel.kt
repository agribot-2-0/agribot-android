package eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.robotdata

import android.util.Log
import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.state.RobotEngine
import eu.agribot.agribotapp.domain.model.cdb.state.RobotHydraulics
import eu.agribot.agribotapp.domain.model.cdb.state.RobotTask
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateViewModel
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.items.RobotStateItems

class PagerRobotDataViewModel(
    val cloudDatabaseService: CloudDatabaseService,
    val resourcesProvider: ResourcesProvider
) : BaseViewModel() {
    val robotDataStateList: MutableLiveData<List<RobotStateViewModel>> = MutableLiveData(listOf())
    val toastEvent: MutableLiveEvent<String> = MutableLiveEvent()

    fun getRobotStateData() {
        getRobotEngineData()
        getRobotHydraulicsData()
        getRobotTaskData()
    }

    private fun getRobotEngineData() {
        cloudDatabaseService.getRobotStateEngine(
            onSuccess = this::onGotRobotEngineData,
            onError = this::onGetRobotDataError
        )
    }

    private fun getRobotHydraulicsData() {
        cloudDatabaseService.getRobotStateHydraulics(
            onSuccess = this::onGotRobotHydraulicsData,
            onError = this::onGetRobotDataError
        )
    }

    private fun getRobotTaskData() {
        cloudDatabaseService.getRobotStateTask(
            onSuccess = this::onGotRobotTaskData,
            onError = this::onGetRobotDataError
        )
    }

    private fun onGetRobotDataError(errorText: String) {
        toastEvent.postValue(errorText)
    }
    // TODO ADD ESTIMATED TASK END TIME, && ROBOT SPEED
    private fun onGotRobotEngineData(engine: RobotEngine) {
        addParameterToRobotDataStateList(
            RobotStateViewModel(
                RobotStateItems.FUEL_LEVEL.ID,
                engine.fuel_level.toString(),
                resourcesProvider.getString(R.string.percent)
            )
        )
    }

    private fun onGotRobotHydraulicsData(hydraulics: RobotHydraulics) {
        Log.d("hydraulic", "robot engine : " + hydraulics)
    }

    private fun onGotRobotTaskData(task: RobotTask) {
        addParameterToRobotDataStateList(
            RobotStateViewModel(
                RobotStateItems.TASK_PROGRESS.ID,
                task.progress.toString(),
                resourcesProvider.getString(R.string.percent)
            )
        )
    }

    private fun addParameterToRobotDataStateList(robotStateParameter: RobotStateViewModel) {
        val isAttributeAlreadyAdded = robotDataStateList.value!!.any { arrtibute ->
            arrtibute.stateItemId == robotStateParameter.stateItemId
        }
        if (!isAttributeAlreadyAdded)
            robotDataStateList.value = listOf(
                RobotStateViewModel(
                    robotStateParameter.stateItemId,
                    robotStateParameter.stateValue,
                    robotStateParameter.stateUnit
                ),
                *robotDataStateList.value!!.toTypedArray()
            )
        else {
            robotDataStateList.value = robotDataStateList.value!!.map { oldAttribute ->
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
