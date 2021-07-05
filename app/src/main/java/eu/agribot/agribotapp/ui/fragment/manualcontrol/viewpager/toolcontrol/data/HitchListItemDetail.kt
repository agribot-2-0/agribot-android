package eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data

import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.RecyclerConstant

data class HitchListItemDetail(
    val toolStartValue: Int = RecyclerConstant.DEFAULT_SLIDER_VALUE,
    val toolUnit: String = RecyclerConstant.DEFAULT_SLIDER_UNIT,
    val minValue: Int = RecyclerConstant.DEFAULT_SLIDER_VALUE_MIN,
    val maxValue: Int = RecyclerConstant.DEFAULT_SLIDER_VALUE_MAX
)
