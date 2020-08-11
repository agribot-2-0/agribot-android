package eu.agribot.agribotapp.ui.fragment.toolcontrol

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.domain.service.BluetoothUtils
import eu.agribot.agribotapp.domain.service.ToolControlService

class ToolControlViewModel(
    private val toolControlService: ToolControlService,
    private val bluetoothUtils: BluetoothUtils
) : BaseViewModel() {
    val showToastEvent: MutableLiveEvent<String> = MutableLiveEvent()

    fun setSwitchesStatus(switchNumber: Int, switchValue: Int, isActive: Boolean) {

        if (bluetoothUtils.isAgribotConnect) {
            toolControlService.writeSwitchesStatus(switchNumber, switchValue, isActive)
        } else {
            showToastEvent.postValue("Agribot Disconnected")
        }
    }
}
