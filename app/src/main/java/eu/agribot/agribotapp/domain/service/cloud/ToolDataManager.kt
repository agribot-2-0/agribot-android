package eu.agribot.agribotapp.domain.service.cloud

import android.util.Log
import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.domain.model.cdb.ToolDetails

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

    fun getToolName(): String {
        return if (toolName.value != null) {
            toolName.value!!
        } else {
            "null"
        }
    }

    fun onUpdateRfid(rfid: Int) {
        this.rfid.value = rfid
    }

    fun getRfid(): Int {
        return if (rfid.value != null) {
            rfid.value!!
        } else {
            0
        }
    }

    fun onUpdatePto(pto: Double) {
        this.pto.value = pto
    }

    fun getPto(): Double {
        return if (pto.value != null) {
            pto.value!!
        } else {
            0.0
        }
    }

    fun onUpdateTplUpDown(tplUpDown: Double) {
        this.tplUpDown.value = tplUpDown
    }

    fun getTplUpDown(): Double {
        return if (tplUpDown.value != null) {
            tplUpDown.value!!
        } else {
            0.0
        }
    }
}
