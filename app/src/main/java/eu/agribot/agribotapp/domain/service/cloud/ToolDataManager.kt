package eu.agribot.agribotapp.domain.service.cloud

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.domain.model.ToolDetails

class ToolDataManager {
    private val toolName: MutableLiveData<String> = MutableLiveData()
    private val rfid: MutableLiveData<String> = MutableLiveData()

    fun updateAllToolParameters(toolDetails: ToolDetails) {
        onUpdateRfid(toolDetails.rfid)
    }

    fun onUpdateToolName(toolName: String) {
        this.toolName.value = toolName
    }

    fun onUpdateRfid(rfid: String) {
        this.rfid.value = rfid
    }

    fun getToolName(): String {
        return if (toolName.value != null) {
            toolName.value!!
        } else {
            "null"
        }
    }

    fun getRfid(): String {
        return if (rfid.value != null) {
            rfid.value!!
        } else {
            "null"
        }
    }
}
