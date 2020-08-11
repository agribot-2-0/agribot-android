package eu.agribot.agribotapp.ui.fragment.toolcontrol

import eu.agribot.agribotapp.base.BaseViewModel

<<<<<<< HEAD
class ToolControlViewModel : BaseViewModel()
=======
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
>>>>>>> 0bd8dbd... static code analyze fix
