package eu.agribot.agribotapp.ui.fragment.settings.recycler.settingsaction

import eu.agribot.agribotapp.base.BaseViewModel

data class SettingsActionViewModel(
    val actionID: Int,
    val actionName: String
) : BaseViewModel()
