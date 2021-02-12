package eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.robotdata

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.recycler.RobotStateViewModel

class PagerRobotDataViewModel : BaseViewModel() {
    val robotDataStateList: MutableLiveData<List<RobotStateViewModel>> = MutableLiveData(listOf())










    private fun addParameterToRobotDataStateList(robotStateParameter: RobotStateViewModel) {
        val isAttributeAlreadyAdded = robotDataStateList.value!!.any { arrtibute ->
            arrtibute.stateItemId == robotStateParameter.stateItemId
        }
        if (!isAttributeAlreadyAdded)
            robotDataStateList.value = listOf(
                RobotStateViewModel(
                    robotStateParameter.stateItemId,
                    robotStateParameter.stateValue,
                    robotStateParameter.stateValue
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

