package eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.recycler

import eu.agribot.agribotapp.base.BaseViewModel

data class RobotStateViewModel(
    val stateItemId: Int,
    val stateValue: String,
    val stateUnit: String
) : BaseViewModel()