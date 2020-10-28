package eu.agribot.agribotapp.domain.service.cloud

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.domain.model.ToolDetails

class ToolDataManager {
    private val toolName: MutableLiveData<String> = MutableLiveData()
    private val rfid: MutableLiveData<Int> = MutableLiveData()
    private val pto: MutableLiveData<Double> = MutableLiveData()
    private val tplUpDown: MutableLiveData<Double> = MutableLiveData()
    private val tplPushPull: MutableLiveData<Double> = MutableLiveData()


    fun updateAllToolParameters(toolDetails: ToolDetails) {
        onUpdateRfid(toolDetails.rfid)
        onUpdatePto(toolDetails.pto)
    }

    fun onUpdateToolName(toolName: String) {
        this.toolName.value = toolName
    }

    fun onUpdateRfid(rfid: Int) {
        this.rfid.value = rfid
    }

    fun onUpdatePto(pto: Double) {
        this.pto.value = pto
    }

    fun getToolName(): String {
        return if (toolName.value != null) {
            toolName.value!!
        } else {
            "null"
        }
    }

    fun getRfid(): Int {
        return if (rfid.value != null) {
            rfid.value!!
        } else {
            0
        }
    }

    fun getPto(): Double {
        return if (pto.value != null) {
            pto.value!!
        } else {
            0.0
        }
    }
}
