package eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.ToolNameNavigationEvent

class ToolManagementViewModel : BaseViewModel() {

    fun onNewToolButtonClicked() {
        navigationEvent.postValue(
            ToolNameNavigationEvent()
        )
    }
}
