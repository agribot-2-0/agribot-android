package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolparams

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNavigationEvent
import eu.agribot.agribotapp.domain.model.ToolDetails
import eu.agribot.agribotapp.domain.model.cdb.taskprogram.taskmodel.AddToolDetails
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager
import eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.ToolListItemViewModel


class ToolParamsViewModel(
    private val toolDataManager: ToolDataManager,
    private val cloudDatabaseService: CloudDatabaseService
) : BaseViewModel() {
    val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val ptoValue: MutableLiveData<Int> = MutableLiveData()
    val ptoString: MutableLiveData<String> = MutableLiveData()

    fun sendToolData() {
        postToolManagementNavigationEvent()
//        toolDataManager.onUpdatePto(ptoValue.value!!.toDouble())
        Log.w("ToolParamsViewModel", ptoValue.value!!.toDouble().toString())
        cloudDatabaseService.addTool(
            toolName = toolDataManager.getToolName(),
            addToolDetails = AddToolDetails(
                rfid = toolDataManager.getRfid(),
                pto = ptoValue.value!!.toDouble()
            )
        )
    }

    fun onStart() {
        cloudDatabaseService.getToolDetails(
            toolName = toolDataManager.getToolName(),
            onGetToolDetails = this::onToolUpdate,
            onGetToolDetailsError = this::postToastErrorMessageEvent
        )
    }

    fun onToolUpdate(toolDetails: ToolDetails) {
            ptoValue.value = toolDetails.pto.toInt()
//            ptoString.value = toolDetails.pto.toString() + " rpm"
    }


    fun postToolManagementNavigationEvent() {
        navigationEvent.postValue(
            ToolNavigationEvent()
        )
    }

    private fun postToastErrorMessageEvent(errorMessage: String) {
        toastMessageEvent.postValue(errorMessage)
    }
}
