package eu.agribot.agribotapp.ui.fragment.toolmanagement

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.ToolNameNavigationEvent

class ToolManagementViewModel : BaseViewModel() {

    fun onNewToolButtonClicked() {
        navigationEvent.postValue(
            ToolNameNavigationEvent()
        )
    }
}
