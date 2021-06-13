package eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data

data class TplSwitchesState(
    val activeTplId: Int,
    val frontUpTplChecked: Boolean,
    val frontBottomTplChecked: Boolean,
    val rearUpTplChecked: Boolean,
    val rearBottomTplChecked: Boolean
)
