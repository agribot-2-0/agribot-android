package eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.toolparameters

import eu.agribot.agribotapp.base.BaseViewModel

data class ListToolParamViewModel(
    val toolParamID: Int,
    val paramName: String,
    val paramValue: Int,
    val paramUnit: String
) : BaseViewModel()
