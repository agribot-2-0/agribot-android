package eu.agribot.agribotapp.ui.fragment.toolmanagement.findtag

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.ShotToolPhotoNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.fromdomain.RfidParameters
import eu.agribot.agribotapp.domain.model.todomain.RfidData
import eu.agribot.agribotapp.domain.service.ToolManagementService
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager
import eu.agribot.agribotapp.ui.fragment.toolmanagement.findtag.data.AntennaNumber
import eu.agribot.agribotapp.ui.fragment.toolmanagement.findtag.data.TagRfidFormatter
import eu.agribot.agribotapp.ui.fragment.toolmanagement.toolparameters.recycler.ReceivedRfidDataViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FindToolTagViewModel(
    private val toolDataManager: ToolDataManager,
    private val resourcesProvider: ResourcesProvider,
    private val toolManagementService: ToolManagementService
) : BaseViewModel() {
    val toastEvent: MutableLiveEvent<String> = MutableLiveEvent()
    private val selectedRfidTagList: MutableLiveData<List<Int>> = MutableLiveData(listOf())
    val rfidReceivedData: MutableLiveData<List<ReceivedRfidDataViewModel>> =
        MutableLiveData(listOf())
    val noRfidTagDataEvent: MutableLiveEvent<Boolean> = MutableLiveEvent()
    val isScanOperation: MutableLiveEvent<Boolean> = MutableLiveEvent()
    val toolName: MutableLiveData<String> = MutableLiveData()

    init {
        toolName.value = String.format(
            resourcesProvider.getString(R.string.display_tool_name),
            toolDataManager.toolName
        )
    }

    fun onNextButtonClicked() {
        if (toolDataManager.rfid != toolDataManager.noRfidTag) {
            navigationEvent.postValue(ShotToolPhotoNavigationEvent())
        } else {
            navigationEvent.postValue(ShotToolPhotoNavigationEvent())
        }
    }

    @ExperimentalCoroutinesApi
    fun startRfidScan() {
        isScanOperation.postValue(true)
        clearRfidData()
    }

    @ExperimentalCoroutinesApi
    fun scanRfid(antennaNumber: Int) = viewModelScope.launch {
        toolManagementService.scanRfidTags(
            RfidParameters(
                comNumber = DEFAULT_COM_NUMBER,
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

    @ExperimentalCoroutinesApi
    private fun clearRfidData() {
        rfidReceivedData.value = listOf()
        toolManagementService.clearRfidData()
    }

    companion object {
        const val DEFAULT_COM_NUMBER = 1
    }
}
