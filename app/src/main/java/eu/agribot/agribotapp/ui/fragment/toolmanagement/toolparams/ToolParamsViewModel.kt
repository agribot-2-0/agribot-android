package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolparams

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.ToolDetails
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.HitchListItemDetail
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemViewModel

class ToolParamsViewModel(
    private val toolDataManager: ToolDataManager,
    private val resourcesProvider: ResourcesProvider,
    private val cloudDatabaseService: CloudDatabaseService
) : BaseViewModel() {
    private val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val paramsList: MutableLiveData<List<HitchListItemViewModel>> =
        MutableLiveData(listOf())
    val toolRfidTag: MutableLiveData<String> = MutableLiveData()

    private fun addToolParamItemsToRecycler() {
        addToolToToolParamList(
            toolName = resourcesProvider.getString(R.string.v_max),
            switchNumber = ToolRecylerItemIds.ID_V_MAX,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.vMax.toInt()
            )
        )
        addToolToToolParamList(
            toolName = resourcesProvider.getString(R.string.green_flow),
            switchNumber = ToolRecylerItemIds.ID_GREEN,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.green.toInt()
            )
        )
        addToolToToolParamList(
            toolName = resourcesProvider.getString(R.string.yellow_flow),
            switchNumber = ToolRecylerItemIds.ID_YELLOW,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.yellow.toInt()
            )
        )
        addToolToToolParamList(
            toolName = resourcesProvider.getString(R.string.blue_flow),
            switchNumber = ToolRecylerItemIds.ID_BLUE,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.blue.toInt()
            )
        )
        addToolToToolParamList(
            toolName = resourcesProvider.getString(R.string.pto),
            switchNumber = ToolRecylerItemIds.ID_PTO,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.pto.toInt()
            )
        )
        addToolToToolParamList(
            toolName = resourcesProvider.getString(R.string.tpl_push_pull),
            switchNumber = ToolRecylerItemIds.ID_TPL_PUSH_PULL,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.tplPushPull.toInt()
            )
        )
        addToolToToolParamList(
            toolName = resourcesProvider.getString(R.string.tpl_up_down),
            switchNumber = ToolRecylerItemIds.ID_TPL_UP_DOWN,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.tplUpDown.toInt()
            )
        )
    }

    fun onHitchParametersChanged(
        sliderNumber: Int,
        sliderValue: Int,
        isActivated: Boolean
    ) {
        // TODO when switch is activated send data via bluetooth in order to control tools
        when (sliderNumber) {
            ToolRecylerItemIds.ID_PTO -> {
                toolDataManager.pto = sliderValue.toDouble()
            }
            ToolRecylerItemIds.ID_TPL_UP_DOWN -> {
                toolDataManager.tplUpDown = sliderValue.toDouble()
            }
            ToolRecylerItemIds.ID_TPL_PUSH_PULL -> {
                toolDataManager.tplPushPull = sliderValue.toDouble()
            }
            ToolRecylerItemIds.ID_GREEN -> {
                toolDataManager.green = sliderValue.toDouble()
            }
            ToolRecylerItemIds.ID_YELLOW -> {
                toolDataManager.yellow = sliderValue.toDouble()
            }
            ToolRecylerItemIds.ID_BLUE -> {
                toolDataManager.blue = sliderValue.toDouble()
            }
            ToolRecylerItemIds.ID_V_MAX -> {
                toolDataManager.vMax = sliderValue.toDouble()
            }
        }
    }

    private fun readRfidTag() {
        if (toolDataManager.rfid == toolDataManager.noRfidTag) {
            // TODO add scaning rfid via bluetooth
        }
    }

    private fun addToolToToolParamList(
        toolName: String,
        switchNumber: Int,
        hitchListItemDetail: HitchListItemDetail
    ) {
        val isToolAlreadyAdded = paramsList.value!!.any() { it ->
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
        readRfidTag()
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
}
