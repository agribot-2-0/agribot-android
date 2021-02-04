package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.rfid.RfidMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.messagecreator.BluetoothMessageCreator
import eu.agribot.agribotapp.communication.database.firestorage.DownloaderPath
import eu.agribot.agribotapp.domain.model.fromdomain.RfidParameters
import eu.agribot.agribotapp.domain.model.todomain.RfidData
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePathProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

class ToolManagementService(
    private val bluetoothMessageCreator: BluetoothMessageCreator,
    private val rfidMessageManager: RfidMessageManager,
    private val filepathProvider: FilePathProvider
    ) {

    companion object {
        const val sourceSeparator = "/"
        const val fileFormat = ".jpg"
    }


    @ExperimentalCoroutinesApi
    private val rfidDataFlow = MutableStateFlow<RfidData?>(null)
    var scope = CoroutineScope(Job() + Dispatchers.IO)

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

    fun clearRfidData() {
        rfidDataFlow.value = null
        rfidMessageManager.clearRfidDataFlow()
    }

     fun getImageFile(filename: String): File {
        return File(filepathProvider.getPhotoPath(DownloaderPath.TOOLS_PHOTO.filePath + sourceSeparator))
    }
}
