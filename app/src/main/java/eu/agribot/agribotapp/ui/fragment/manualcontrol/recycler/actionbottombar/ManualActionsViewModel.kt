package eu.agribot.agribotapp.ui.fragment.manualcontrol.recycler.actionbottombar

import eu.agribot.agribotapp.base.BaseViewModel

data class ManualActionsViewModel(
    val actionID: Int,
    val actionName: String
) : BaseViewModel()
