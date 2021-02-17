package eu.agribot.agribotapp.ui.fragment.controlPanel.taskselect

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.dialog.DialogManager
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.DashboardNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentTaskSelectBinding
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.TaskListItemAdapter
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.TaskListItemViewModel
import kotlinx.android.synthetic.main.fragment_task_select.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TaskSelectFragment : BaseFragment<TaskSelectViewModel, FragmentTaskSelectBinding>() {

    private val dialogManager: DialogManager by inject { parametersOf(requireContext()) }

    override val layoutResourceId: Int = R.layout.fragment_task_select
    override val viewModel: TaskSelectViewModel by viewModel()
    override fun bindViewVariables(viewBinding: FragmentTaskSelectBinding) {
        viewBinding.viewModel = viewModel
    }

    private val taskAdapter = TaskListItemAdapter(this::onTaskSelected)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        viewModel.taskList.observe(this::addTaskItem)
        viewModel.getAvailableTask()
        viewModel.toastEvent.observe(this::showToast)
        viewModel.cancelTaskButtonEvent.observe(this::onCancelTask)
    }

    private fun onTaskSelected(taskListItemViewModel: TaskListItemViewModel) {
        dialogManager.showTaskSelectedDialog(
            taskName = taskListItemViewModel.taskName,
            viewModel::setCurrentTask
        )
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is DashboardNavigationEvent -> handleDashboardNavigationEvent()
        else -> Unit
    }

    private fun handleDashboardNavigationEvent() {
        navigationController?.navigate(R.id.action_selectTaskFragment_to_dashboardFragment)
    }

    private fun setupRecycler() {
        task_select_recycler.adapter = taskAdapter
        task_select_recycler.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun addTaskItem(task: List<TaskListItemViewModel>) {
        taskAdapter.items = task
    }

    private fun onCancelTask(cancelTask: Boolean) {
        if (cancelTask) {
            dialogManager.cancelTaskDialog(viewModel::sendCancelTask)
        }
    }
}
