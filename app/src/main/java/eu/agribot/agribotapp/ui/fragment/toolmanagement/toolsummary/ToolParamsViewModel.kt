package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolsummary

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.ToolDetails
import eu.agribot.agribotapp.domain.model.fromdomain.RfidParameters
import eu.agribot.agribotapp.domain.model.todomain.RfidData
import eu.agribot.agribotapp.domain.service.ToolManagementService
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.HitchListItemDetail
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemViewModel
import eu.agribot.agribotapp.ui.fragment.toolmanagement.findtag.data.AntennaNumber
import eu.agribot.agribotapp.ui.fragment.toolmanagement.findtag.data.TagRfidFormatter
import eu.agribot.agribotapp.ui.fragment.toolmanagement.findtag.data.ToolRecylerItemId
import eu.agribot.agribotapp.ui.fragment.toolmanagement.toolsummary.recycler.ReceivedRfidDataViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ToolParamsViewModel(
    private val toolDataManager: ToolDataManager,
    private val resourcesProvider: ResourcesProvider,
    private val cloudDatabaseService: CloudDatabaseService,
    private val toolManagementService: ToolManagementService
) : BaseViewModel() {
    private val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()
    private val selectedRfidTagList: MutableLiveData<List<Int>> = MutableLiveData(listOf())
    private val toolRfidTag: MutableLiveData<String> = MutableLiveData()
    val rfidReceivedData: MutableLiveData<List<ReceivedRfidDataViewModel>> =
        MutableLiveData(listOf())
    val noRfidTagDataEvent: MutableLiveEvent<Boolean> = MutableLiveEvent()
    val isScanOperation: MutableLiveEvent<Boolean> = MutableLiveEvent()
    val paramsList: MutableLiveData<List<HitchListItemViewModel>> =
        MutableLiveData(listOf())


    fun getToolPhotoName() : String{
        return "/" + toolDataManager.toolName + ".jpg"
    }

    fun onRfidTagSelected(rfidElement: ReceivedRfidDataViewModel) {
        toolDataManager.rfid = listOf()

        rfidElement.rfidTagDataArray.forEach { rfifTagSingleByte ->
            selectedRfidTagList.value =
                listOf(
                    rfifTagSingleByte.toInt(),
                    *selectedRfidTagList.value!!.toTypedArray()
                )
        }
        toolDataManager.rfid = selectedRfidTagList.value!!
    }

    private fun addToolParamItemsToRecycler() {
        addToolToToolParamList(
            toolName = resourcesProvider.getString(R.string.v_max),
            switchNumber = ToolRecylerItemId.V_MAX.ID,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.vMax.toInt()
            )
        )
        addToolToToolParamList(
            toolName = resourcesProvider.getString(R.string.green_flow),
            switchNumber = ToolRecylerItemId.GREEN.ID,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.green.toInt()
            )
        )
        addToolToToolParamList(
            toolName = resourcesProvider.getString(R.string.yellow_flow),
            switchNumber = ToolRecylerItemId.YELLOW.ID,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.yellow.toInt()
            )
        )
        addToolToToolParamList(
            toolName = resourcesProvider.getString(R.string.blue_flow),
            switchNumber = ToolRecylerItemId.BLUE.ID,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.blue.toInt()
            )
        )
        addToolToToolParamList(
            toolName = resourcesProvider.getString(R.string.pto),
            switchNumber = ToolRecylerItemId.PTO.ID,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.pto.toInt()
            )
        )
        addToolToToolParamList(
            toolName = resourcesProvider.getString(R.string.tpl_push_pull),
            switchNumber = ToolRecylerItemId.TPL_PUSH_PULL.ID,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.tplPushPull.toInt()
            )
        )
        addToolToToolParamList(
            toolName = resourcesProvider.getString(R.string.tpl_up_down),
            switchNumber = ToolRecylerItemId.TPL_UP_DOWN.ID,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.tplUpDown.toInt()
            )
        )
    }

    fun onHitchParametersChanged(
        sliderNumber: Int,
        sliderValue: Int
    ) {
        // TODO when switch is activated send data via bluetooth in order to control tools
        when (sliderNumber) {
            ToolRecylerItemId.PTO.ID -> {
                toolDataManager.pto = sliderValue.toDouble()
            }
            ToolRecylerItemId.TPL_UP_DOWN.ID -> {
                toolDataManager.tplUpDown = sliderValue.toDouble()
            }
            ToolRecylerItemId.TPL_PUSH_PULL.ID -> {
                toolDataManager.tplPushPull = sliderValue.toDouble()
            }
            ToolRecylerItemId.GREEN.ID -> {
                toolDataManager.green = sliderValue.toDouble()
            }
            ToolRecylerItemId.YELLOW.ID -> {
                toolDataManager.yellow = sliderValue.toDouble()
            }
            ToolRecylerItemId.BLUE.ID -> {
                toolDataManager.blue = sliderValue.toDouble()
            }
            ToolRecylerItemId.V_MAX.ID -> {
                toolDataManager.vMax = sliderValue.toDouble()
            }
        }
    }

    private fun addToolToToolParamList(
        toolName: String,
        switchNumber: Int,
        hitchListItemDetail: HitchListItemDetail
    ) {
        val isToolAlreadyAdded = paramsList.value!!.any {
            it.toolName == toolName
        }

        if (!isToolAlreadyAdded) {
            paramsList.value = listOf(
                HitchListItemViewModel(
                    toolName = toolName,
                    switchNumber = switchNumber,
                    hitchListItemDetail = hitchListItemDetail
                ), *paramsList.value!!.toTypedArray()
            )
        } else {
            paramsList.value = paramsList.value!!.map { oldAttribute ->
                if (oldAttribute.toolName == toolName) {
                    HitchListItemViewModel(
                        toolName = toolName,
                        switchNumber = switchNumber,
                        hitchListItemDetail = hitchListItemDetail
                    )
                } else {
                    oldAttribute
                }
            }
        }
    }

    fun sendToolData() {
        postToolManagementNavigationEvent()
        cloudDatabaseService.addTool(
            toolName = toolDataManager.toolName,
            toolDetails = ToolDetails(
                rfid = toolDataManager.rfid,
                pto = toolDataManager.pto,
                tplPushPull = toolDataManager.tplPushPull,
                tplUpDown = toolDataManager.tplUpDown,
                blue = toolDataManager.blue,
                yellow = toolDataManager.yellow,
                green = toolDataManager.green,
                vMax = toolDataManager.vMax
            ),
            onSucess = this::onSuccess
        )
    }

    fun createToolsList() {
        cloudDatabaseService.getToolDetails(
            toolName = toolDataManager.toolName,
            onGetToolDetails = this::onToolUpdate,
            onGetToolDetailsError = this::postToastErrorMessageEvent
        )
        addToolParamItemsToRecycler()
    }

    private fun onToolUpdate(toolDetails: ToolDetails) {
        toolDataManager.updateAllToolParameters(toolDetails)
        toolRfidTag.value = toolDataManager.rfid.toString()
        addToolParamItemsToRecycler()
    }

    private fun postToolManagementNavigationEvent() {
        navigationEvent.postValue(
            ToolNavigationEvent()
        )
    }

    private fun postToastErrorMessageEvent(errorMessage: String) {
        toastMessageEvent.postValue(errorMessage)
    }

    private fun onSuccess() {
        toastMessageEvent.postValue(resourcesProvider.getString(R.string.add_tool_success))
    }

    fun startRfidScan() {
        isScanOperation.postValue(true)
        clearRfidData()
    }

    @ExperimentalCoroutinesApi
    fun scanRfid(antennaNumber: Int) = viewModelScope.launch {
        toolManagementService.scanRfidTags(
            RfidParameters(
                comNumber = 1,
                antennaNumber = antennaNumber
            )
        )
        getRfidData()
    }

    @ExperimentalCoroutinesApi
    private fun getRfidData() {
        viewModelScope.launch {
            toolManagementService.observeRfidDataFlow().collect { receivedRfidData ->
                if (receivedRfidData != null) {
                    if (receivedRfidData.antennaNumber == AntennaNumber.NO_ANTENNA.number) {
                        noRfidTagDataEvent.postValue(true)
                    } else {
                        onGotRfidData(receivedRfidData)
                    }
                }
            }
        }
    }

    private fun onGotRfidData(rfidData: RfidData) {
        val isTagalreadyAdded = rfidReceivedData.value!!.any { foundTool ->
            foundTool.rfidTagDataArray.contentEquals(rfidData.tagIDNumber)
        }
        if (!isTagalreadyAdded) {
            rfidReceivedData.value = listOf(
                ReceivedRfidDataViewModel(
                    antennaNumber = getAntennaName(rfidData.antennaNumber),
                    rfidTagData = getDisplayRfidTag(rfidData.tagIDNumber),
                    rfidTagDataArray = rfidData.tagIDNumber
                ), *rfidReceivedData.value!!.toTypedArray()
            )
        }
    }

    private fun getDisplayRfidTag(tagIDArray: ByteArray): String {
        var rfidTagHexString = ""
        tagIDArray.reversedArray().forEach {
            rfidTagHexString += String.format(
                TagRfidFormatter.NUMBER_FORMAT.formatElement,
                it
            ) + TagRfidFormatter.NUMBER_SEPARATOR.formatElement
        }
        return rfidTagHexString
    }

    private fun getAntennaName(antenaNumber: Int): String {
        return when (antenaNumber) {

            AntennaNumber.FRONT_ANTENNA.number -> {
                resourcesProvider.getString(R.string.front_antenna)
            }

            AntennaNumber.BACK_ANTENNA.number -> {
                resourcesProvider.getString(R.string.back_antenna)
            }

            else -> resourcesProvider.getString(R.string.both_antenna)
        }
    }

    private fun clearRfidData() {
        rfidReceivedData.value = listOf()
        toolManagementService.clearRfidData()
    }
}
