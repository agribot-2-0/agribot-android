package eu.agribot.agribotapp.domain.service

import com.google.firebase.crashlytics.FirebaseCrashlytics
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.rfid.RfidMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.messagecreator.BluetoothMessageCreator
import eu.agribot.agribotapp.domain.model.fromdomain.RfidParameters
import eu.agribot.agribotapp.domain.model.todomain.RfidData
import eu.agribot.agribotapp.domain.service.cloud.CloudStorageService
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePathProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class ToolManagementService(
    private val bluetoothMessageCreator: BluetoothMessageCreator,
    private val rfidMessageManager: RfidMessageManager,
    private val cloudStorageService: CloudStorageService,
    private val filePathProvider: FilePathProvider
) {

    companion object {
        const val TOOL_PHOTO_FILE_EXTENSION = ".jpg"
    }

    @ExperimentalCoroutinesApi
    private val rfidDataFlow = MutableStateFlow<RfidData?>(null)
    private var scope = CoroutineScope(Job() + Dispatchers.IO)

    @ExperimentalCoroutinesApi
    fun scanRfidTags(rfidParameters: RfidParameters) {
        bluetoothMessageCreator.createRfifRequestMessage(rfidParameters.toCommunicationModel())
        getRfidData()
    }

    @ExperimentalCoroutinesApi
    fun observeRfidDataFlow(): MutableStateFlow<RfidData?> {
        return rfidDataFlow
    }

    @ExperimentalCoroutinesApi
    private fun getRfidData() {
        scope.launch {
            rfidMessageManager.observeRfidMessage().collect {
                if (it != null) {
                    rfidDataFlow.value = it.toDomainModel()
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun clearRfidData() {
        rfidDataFlow.value = null
        rfidMessageManager.clearRfidDataFlow()
    }

    fun getPhotoDirectory(): File {
        return File(filePathProvider.getToolPhotoDirectory())
    }

    fun uploadToolPhotoToCloud(
        toolName: String,
        toolPhotoSource: String,
        onSucess: () -> Unit,
        onError: (error: String) -> Unit
    ) {
        cloudStorageService.uploadToolPhotoFile(
            toolPhotoSource,
            toolName + TOOL_PHOTO_FILE_EXTENSION,
            onSucess,
            onError
        )
    }

    fun getToolPhotoToDisplay(toolName: String): File {
        if (File(getPhotoDirectory().absolutePath + toolName).exists()) {
            return getPhotoDirectory()
        } else {
            cloudStorageService.downloadToolPhotoFile(
                getPhotoDirectory().absolutePath,
                toolName,
                onDownloadToolPhotoSuccess = this::onSuccess,
                onDownloadToolPhotoError = this::onError
            )
        }
        return getPhotoDirectory()
    }

    private fun onSuccess(): File {
        return getPhotoDirectory()
    }

    // TODO MAP FIRESTORAGE ERROR, currently work around log
    private fun onError(error: Throwable) {
        FirebaseCrashlytics.getInstance().log("ToolManagement service Exception : $error")
        FirebaseCrashlytics.getInstance().recordException(error)
        Timber.e("Download error %s", error)
    }
}
