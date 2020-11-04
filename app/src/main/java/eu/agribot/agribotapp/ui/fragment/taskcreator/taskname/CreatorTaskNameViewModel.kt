package eu.agribot.agribotapp.ui.fragment.taskcreator.taskname

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskPathNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.TaskDetails
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager

class CreatorTaskNameViewModel(
    private val taskCreatorDataManager: TaskCreatorDataManager,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {
    val taskName: MutableLiveData<String> = MutableLiveData()
    val toastEvent: MutableLiveEvent<String> = MutableLiveEvent()

    fun onTaskNameChanged(text: CharSequence) {
        taskName.value = text.toString()
    }

    fun onNextButtonClicked() {
        if (taskName.value != null) {
            taskCreatorDataManager.updateTaskParameters(TaskDetails(task_name = taskName.value.toString()))
            navigationEvent.postValue(
                CreatorTaskPathNavigationEvent()
            )
        } else {
            toastEvent.postValue(resourcesProvider.getString(R.string.empty_task_name))
        }
    }
}
