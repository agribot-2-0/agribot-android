package eu.agribot.agribotapp.ui.fragment.manualcontrol

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.domain.service.RemoteControlService

class ManualControlViewModel(
    private val remoteControlService: RemoteControlService
) : BaseViewModel() {

    fun onToolControlClicked() {
        navigationEvent.postValue(
            ToolControlNavigationEvent()
        )
    }
}
