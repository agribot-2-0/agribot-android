package eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler

import eu.agribot.agribotapp.base.BaseViewModel

data class RobotStateViewModel(
    val stateItemId: Int,
    val stateValue: String,
    val stateUnit: String
) : BaseViewModel()
