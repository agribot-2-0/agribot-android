package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolparams

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNavigationEvent
import eu.agribot.agribotapp.domain.model.cdb.ToolDetails
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.HitchListItemDetail
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemViewModel

class ToolParamsViewModel(
    private val toolDataManager: ToolDataManager,
    private val cloudDatabaseService: CloudDatabaseService
) : BaseViewModel() {
    private val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val paramsList: MutableLiveData<List<HitchListItemViewModel>> =
        MutableLiveData(listOf())

    private fun addToolParamItemsToRecycler() {
        addToolToToolParamList(
            toolName = "Max robot velocity",
            switchNumber = ToolRecylerItemIds.ID_V_MAX,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.getVMax().toInt()
            )
        )
        addToolToToolParamList(
            toolName = "YELLOW",
            switchNumber = ToolRecylerItemIds.ID_YELLOW,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.getYellow().toInt()
            )
        )
        addToolToToolParamList(
            toolName = "GREEN",
            switchNumber = ToolRecylerItemIds.ID_GREEN,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.getGreen().toInt()
            )
        )
        addToolToToolParamList(
            toolName = "BLUE",
            switchNumber = ToolRecylerItemIds.ID_BLUE,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.getBlue().toInt()
            )
        )
        addToolToToolParamList(
            toolName = "PTO",
            switchNumber = ToolRecylerItemIds.ID_PTO,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.getPto().toInt()
            )
        )
        addToolToToolParamList(
            toolName = "TPL PUSH/PULL",
            switchNumber = ToolRecylerItemIds.ID_TPL_PUSH_PULL,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.getTplPushPull().toInt()
            )
        )
        addToolToToolParamList(
            toolName = "TPL UP/DOWN",
            switchNumber = ToolRecylerItemIds.ID_TPL_UP_DOWN,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.getTplUpDown().toInt()
            )
        )
    }

    fun onHitchParametersChanged(
        sliderNumber: Int,
        sliderValue: Int,
        isActivated: Boolean
    ) {
        if (sliderNumber == ToolRecylerItemIds.ID_PTO) {
            toolDataManager.onUpdatePto(sliderValue.toDouble())
        }
        if (sliderNumber == ToolRecylerItemIds.ID_TPL_UP_DOWN) {
            toolDataManager.onUpdateTplUpDown(sliderValue.toDouble())
        }
        if (sliderNumber == ToolRecylerItemIds.ID_TPL_PUSH_PULL) {
            toolDataManager.onUpdateTplPushPull(sliderValue.toDouble())
        }
        if (sliderNumber == ToolRecylerItemIds.ID_GREEN) {
            toolDataManager.onUpdateGreen(sliderValue.toDouble())
        }
        if (sliderNumber == ToolRecylerItemIds.ID_YELLOW) {
            toolDataManager.onUpdateYellow(sliderValue.toDouble())
        }
        if (sliderNumber == ToolRecylerItemIds.ID_BLUE) {
            toolDataManager.onUpdateBlue(sliderValue.toDouble())
        }
        if (sliderNumber == ToolRecylerItemIds.ID_V_MAX) {
            toolDataManager.onUpdateVMax(sliderValue.toDouble())
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
            toolName = toolDataManager.getToolName(),
            toolDetails = ToolDetails(
                rfid = toolDataManager.getRfid(),
                pto = toolDataManager.getPto(),
                tpl_push_pull = toolDataManager.getTplPushPull(),
                tpl_up_down = toolDataManager.getTplUpDown(),
                blue = toolDataManager.getBlue(),
                yellow = toolDataManager.getYellow(),
                green = toolDataManager.getGreen(),
                v_max = toolDataManager.getVMax()
            )
        )
    }

    fun onStart() {
        cloudDatabaseService.getToolDetails(
            toolName = toolDataManager.getToolName(),
            onGetToolDetails = this::onToolUpdate,
            onGetToolDetailsError = this::postToastErrorMessageEvent
        )
        addToolParamItemsToRecycler()
    }

    private fun onToolUpdate(toolDetails: ToolDetails) {
        toolDataManager.updateAllToolParameters(toolDetails)
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
}
