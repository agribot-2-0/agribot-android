package eu.agribot.agribotapp.ui.fragment.toolmanagement.tooltagphoto

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.domain.permission.PermissionError
import eu.agribot.agribotapp.domain.permission.PermissionManager
import eu.agribot.agribotapp.domain.service.ToolManagementService
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager
import java.io.File

class ToolTagPhotoViewModel(
    private val permissionManager: PermissionManager, private val toolDataManager: ToolDataManager,
    private val toolManagementService: ToolManagementService
) : BaseViewModel() {
    val toastEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val takePhotoEvent: MutableLiveEvent<Boolean> = MutableLiveEvent()

    fun checkPermissions(onSucess: () -> Unit) {
        permissionManager.requestPermission(onSucess, onError = this::onPermissionError)
    }

    private fun onPermissionError(permissionError: PermissionError) {
        toastEvent.postValue(permissionError.name)
    }

    fun getToolPhotoName() : String{
        return toolDataManager.toolName + ".jpg"
    }

    fun getPhotoSourceFile(): File {
        return toolManagementService.getImageFile(toolDataManager.toolName)
    }

    fun shotToolPhoto() {
        takePhotoEvent.postValue(true)
    }
}