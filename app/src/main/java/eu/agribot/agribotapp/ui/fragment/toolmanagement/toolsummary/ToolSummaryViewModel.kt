package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolsummary

import android.util.Log
import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.ToolNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.ToolDetails
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.HitchListItemDetail
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemViewModel
import eu.agribot.agribotapp.ui.fragment.toolmanagement.findtag.data.TagRfidFormatter
import eu.agribot.agribotapp.ui.fragment.toolmanagement.findtag.data.ToolRecylerItemId

class ToolSummaryViewModel(
    private val toolDataManager: ToolDataManager,
    private val cloudDatabaseService: CloudDatabaseService,
    private val resourcesProvider: ResourcesProvider
    ): BaseViewModel() {

    val toolName : MutableLiveData<String> = MutableLiveData()
    val toolRfidTag : MutableLiveData<String> = MutableLiveData()
    val paramsList: MutableLiveData<List<HitchListItemViewModel>> =
        MutableLiveData(listOf())


    fun setSummaryParameters(){
        setToolName()
        setToolTag()
        addToolParamItemsToRecycler()
    }

    private fun setToolName(){
        toolName.value = String.format(resourcesProvider.getString(R.string.summary_tool_name),toolDataManager.toolName)
    }

    private fun setToolTag(){
        toolRfidTag.value = String.format(resourcesProvider.getString(R.string.summary_tool_tag),getDisplayRfidTag(toolDataManager.rfid))
        Log.e("xd"," " + toolDataManager.rfid )
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

    fun getToolPhotoName() : String{
        return "/" + toolDataManager.toolName + ".jpg"
    }

    fun sendToolData(){
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

    private fun onAddNewToolSucess(){
        navigationEvent.postValue(
            ToolNavigationEvent()
        )
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

}