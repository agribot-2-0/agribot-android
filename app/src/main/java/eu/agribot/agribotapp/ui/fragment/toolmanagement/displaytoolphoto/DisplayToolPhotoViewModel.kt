package eu.agribot.agribotapp.ui.fragment.toolmanagement.displaytoolphoto

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.ShotToolPhotoNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNavigationEvent
import eu.agribot.agribotapp.domain.service.ToolManagementService
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager
import timber.log.Timber
import java.lang.RuntimeException

class DisplayToolPhotoViewModel(
    private val toolDataManager: ToolDataManager,
    private val toolManagementService: ToolManagementService
) : BaseViewModel() {

    fun getToolPhotoDirection(): String {
        return toolManagementService.getToolPhotoToDisplay(toolDataManager.getToolPhotoPathName()).absolutePath + toolDataManager.getToolPhotoPathName()
    }

    fun onPhotoRepeatButtonClicked() {
        navigationEvent.postValue(ShotToolPhotoNavigationEvent())
    }

    fun onNextButtonClicked() {
        toolManagementService.uploadToolPhotoToCloud(
            toolName = toolDataManager.toolName,
            toolPhotoSource = getToolPhotoDirection(),
            onSucess = this::onUploadPhotoSucess,
            onError = this::onError
        )
    }

    private fun onError(error: String) {
        toolManagementService.logExceptionFromViewModel(message = "Tool photo exception ", exception = RuntimeException("Display photo ViewModel Exception"))
        Timber.e(String.format("Tool photo exception %s", error))
    }

    private fun onUploadPhotoSucess() {
        navigationEvent.postValue(ToolNavigationEvent())
    }
}
