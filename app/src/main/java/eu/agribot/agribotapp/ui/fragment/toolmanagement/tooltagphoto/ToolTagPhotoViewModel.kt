package eu.agribot.agribotapp.ui.fragment.toolmanagement.tooltagphoto

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.permission.PermissionError
import eu.agribot.agribotapp.domain.permission.PermissionManager
import eu.agribot.agribotapp.domain.service.ToolManagementService
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager
import java.io.File

class ToolTagPhotoViewModel(
    private val permissionManager: PermissionManager,
    private val toolDataManager: ToolDataManager,
    private val toolManagementService: ToolManagementService,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {
    val toastEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val takePhotoEvent: MutableLiveEvent<Boolean> = MutableLiveEvent()
    val displayToolName: MutableLiveData<String> = MutableLiveData()

    init {
        displayToolName.value = String.format(
            resourcesProvider.getString(R.string.display_tool_name),
            toolDataManager.toolName
        )
    }

    fun checkPermissions(onSucess: () -> Unit) {
        permissionManager.requestPermission(onSucess, onError = this::onPermissionError)
    }

    private fun onPermissionError(permissionError: PermissionError) {
        toastEvent.postValue(permissionError.name)
    }

    fun getToolPhotoName(): String {
        return toolDataManager.getToolPhotoPathName()
    }

    fun shotToolPhoto() {
        takePhotoEvent.postValue(true)
    }

    fun getPhotoFile(): File {
        return toolManagementService.getPhotoDirectory()
    }
}
