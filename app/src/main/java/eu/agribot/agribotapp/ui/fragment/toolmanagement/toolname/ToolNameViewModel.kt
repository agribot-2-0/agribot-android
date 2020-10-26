package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolname

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.ToolParamsNavigationEvent


class ToolNameViewModel() : BaseViewModel() {
    fun onTaskNameChanged(text: CharSequence) {
    }

    fun onNextButtonClicked() {
        navigationEvent.postValue(
            ToolParamsNavigationEvent()
        )
    }
}
