package eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.GoogleMap
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskNameNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskPathNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentCreatorConfiguratorBinding
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.TaskListItemAdapter
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.TaskListItemViewModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.swiperecyclerutils.SwipeHelper
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.swiperecyclerutils.SwipeRecyclerClickListener
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.swiperecyclerutils.SwipeRecyclerHelper
import kotlinx.android.synthetic.main.fragment_creator_configurator.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TaskCreatorFragment :
    BaseFragment<TaskCreatorViewModel, FragmentCreatorConfiguratorBinding>() {


    override val layoutResourceId: Int = R.layout.fragment_creator_configurator
    override val viewModel: TaskCreatorViewModel by viewModel { parametersOf(requireContext()) }
    private var googleMaps: GoogleMap? = null
    private var adapter = TaskListItemAdapter(this::onItemClicked)

    override fun bindViewVariables(viewBinding: FragmentCreatorConfiguratorBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.toastMessageEvent.observe(this::showToast)
        //val mapFragment =
         //   childFragmentManager.findFragmentById(R.id.creator_map) as SupportMapFragment
       // mapFragment.getMapAsync(this)
        setupRecycler()
        viewModel.getAvailableTask()
        viewModel.taskList.observe(this::addToTaskList)
    }

    private fun setupRecycler() {
        task_recycler.adapter = adapter
        task_recycler.layoutManager = LinearLayoutManager(requireActivity())
        task_recycler.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        ItemTouchHelper(
            SwipeRecyclerHelper(
                task_recycler,
                deleteButton = this::deleteButton,
                editButton = this::editButton
            )
        ).attachToRecyclerView(task_recycler)
    }

    private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            getString(R.string.delete_task_button),
            resources.getDimension(R.dimen.text_size_small),
            android.R.color.holo_red_light,
            SwipeRecyclerClickListener(position, onButtonClicked = viewModel::onDeleteButtonClicked)
        )
    }

    private fun editButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            getString(R.string.edit_task_button),
            resources.getDimension(R.dimen.text_size_small),
            android.R.color.holo_blue_light,
            SwipeRecyclerClickListener(position, onButtonClicked = viewModel::onEditButtonClicked)
        )
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is CreatorTaskNameNavigationEvent -> navigateToNewTaskName()
        is CreatorTaskPathNavigationEvent -> navigateToPathSelect()
        else -> Unit
    }

    private fun navigateToPathSelect() {
        navigationController?.navigate(R.id.action_taskCreatorFragment_to_taskPathCreatorFragment)
    }

    private fun navigateToNewTaskName() {
        navigationController?.navigate(R.id.action_taskCreatorFragment_to_taskNameCreatorFragment)
    }

    private fun onItemClicked(taskCreatorViewModel: TaskListItemViewModel) {
        viewModel.onTaskSelected(taskCreatorViewModel)
    }

    private fun addToTaskList(task: List<TaskListItemViewModel>) {
        adapter.items = task
    }

}
