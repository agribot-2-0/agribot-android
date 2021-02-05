package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolsummary

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.ToolNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.ToolDetails
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager
import eu.agribot.agribotapp.ui.fragment.toolmanagement.findtag.data.TagRfidFormatter
import eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.toolparameters.ListToolParamItem
import eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.toolparameters.ListToolParamViewModel

class ToolSummaryViewModel(
    private val toolDataManager: ToolDataManager,
    private val cloudDatabaseService: CloudDatabaseService,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {

    val toolName: MutableLiveData<String> = MutableLiveData()
    val toolRfidTag: MutableLiveData<String> = MutableLiveData()
    val paramsList: MutableLiveData<List<ListToolParamViewModel>> =
        MutableLiveData(listOf())

    fun setSummaryParameters() {
        setToolName()
        setToolTag()
        setAllParameters()
    }

    private fun setToolName() {
        toolName.value = String.format(resourcesProvider.getString(R.string.summary_tool_name), toolDataManager.toolName)
    }

    private fun setToolTag() {
        toolRfidTag.value = String.format(resourcesProvider.getString(R.string.summary_tool_tag), getDisplayRfidTag(toolDataManager.rfid))
    }

    private fun getDisplayRfidTag(tagIDArray: List<Int>): String {
        var rfidTagHexString = ""
        tagIDArray.forEach {
            rfidTagHexString += String.format(
                TagRfidFormatter.NUMBER_FORMAT.formatElement,
                it
            ) + TagRfidFormatter.NUMBER_SEPARATOR.formatElement
        }
        return rfidTagHexString
    }

    fun getToolPhotoName(): String {
        return "/" + toolDataManager.toolName + ".jpg"
    }

    fun sendToolData() {
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
            onSucess = this::onAddNewToolSucess
        )
    }

    private fun onAddNewToolSucess() {
        navigationEvent.postValue(
            ToolNavigationEvent()
        )
    }

    private fun setAllParameters() {
        toolDataManager.let {
            addToParamList(getTplUpDownItem(it.tplUpDown.toInt()))
            addToParamList(getTplPushItem(it.tplPushPull.toInt()))
            addToParamList(getPtoItemItem(it.pto.toInt()))
            addToParamList(getBlueFlowItem(it.blue.toInt()))
            addToParamList(getYellowFlowItem(it.yellow.toInt()))
            addToParamList(getGreenFlowItem(it.green.toInt()))
            addToParamList(getVelocityItem(it.vMax.toInt()))
        }
    }

    private fun getTplUpDownItem(tplUpDownValue: Int): ListToolParamViewModel {
        return ListToolParamViewModel(
            ListToolParamItem.TPL_UP_DOWN.toolParamId,
            resourcesProvider.getString(R.string.tpl_up_down),
            tplUpDownValue, resourcesProvider.getString(R.string.rpm)
        )
    }

    private fun getTplPushItem(tplPushValue: Int): ListToolParamViewModel {
        return ListToolParamViewModel(
            ListToolParamItem.TPL_PUSH_PULL.toolParamId,
            resourcesProvider.getString(R.string.tpl_push_pull),
            tplPushValue, resourcesProvider.getString(R.string.rpm)
        )
    }

    private fun getPtoItemItem(ptoValue: Int): ListToolParamViewModel {
        return ListToolParamViewModel(
            ListToolParamItem.PTO.toolParamId,
            resourcesProvider.getString(R.string.pto),
            ptoValue, resourcesProvider.getString(R.string.rpm)
        )
    }

    private fun getBlueFlowItem(blueFlowValue: Int): ListToolParamViewModel {
        return ListToolParamViewModel(
            ListToolParamItem.BLUE_FLOW.toolParamId,
            resourcesProvider.getString(R.string.blue_flow),
            blueFlowValue, resourcesProvider.getString(R.string.rpm)
        )
    }

    private fun getYellowFlowItem(yellowFlowValue: Int): ListToolParamViewModel {
        return ListToolParamViewModel(
            ListToolParamItem.YELLOW_FLOW.toolParamId,
            resourcesProvider.getString(R.string.yellow_flow), yellowFlowValue,
            resourcesProvider.getString(R.string.rpm)
        )
    }

    private fun getGreenFlowItem(greenFlowValue: Int): ListToolParamViewModel {
        return ListToolParamViewModel(
            ListToolParamItem.GREEN_FLOW.toolParamId,
            resourcesProvider.getString(R.string.green_flow),
            greenFlowValue, resourcesProvider.getString(R.string.rpm)
        )
    }

    private fun getVelocityItem(velocityVmaxValue: Int): ListToolParamViewModel {
        return ListToolParamViewModel(
            ListToolParamItem.VELOCITY.toolParamId,
            resourcesProvider.getString(R.string.v_max),
            velocityVmaxValue, resourcesProvider.getString(R.string.rpm)
        )
    }

    private fun addToParamList(listToolParam: ListToolParamViewModel) {
        val isAttributeAlreadyAdded = paramsList.value!!.any { arrtibute ->
            arrtibute.toolParamID == listToolParam.toolParamID
        }
        if (!isAttributeAlreadyAdded)
            paramsList.value = listOf(
                *paramsList.value!!.toTypedArray(),
                ListToolParamViewModel(
                    listToolParam.toolParamID,
                    listToolParam.paramName,
                    listToolParam.paramValue,
                    listToolParam.paramUnit
                )
            )
        else {
            paramsList.value = paramsList.value!!.map { oldAttribute ->
                if (oldAttribute.toolParamID == listToolParam.toolParamID) {
                    ListToolParamViewModel(
                        oldAttribute.toolParamID,
                        oldAttribute.paramName,
                        listToolParam.paramValue,
                        oldAttribute.paramUnit
                    )
                } else {
                    oldAttribute
                }
            }
        }
    }
}
