package eu.agribot.agribotapp.ui.fragment.toolcontrol.data

data class HitchListItemDetail(
    val toolStartValue: Int = RecyclerConstant.DEFAULT_SLIDER_VALUE,
    val toolUnit: String = RecyclerConstant.DEFAULT_SLIDER_UNIT,
    val minValue: Int = RecyclerConstant.DEFAULT_SLIDER_VALUE_MIN,
    val maxValue: Int = RecyclerConstant.DEFAULT_SLIDER_VALUE_MAX
)
