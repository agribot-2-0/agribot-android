package eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.RecyclerConstant.DEFAULT_SLIDER_UNIT
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.RecyclerConstant.DEFAULT_SLIDER_VALUE
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.RecyclerConstant.DEFAULT_SLIDER_VALUE_MAX
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.RecyclerConstant.DEFAULT_SLIDER_VALUE_MIN

data class HitchListItemViewModel(
    val toolName: String,
    val switchNumber: Int,
    val toolStartValue: Int = DEFAULT_SLIDER_VALUE,
    val toolUnit: String = DEFAULT_SLIDER_UNIT,
    val minValue: Int = DEFAULT_SLIDER_VALUE_MIN,
    val maxValue: Int = DEFAULT_SLIDER_VALUE_MAX
) : BaseViewModel()
