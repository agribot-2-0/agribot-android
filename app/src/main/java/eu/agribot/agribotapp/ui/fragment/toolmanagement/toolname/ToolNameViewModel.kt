package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolname

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.ToolParamsNavigationEvent
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager

class ToolNameViewModel(
    private val toolDataManager: ToolDataManager

) : BaseViewModel() {
    val toolName: MutableLiveData<String> = MutableLiveData()
    val toastEvent: MutableLiveEvent<String> = MutableLiveEvent()

    fun onTaskNameChanged(text: CharSequence) {
        toolName.value = text.toString()
    }

    fun onNextButtonClicked() {
        if (toolName.value != null) {
            toolDataManager.onUpdateToolName(toolName.value.toString())
            navigationEvent.postValue(
                ToolParamsNavigationEvent()
            )
        } else {
            toastEvent.postValue("No tool name")
        }
    }
}
