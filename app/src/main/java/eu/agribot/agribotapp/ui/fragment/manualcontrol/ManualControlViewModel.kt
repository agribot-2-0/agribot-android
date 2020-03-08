package eu.agribot.agribotapp.ui.fragment.manualcontrol

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent

class ManualControlViewModel : BaseViewModel() {

    fun onToolControlClicked() {
        navigationEvent.postValue(
            ToolControlNavigationEvent()
        )
    }
}
