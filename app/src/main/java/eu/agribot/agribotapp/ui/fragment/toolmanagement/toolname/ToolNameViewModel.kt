package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolname

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.ToolParamsNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.ToolDetails
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager

class ToolNameViewModel(
    private val toolDataManager: ToolDataManager,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {

    val toolName: MutableLiveData<String> = MutableLiveData()
    val toastEvent: MutableLiveEvent<String> = MutableLiveEvent()

    fun onTaskNameChanged(text: CharSequence) {
        toolName.value = text.toString()
    }

    fun onNextButtonClicked() {
        if (toolName.value != null) {
            toolDataManager.updateAllToolParameters(
                ToolDetails()
            )
            toolDataManager.toolName = toolName.value.toString()
            navigationEvent.postValue(
                ToolParamsNavigationEvent()
            )
        } else {
            toastEvent.postValue(resourcesProvider.getString(R.string.no_tool_name))
        }
    }
}
