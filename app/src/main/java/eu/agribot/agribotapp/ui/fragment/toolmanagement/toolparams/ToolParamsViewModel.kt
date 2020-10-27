package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolparams

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.ToolNavigationEvent
import eu.agribot.agribotapp.domain.model.cdb.taskprogram.taskmodel.AddToolDetails
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager


class ToolParamsViewModel(
    private val toolDataManager: ToolDataManager,
    private val cloudDatabaseService: CloudDatabaseService
) : BaseViewModel() {
//    fun onTaskNameChanged(text: CharSequence) {
//    }
//
//    fun onNextButtonClicked() {
//
//    }

    fun sendToolData() {
        postToolManagementNavigationEvent()
        cloudDatabaseService.addTool(
            toolName = toolDataManager.getToolName(),
            addToolDetails = AddToolDetails(
                rfid = toolDataManager.getRfid()
            )
        )
    }

    fun postToolManagementNavigationEvent() {
        navigationEvent.postValue(
            ToolNavigationEvent()
        )
    }
}
