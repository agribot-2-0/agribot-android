package eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.dialog.DialogManager
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNameNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentToolManagementBinding
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.TaskListItemAdapter
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.swiperecyclerutils.SwipeHelper
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.swiperecyclerutils.SwipeRecyclerClickListener
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.swiperecyclerutils.SwipeRecyclerHelper
import eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.ToolManagementViewModel
import eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.ToolListItemAdapter
import eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.ToolListItemViewModel
import kotlinx.android.synthetic.main.fragment_creator_configurator.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ToolManagementFragment :
    BaseFragment<ToolManagementViewModel, FragmentToolManagementBinding>() {

    private val dialogManager: DialogManager by inject { parametersOf(requireContext()) }
    override val layoutResourceId: Int = R.layout.fragment_tool_management
    override val viewModel: ToolManagementViewModel by viewModel { parametersOf(requireContext()) }
    private var adapter = ToolListItemAdapter(this::onItemClicked)

    private fun onItemClicked(toolListItemViewModel: ToolListItemViewModel) {
        viewModel.onTaskSelected(toolListItemViewModel)
    }

//    private fun setupRecycler() {
//        task_recycler.adapter = adapter
//        task_recycler.layoutManager = LinearLayoutManager(requireActivity())
//        task_recycler.addItemDecoration(
//            DividerItemDecoration(
//                requireContext(),
//                DividerItemDecoration.VERTICAL
//            )
//        )
//        ItemTouchHelper(
//            SwipeRecyclerHelper(
//                task_recycler,
//                deleteButton = this::deleteButton,
//                editButton = this::editButton
//            )
//        ).attachToRecyclerView(task_recycler)
//    }

    private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            getString(R.string.delete_task_button),
            resources.getDimension(R.dimen.text_size_small),
            android.R.color.holo_red_light,
            SwipeRecyclerClickListener(position, onButtonClicked = this::showDeleteTaskDialog)
        )
    }

    private fun showDeleteTaskDialog(itemPosition: Int) {
        dialogManager.showDeleteTaskDialog(itemPosition, viewModel::onDeleteButtonClicked)
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

    override fun bindViewVariables(viewBinding: FragmentToolManagementBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ToolNameNavigationEvent -> navigateToToolName()
        else -> Unit
    }

    private fun navigateToToolName() {
        navigationController?.navigate(R.id.action_toolManagementFragment_to_toolManagementNameFragment)
    }

}
