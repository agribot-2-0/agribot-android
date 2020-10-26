package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolname

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskPathNavigationEvent
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager

class ToolNameViewModel() : BaseViewModel() {
val taskName: MutableLiveData<String> = MutableLiveData()
    val toastEvent: MutableLiveEvent<String> = MutableLiveEvent()

    fun onTaskNameChanged(text: CharSequence) {
        taskName.value = text.toString()
    }

    fun onNextButtonClicked() {
//        if (taskName.value != null) {
//            taskCreatorDataManager.onUpdateTaskName(taskName.value.toString())
//            navigationEvent.postValue(
//                CreatorTaskPathNavigationEvent()
//            )
//        } else {
//            toastEvent.postValue("No task name")
//        }
    }
}
