package eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.HitchListItemDetail

data class HitchListItemViewModel(
    val toolName: String,
    val switchNumber: Int,
    val hitchListItemDetail: HitchListItemDetail = HitchListItemDetail(
        toolStartValue = 0,
        maxValue = 100,
        minValue = 0,
        toolUnit = "RPM"
    )
) : BaseViewModel()
