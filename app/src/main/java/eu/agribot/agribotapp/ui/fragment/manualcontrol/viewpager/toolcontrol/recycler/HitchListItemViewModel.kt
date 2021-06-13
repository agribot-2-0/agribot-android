package eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.recycler

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.HitchListItemDetail
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.RecyclerConstant

data class HitchListItemViewModel(
    val toolName: String,
    val switchNumber: Int,
    val isNewToolControl :  Boolean = false,
    val progressValue : String = "",
    val encoderValue: String = "",
    val hitchListItemDetail: HitchListItemDetail = HitchListItemDetail(
        toolStartValue = RecyclerConstant.DEFAULT_SLIDER_VALUE,
        maxValue = RecyclerConstant.DEFAULT_SLIDER_VALUE_MAX,
        minValue = RecyclerConstant.DEFAULT_SLIDER_VALUE_MIN,
        toolUnit = RecyclerConstant.DEFAULT_SLIDER_UNIT
    )
) : BaseViewModel()
