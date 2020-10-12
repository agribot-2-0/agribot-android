package eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler

import eu.agribot.agribotapp.base.BaseViewModel

data class HitchListItemViewModel(
    val toolName: String,
    val switchNumber: Int
) : BaseViewModel()
