package eu.agribot.agribotapp.domain.service.cloud

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.domain.model.cdb.ToolDetails

class ToolDataManager {
    private val toolName: MutableLiveData<String> = MutableLiveData()
    private val rfid: MutableLiveData<Int> = MutableLiveData()
    private val pto: MutableLiveData<Double> = MutableLiveData()
    private val tplUpDown: MutableLiveData<Double> = MutableLiveData()
    private val tplPushPull: MutableLiveData<Double> = MutableLiveData()
    private val green: MutableLiveData<Double> = MutableLiveData()
    private val yellow: MutableLiveData<Double> = MutableLiveData()
    private val blue: MutableLiveData<Double> = MutableLiveData()
    private val vMax: MutableLiveData<Double> = MutableLiveData()

    fun updateAllToolParameters(toolDetails: ToolDetails) {
        onUpdateRfid(toolDetails.rfid)
        onUpdatePto(toolDetails.pto)
        onUpdateTplUpDown(toolDetails.tpl_up_down)
        onUpdateTplPushPull(toolDetails.tpl_push_pull)
        onUpdateGreen(toolDetails.green)
        onUpdateBlue(toolDetails.blue)
        onUpdateYellow(toolDetails.yellow)
        onUpdateVMax(toolDetails.v_max)
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

    fun onUpdateTplPushPull(tplPushPull: Double) {
        this.tplPushPull.value = tplPushPull
    }

    fun getTplPushPull(): Double {
        return if (tplPushPull.value != null) {
            tplPushPull.value!!
        } else {
            0.0
        }
    }

    fun onUpdateGreen(green: Double) {
        this.green.value = green
    }

    fun getGreen(): Double {
        return if (green.value != null) {
            green.value!!
        } else {
            0.0
        }
    }

    fun onUpdateYellow(yellow: Double) {
        this.yellow.value = yellow
    }

    fun getYellow(): Double {
        return if (yellow.value != null) {
            yellow.value!!
        } else {
            0.0
        }
    }

    fun onUpdateBlue(blue: Double) {
        this.blue.value = blue
    }

    fun getBlue(): Double {
        return if (blue.value != null) {
            blue.value!!
        } else {
            0.0
        }
    }

    fun onUpdateVMax(vMax: Double) {
        this.vMax.value = vMax
    }

    fun getVMax(): Double {
        return if (vMax.value != null) {
            vMax.value!!
        } else {
            0.0
        }
    }
}
